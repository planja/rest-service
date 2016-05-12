package com.guru.parser.impl.qfparser;

import com.guru.domain.model.ClasInfo;
import com.guru.domain.model.Flight;
import com.guru.domain.model.MileCost;
import com.guru.domain.model.Trip;
import com.guru.domain.repository.MileCostRepository;
import com.guru.domain.repository.QueryRepository;
import com.guru.parser.account.Account;
import com.guru.parser.interf.Parser;
import com.guru.parser.proxy.ProxyUtils;
import com.guru.parser.utils.AccountUtils;
import com.guru.parser.utils.ParserUtils;
import com.guru.vo.transfer.RequestData;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by Никита on 27.04.2016.
 */

@Component
public class QFParser implements Parser {


    @Inject
    private MileCostRepository mileCostRepository;


    @Override
    public Collection<Trip> parse(RequestData requestData) throws Exception {


        List<Trip> result = getQantas(requestData);
        MileCost mileCost = null;
        if (result.size() != 0) {
            List<MileCost> miles = StreamSupport.stream(Spliterators.spliteratorUnknownSize(mileCostRepository.findAll().iterator(), Spliterator.ORDERED), false)
                    .collect(Collectors.toCollection(ArrayList::new));
            mileCost = miles.stream().filter(o -> Objects.equals(o.getParser(), result.get(0).getFlights().get(0).getParser()))
                    .findFirst().get();
            result.get(0).setIsComplete(true);
        }

        setMiles2Trip(result, mileCost);
        return result;
    }

    static void setClientProxyProperties(DefaultHttpClient httpclient, Account account) throws IOException {
        String credent = null;
        String ipport = null;

        if (account.isProxy()) {

            credent = account.getProxy_login() + ":" + account.getProxy_password();
            ipport = account.getIp() + ":" + account.getPort();

        } else {

            String proxyInfo = ProxyUtils.getProxy("AA");

            credent = proxyInfo.split("@")[0];
            ipport = proxyInfo.split("@")[1];
        }

        HttpHost proxy = new HttpHost(ipport.split(":")[0], Integer.parseInt(ipport.split(":")[1]));

        Credentials credentials = new UsernamePasswordCredentials(credent.split(":")[0], credent.split(":")[1]);
        AuthScope authScope = new AuthScope(ipport.split(":")[0], Integer.parseInt(ipport.split(":")[1]));
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(authScope, credentials);

        httpclient.setCredentialsProvider(credsProvider);
        httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
    }

    private void setMiles2Trip(List<Trip> trips, MileCost mileCost) {
        if (mileCost == null) return;
        List<String> miles1 = new ArrayList<>();
        for (Trip trip : trips) {
            for (ClasInfo clasInfo : trip.getClasInfo()) {
                miles1.add(clasInfo.getMileage());
            }
        }
        for (Trip trip : trips) {
            String m = trip.getClasInfo().stream().filter(o -> Objects.equals(o.getReduction(), trip.getClas()))
                    .findFirst().get().getMileage();
            System.out.println(m);


            Integer miles = Integer.valueOf(trip.getClasInfo().stream()
                    .filter(o -> Objects.equals(o.getReduction(), trip.getClas()))
                    .findFirst().get().getMileage());
            trip.setMiles(miles);
            double parserCost = miles / 100 * mileCost.getCost().doubleValue();//ещё сложить таксы
            trip.setCost(BigDecimal.valueOf(parserCost));


        }
    }

    private DefaultHttpClient login(String user, String surname, String password, Account account) throws IOException, InterruptedException, ExecutionException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter("http.protocol.cookie-policy", "compatibility");
        httpclient.setCookieStore(new BasicCookieStore());
        httpclient.setRedirectStrategy(new LaxRedirectStrategy());

        setClientProxyProperties(httpclient, account);

        httpclient.getParams().setParameter("http.useragent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
        HttpPost httget = new HttpPost("http://www.qantas.com.au");
        httpclient.execute(httget).close();
        httget = new HttpPost("https://www.qantas.com.au/fflyer/dyns/dologin?action=login&origin=homepage");
        List<BasicNameValuePair> nameValuePairs = Arrays.asList(new BasicNameValuePair("login_ffNumber", user),
                new BasicNameValuePair("login_surname", surname), new BasicNameValuePair("login_pin", password));
        httget.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        CloseableHttpResponse response = httpclient.execute(httget);
        HttpEntity entity = response.getEntity();
        String login = IOUtils.toString(entity.getContent());
        Document logDoc = Jsoup.parse(login);
        return logDoc.getElementById("errormsgs") != null ? null : httpclient;
    }

