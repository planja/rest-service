package com.guru.parser.impl.qfparser;

import com.guru.domain.model.ClasInfo;
import com.guru.domain.model.Flight;
import com.guru.domain.model.Trip;
import com.guru.parser.interf.Parser;
import com.guru.parser.utils.ParserUtils;
import com.guru.vo.transfer.RequestData;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by Никита on 27.04.2016.
 */

public class QFParser implements Parser {

    @Override
    public Collection<Trip> parse(RequestData requestData) throws Exception {
        return getQantas(requestData);
    }

    private DefaultHttpClient login(String user, String surname, String password) throws IOException, InterruptedException, ExecutionException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter("http.protocol.cookie-policy", "compatibility");
        httpclient.setCookieStore(new BasicCookieStore());
        httpclient.setRedirectStrategy(new LaxRedirectStrategy());
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
        DefaultHttpClient httpclient = this.login("1924112640", "Kin", "4152");
        Date start, end;
        if (Objects.equals(requestData.getType(), "ow")) {
            start = requestData.getOw_start_date();
            end = requestData.getOw_end_date();
        } else {
            start = requestData.getRt_start_date();
            end = requestData.getRt_end_date();
        }
        System.out.println(start);
        System.out.println(end);
        System.out.println(requestData.getOrigin());
        System.out.println(requestData.getDestination());
        System.out.println(requestData.getSeats());

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        List<Trip> awardList = new ArrayList<>
                ((new ParserThread(httpclient, sdf1.format(start),
                        sdf1.format(end), requestData.getOrigin(),
                        requestData.getDestination(), requestData.getSeats(), this)).call());
        ExecutorService executor = Executors.newCachedThreadPool();
        LinkedList resultList = new LinkedList();
        HashSet callables = new HashSet();
        Iterator futureList = awardList.iterator();

        while (futureList.hasNext()) {
            Trip trip = (Trip) futureList.next();
            callables.add(new DataThread(trip, httpclient));
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
        }else{
            rt.forEach(o -> o.setQueryId((long) requestData.getRequest_id()));
            ow.forEach(o -> o.setQueryId((long) requestData.getRequest_id()));
        }

       /*parser.qf.QFParser qfParser = new parser.qf.QFParser();
        ComplexAward complexAward = qfParser.getQantas(httpclient, start,
                end, requestData.getOrigin(),
                requestData.getDestination(), requestData.getSeats());*/


        trips.addAll(ow);
        trips.addAll(rt);
        System.out.println(trips.size());
        trips = getTrips(trips, requestData.getCabins());
        System.out.println("Search");
        System.out.println(trips.size());
        return trips;

    }


    private List<Trip> getTrips(List<Trip> trips, List<String> classes) {
        trips.stream().forEach(o -> {
            //  o.setQueryId((long) (new Random().nextDouble() * 123L));
            o.setArriveCode(o.getFlights().get(o.getFlights().size() - 1).getArriveCode());
            o.setDepartCode(o.getFlights().get(0).getDepartCode());

            o.setArrivePlace(o.getFlights().get(0).getArrivePlace());
            o.setDepartPlace(o.getFlights().get(o.getFlights().size() - 1).getArrivePlace());

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
                o.setLayovers("[]");
            }


        });
        return setCabin(trips, classes);
    }

    private String getTripDuration(Trip trip) {
        if (trip.getTripDuration() != null) return trip.getTripDuration();
        else {
            long time = getDateDiff(trip.getFlights().get(0).getFullStartDate(),
                    trip.getFlights().get(trip.getFlights().size() - 1).getFullEndDate(), TimeUnit.MINUTES);
            return ParserUtils.convertMinutes((int) time);
        }
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
                trip.getFlights().stream().forEach(o -> o.setCabin(clasInfo.getReduction()));
                List<Info> infos = new ArrayList<>();
                trip.getFlights().stream().forEach(o -> infos.add(new Info(o.getDepartPlace(), o.getArrivePlace())));
                for (int i = 0; i < trip.getFlights().size(); i++) {
                    for (int j = 0; j < clasInfo.getMixedCabins().size(); j++) {
                        String str = clasInfo.getMixedCabins().get(j);
                        List<String> strings = Arrays.asList(str.split(" "));
                        if (infos.get(i).getArrive().contains(strings.get(3).substring(0, strings.get(3).length() - 1)) &&
                                infos.get(i).getDepart().contains(strings.get(1)))
                            trip.getFlights().get(i).setCabin(strings.get(4).substring(0, 1));
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