    private List<Trip> getQantas(RequestData requestData) throws Exception, IOException, InterruptedException, ExecutionException, ParseException {
        Account account = AccountUtils.getAccount("QF");
        DefaultHttpClient httpclient = this.login("1924112640", "Kin", "4152", account);


        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        List<Trip> awardList = new ArrayList<>
                ((new ParserThread(httpclient, sdf1.format(requestData.getOw_start_date()),
                        sdf1.format(requestData.getOw_end_date()), requestData.getOrigin(),
                        requestData.getDestination(), requestData.getSeats(), this)).call());
        ExecutorService executor = Executors.newCachedThreadPool();
        LinkedList resultList = new LinkedList();
        HashSet callables = new HashSet();
        Iterator futureList = awardList.iterator();

        while (futureList.hasNext()) {
            Trip trip = (Trip) futureList.next();
            callables.add(new DataThread(trip, httpclient, account));
        }

        List futureList1 = executor.invokeAll(callables);
        Iterator award2 = futureList1.iterator();

        while (award2.hasNext()) {
            Future award1 = (Future) award2.next();
            resultList.add(award1.get());
        }

        award2 = resultList.iterator();

        List<Trip> trips = new ArrayList<>();
        List<Trip> ow = new ArrayList<>();
        List<Trip> rt = new ArrayList<>();


        while (award2.hasNext()) {
            Trip next = (Trip) award2.next();
            if (next.getDirection() == 0) {
                ow.add(next);
            } else {
                rt.add(next);
            }
        }
        if (Objects.equals(requestData.getType(), "ow")) {
            rt.forEach(o -> o.setQueryId(Long.valueOf("0")));
            ow.forEach(o -> o.setQueryId((long) requestData.getRequest_id()));
        } else {
            rt.forEach(o -> o.setQueryId((long) requestData.getRequest_id()));
            ow.forEach(o -> o.setQueryId((long) requestData.getRequest_id()));
        }


        trips.addAll(ow);
        trips.addAll(rt);
        System.out.println(trips.size());
        trips = getTrips(trips, requestData.getCabins());
        System.out.println("Search");
        System.out.println(trips.size());
        return trips;

    }

    private List<Flight> getFlightDur(List<Flight> flights) {
        for (Flight flight : flights) {
            long time = getDateDiff(flight.getFullStartDate(), flight.getFullEndDate(), TimeUnit.MINUTES);
            flight.setFlightDuration(ParserUtils.convertMinutes((int) time));
        }
        return flights;
    }


    private List<Trip> getTrips(List<Trip> trips, List<String> classes) {
        trips.stream().forEach(o -> {
            o.setFlights(getFlightDur(o.getFlights()));
            //  o.setQueryId((long) (new Random().nextDouble() * 123L));
            o.setArriveCode(o.getFlights().get(o.getFlights().size() - 1).getArriveCode());
            o.setDepartCode(o.getFlights().get(0).getDepartCode());

            o.setArrivePlace(o.getFlights().get(o.getFlights().size() - 1).getArrivePlace());
            o.setDepartPlace(o.getFlights().get(0).getDepartPlace());

            o.setTripDate(o.getFlights().get(0).getDepartDate());
            o.setStops(getStops(o.getFlights()));
            o.setCarriers(getCarriers(o.getFlights()));
            o.setFlightLegs(getFlightLegs(o.getFlights()));
            o.setFlightNumbers(getFlightNumbers(o.getFlights()));

            o.setCost(BigDecimal.valueOf(1000L + ((long) (new Random().nextDouble() * (2000L - 1000L)))));
            o.setUpdatedAt(new Date());
            o.setTripDuration(getTripDuration(o));
            o.setCreatedAt(new Date());
            o.getFlights().forEach(k -> k.setTrip(o));
            Map<String, List<Flight>> map = getLayovers(o.getFlights());
            if (map != null) {
                o.setLayovers(map.keySet().stream().findFirst().get());
                o.setFlights(map.values().stream().findFirst().get());
            } else {
                o.setLayovers("[\"00:00\"]");
            }


        });
        return setCabin(trips, classes);
    }

    private String getTripDuration(Trip trip) {
        long time = getDateDiff(trip.getFlights().get(0).getFullStartDate(),
                trip.getFlights().get(trip.getFlights().size() - 1).getFullEndDate(), TimeUnit.MINUTES);
        return ParserUtils.convertMinutes((int) time);
    }

    private List<Trip> setCabin(List<Trip> list, List<String> classes) {
        System.out.println(list.size());
        List<Trip> result = new ArrayList<>();
        for (Trip trip : list) {
            List<ClasInfo> sorted;
            List<ClasInfo> clasInfos = trip.getClasInfo();
            try {
                sorted = clasInfos.stream().filter(o -> classes.contains(o.getReduction()) && o.getStatus() != 0).collect(Collectors.toList());
            } catch (NoSuchElementException ex) {
                return new ArrayList<>();
            }
            for (ClasInfo clasInfo : sorted) {
                trip.setClas(clasInfo.getReduction());
                trip.getFlights().stream().forEach(o -> o.setCabin(clasInfo.getReduction()));
                List<Info> infos = new ArrayList<>();
                trip.getFlights().stream().forEach(o -> infos.add(new Info(o.getDepartPlace(), o.getArrivePlace())));
                for (int i = 0; i < trip.getFlights().size(); i++) {
                    for (int j = 0; j < clasInfo.getMixedCabins().size(); j++) {
                        String str = clasInfo.getMixedCabins().get(j);///????
                        List<String> strings = Arrays.asList(str.split(" "));
                        if (infos.get(i).getArrive().contains(strings.get(3).substring(0, strings.get(3).length() - 1)) &&
                                infos.get(i).getDepart().contains(strings.get(1))) {
                            String clas = "N";
                            if (str.contains("Economy"))
                                clas = "E";
                            if (str.contains("Business"))
                                clas = "B";
                            if (str.contains("First"))
                                clas = "F";
                            trip.getFlights().get(i).setCabin(clas);
                        }

                    }
                }
                trip.setCabins(getCabins(trip.getFlights()));
                result.add(trip);
            }
        }
        System.out.println(result.size());
        return result;
    }

    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    private String getCabins(List<Flight> flights) {
        String str = "[" + flights.stream().map(o -> "\"" + o.getCabin() + "\",").collect(Collectors.joining()) + "]";
        int index = str.lastIndexOf(',');//Переделать
        return str.substring(0, index) + str.substring(index + 1, str.length());
    }

    private Map<String, List<Flight>> getLayovers(List<Flight> flights) {
        List<Long> longs = new ArrayList<>();
        if (flights.size() == 1) return null;
        for (int i = 0; i < flights.size() - 1; i++) {
            longs.add(getDateDiff(flights.get(i).getFullEndDate(),
                    flights.get(i + 1).getFullStartDate(), TimeUnit.MINUTES));
        }
        List<String> list = longs.stream().map(o -> ParserUtils.convertMinutes(Integer.valueOf(o.toString()))).collect(Collectors.toList());
        for (int i = 1; i <= flights.size() - 1; i++) {
            flights.get(i).setLayover(list.get(i - 1));
        }

        String str = "[" + list.stream().map(o -> "\"" + o.toString() + "\",").collect(Collectors.joining()) + "]";
        int index = str.lastIndexOf(',');
        Map<String, List<Flight>> map = new TreeMap<>();
        String layovers = str.substring(0, index) + str.substring(index + 1, str.length());
        for (int i = 1; i < flights.size(); i++) {
            flights.get(i).setLayover(list.get(i - 1));
        }
        map.put(layovers, flights);
        return map;
    }

    private String getStops(List<Flight> flights) {
        if (flights.size() == 1) return "[]";
        else {
            String str = "[" + flights.subList(1, flights.size()).stream()
                    .map(o -> "\"" + o.getDepartCode() + "\",").collect(Collectors.joining()) + "]";
            int index = str.lastIndexOf(',');//Переделать
            return str.substring(0, index) + str.substring(index + 1, str.length());
        }

    }

    private String getCarriers(List<Flight> flights) {
        String str = "[" + flights.stream().map(o -> "\"" + o.getCarrierCode() + "\",").collect(Collectors.joining()) + "]";//??????
        int index = str.lastIndexOf(',');//Переделать
        return str.substring(0, index) + str.substring(index + 1, str.length());
    }

    private String getFlightLegs(List<Flight> flights) {
        String str = "[" + flights.stream().map(o -> "\"" + o.getFlightDuration() + "\",").collect(Collectors.joining()) + "]";
        int index = str.lastIndexOf(',');//Переделать
        return str.substring(0, index) + str.substring(index + 1, str.length());
    }


    private String getFlightNumbers(List<Flight> flights) {
        String str = "[" + flights.stream().map(o -> "\"" + o.getFlightNumber() + "\",").collect(Collectors.joining()) + "]";
        int index = str.lastIndexOf(',');//Переделать
        return str.substring(0, index) + str.substring(index + 1, str.length());
    }
}
