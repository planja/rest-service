package com.guru.parser.impl.acparser;

import com.guru.domain.model.ClasInfo;
import com.guru.domain.model.Flight;
import com.guru.domain.model.MileCost;
import com.guru.domain.model.Trip;
import com.guru.domain.repository.MileCostRepository;
import com.guru.parser.account.Account;
import com.guru.parser.interf.Parser;
import com.guru.parser.proxy.ProxyUtils;
import com.guru.parser.utils.AccountUtils;
import com.guru.parser.utils.ComplexTrip;
import com.guru.parser.utils.ParserUtils;
import com.guru.vo.transfer.RequestData;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import parser.exceptions.IncorrectCredentials;
import parser.exceptions.MaintenanceException;
import parser.utils.ComplexAward;

import javax.inject.Inject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by Никита on 11.05.2016.
 */
@Component
public class ACParser implements Parser {

    @Inject
    private MileCostRepository mileCostRepository;

    @Override
    public Collection<Trip> parse(RequestData requestData) throws Exception {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String owDate = df.format(requestData.getOw_start_date());
        String rtDate = df.format(requestData.getRt_start_date());

      /*  parser.ac.ACParser acParser = new parser.ac.ACParser();

        parser.utils.Account account1 = parser.utils.AccountUtils.getAccount("AC");

        //538332420

        DefaultHttpClient httpclient1 = parser.ac.ACParser.login("947", "826", "111", "test1985", account1);

//        DefaultHttpClient httpclient = ACParser.login("587", "807", "934", "fdsf2g34t8I", account);

        ComplexAward flights1 = acParser.getAC(httpclient1, owDate, rtDate, requestData.getOrigin(),
                requestData.getDestination(), requestData.getSeats());*/

        Account account = AccountUtils.getAccount("AC");
        DefaultHttpClient httpclient = login("947", "826", "111", "test1985", account);
        ComplexTrip flights = getAC(httpclient, owDate, rtDate, requestData.getOrigin(), requestData.getDestination(),
                requestData.getSeats(), account);
        List<Trip> owTrips = getTrips(flights.getOneWayList(), requestData.getCabins(),
                (long) requestData.getRequest_id());
      //  List<Trip> rtTrips = getTrips(flights.getReturnWayList(), requestData.getCabins(),
            //    (long) requestData.getRequest_id());


        MileCost mileCost = null;
        if (owTrips.size() != 0) {
            List<MileCost> miles = StreamSupport.stream(Spliterators.spliteratorUnknownSize(mileCostRepository.findAll().iterator(), Spliterator.ORDERED), false)
                    .collect(Collectors.toCollection(ArrayList::new));
            mileCost = miles.stream().filter(o -> Objects.equals(o.getParser(), owTrips.get(0).getFlights().get(0).getParser()))
                    .findFirst().get();
        }

       // ParserUtils.setMiles2Trip(rtTrips, mileCost);
        ParserUtils.setMiles2Trip(owTrips, mileCost);
        return owTrips;
    }

    private List<Trip> getTrips(List<Trip> trips, List<String> classes, Long queryId) {
        trips.stream().forEach(o -> {
            o.setQueryId(queryId);
            o.setFlights(ParserUtils.getFlightDur(o.getFlights()));
            //  o.setQueryId((long) (new Random().nextDouble() * 123L));
            o.setArriveCode(o.getFlights().get(o.getFlights().size() - 1).getArriveCode());
            o.setDepartCode(o.getFlights().get(0).getDepartCode());

            o.setArrivePlace(o.getFlights().get(o.getFlights().size() - 1).getArrivePlace());
            o.setDepartPlace(o.getFlights().get(0).getDepartPlace());

            o.setTripDate(o.getFlights().get(0).getDepartDate());
            o.setStops(ParserUtils.getStops(o.getFlights()));
            o.setCarriers(ParserUtils.getCarriers(o.getFlights()));
            o.setFlightLegs(ParserUtils.getFlightLegs(o.getFlights()));
            o.setFlightNumbers(ParserUtils.getFlightNumbers(o.getFlights()));

            //o.setCost(BigDecimal.valueOf(1000L + ((long) (new Random().nextDouble() * (2000L - 1000L)))));
            o.setUpdatedAt(new Date());
            o.setTripDuration(ParserUtils.getTripDuration(o));
            o.setCreatedAt(new Date());
            o.getFlights().forEach(k -> k.setTrip(o));
            Map<String, List<Flight>> map = ParserUtils.getLayovers(o.getFlights());
            if (map != null) {
                o.setLayovers(map.keySet().stream().findFirst().get());
                o.setFlights(map.values().stream().findFirst().get());
            } else {
                o.setLayovers("[\"00:00\"]");
            }


        });
        return ParserUtils.setCabin(trips, classes);
        //return trips;
    }

    public ComplexTrip getAC(DefaultHttpClient client, String date, String returnDate, String origin, String destination, int seats, Account account) throws UnsupportedEncodingException, IOException, IncorrectCredentials, ParseException, InterruptedException {

        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        httpclient.setCookieStore(new BasicCookieStore());
        httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
        httpclient.setRedirectStrategy(new LaxRedirectStrategy());

        for (Cookie item : client.getCookieStore().getCookies()) {

            httpclient.getCookieStore().addCookie(item);
        }

        setClientProxyProperties(httpclient, account);

        List<Trip> ow = new ArrayList<>();
        List<Trip> rt = new ArrayList<>();

        // ComplexAward result = new ComplexAward();
        ComplexTrip result = new ComplexTrip();
        ComplexTrip complexTrip = getAeroplan(httpclient, date, returnDate, origin, destination, seats, account);


        // ComplexAward complexAward = getAeroplan(httpclient, date, returnDate, origin, destination, seats, account);

        ExecutorService executor = Executors.newCachedThreadPool();

        Set<Callable<Trip>> callables = new HashSet<Callable<Trip>>();

        for (Trip trip : complexTrip.getOneWayList()) {

            callables.add(new DataThread(httpclient, trip, false, seats, account));
        }

        for (Trip trip : complexTrip.getReturnWayList()) {

            callables.add(new DataThread(httpclient, trip, true, seats, account));
        }

        List<Future<Trip>> futureList = executor.invokeAll(callables);

        for (Future<Trip> futureItem : futureList) {

            try {

                Trip trip = futureItem.get();

                if (trip.getDirection() == 0) {

                    result.getOneWayList().add(trip);

                } else {

                    result.getReturnWayList().add(trip);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        executor.shutdown();

        return result;
    }

    public class DataThread implements Callable<Trip> {

        private Trip trip;
        private DefaultHttpClient httpclient;
        private boolean oReturn;
        private int seats;
        private Account account;

        public DataThread(DefaultHttpClient httpclient, Trip trip, boolean oReturn, int seats, Account account) {

            this.httpclient = httpclient;
            this.trip = trip;
            this.oReturn = oReturn;
            this.seats = seats;
            this.account = account;
        }

        @Override
        public Trip call() throws Exception {
            List<ClasInfo> clasInfos = new ArrayList<>();
            for (ClasInfo clasInfo : trip.getClasInfo()) {
                if (clasInfo.getReduction() == "E") {
                    ClasInfo info = getTax(httpclient, clasInfo, oReturn, seats, account);
                    clasInfos.add(info);
                }
                if (clasInfo.getReduction() == "B") {
                    ClasInfo info = getTax(httpclient, clasInfo, oReturn, seats, account);
                    clasInfos.add(info);
                }
                if (clasInfo.getReduction() == "P") {
                    ClasInfo info = getTax(httpclient, clasInfo, oReturn, seats, account);
                    clasInfos.add(info);
                }
                if (clasInfo.getReduction() == "F") {
                    ClasInfo info = getTax(httpclient, clasInfo, oReturn, seats, account);
                    clasInfos.add(info);
                }
            }
            trip.setClasInfo(clasInfos);
            return trip;
        }

        public ClasInfo getTax(DefaultHttpClient client, ClasInfo info, boolean oReturn, int seats, Account account) throws IOException, InterruptedException {

            DefaultHttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
            httpclient.setCookieStore(new BasicCookieStore());
            httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
            httpclient.setRedirectStrategy(new LaxRedirectStrategy());

            for (Cookie item : client.getCookieStore().getCookies()) {

                httpclient.getCookieStore().addCookie(item);
            }

            setClientProxyProperties(httpclient, account);

            String taxFull = "";
            String currency = "";

            try {

                HttpResponse response = null;
                HttpEntity entity = null;

                String urlAirPrice = "https://www4.aeroplan.com/adr/AirPrice_Ajax.jsp?";


                if (oReturn) {

                    urlAirPrice = urlAirPrice + "product=classic"
                            + "&adults=" + seats
                            + "&child=0"
                            + "&seg0Component=1"
                            + "&seg0Product=0"
                            + "&seg0Option=" + info.getPosition()
                            + "&estimatedMiles=" + info.getMileage();

                } else {

                    urlAirPrice = urlAirPrice + "product=classic"
                            + "&adults=" + seats
                            + "&child=0"
                            + "&seg0Component=0"
                            + "&seg0Product=0"
                            + "&seg0Option=" + info.getPosition()
                            + "&estimatedMiles=" + info.getMileage();
                }

                HttpPost httpPost = new HttpPost(urlAirPrice);

                response = httpclient.execute(httpPost);
                entity = response.getEntity();

                String tax = ParserUtils.responseToString(entity.getContent());
                httpPost.abort();

                int tFirst = tax.indexOf("<span class=\"totalCash\">") + "<span class=\"totalCash\">".length();
                int tSecond = tax.indexOf("<br/>", tFirst);
                if (tFirst < 0 || tSecond < 0 || tFirst > tSecond) {
                    getTax(httpclient, info, oReturn, seats, account);
                }
                tax = tax.substring(tFirst, tSecond);

                String[] taxArray = tax.split("\\n");

                taxFull = taxArray[0].trim().substring(1);
                currency = taxArray[1].trim().replaceAll(" ", "");

                System.out.println(taxFull);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            info.setTax(taxFull);
            info.setCurrency(currency);

            //        Thread.sleep(10);
            return info;
        }
    }

    public ComplexTrip getAeroplan(DefaultHttpClient client, String date, String returnDate, String origin, String destination, int seats, Account account) throws UnsupportedEncodingException, IOException, IncorrectCredentials, ParseException {
        // добавить настройки прокси
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        httpclient.setCookieStore(new BasicCookieStore());
        httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
        httpclient.setRedirectStrategy(new LaxRedirectStrategy());

        for (Cookie item : client.getCookieStore().getCookies()) {

            httpclient.getCookieStore().addCookie(item);
        }

        setClientProxyProperties(httpclient, account);


        HttpResponse response = null;
        HttpEntity entity = null;

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        HttpGet httGet = new HttpGet("https://www4.aeroplan.com/adr/AirBooking.do?icid=SUB_M_Travel_EN");

        response = httpclient.execute(httGet);
        entity = response.getEntity();

        String data = ParserUtils.responseToString(entity.getContent());

        Document doc = Jsoup.parse(data);

        Elements rToken = doc.getElementsByAttributeValue("name", "rToken");

        String token = "";

        if (rToken.size() > 0) {

            token = rToken.get(0).attr("value");
        }
        System.out.println(token);

        HttpPost httget = new HttpPost("https://www4.aeroplan.com/adr/SearchProcess.do");

        //   nameValuePairs.clear();

        nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("rToken", token));
        nameValuePairs.add(new BasicNameValuePair("saveCookie", "true"));
        nameValuePairs.add(new BasicNameValuePair("currentTripTab", "return"));
        nameValuePairs.add(new BasicNameValuePair("modifySearch", "false"));
        nameValuePairs.add(new BasicNameValuePair("forceIkk", "false"));
        nameValuePairs.add(new BasicNameValuePair("savedSearch", "-"));
        nameValuePairs.add(new BasicNameValuePair("city1ToReturnCode", destination));
        nameValuePairs.add(new BasicNameValuePair("city1FromReturnCode", origin));
        nameValuePairs.add(new BasicNameValuePair("l1ReturnDate", date));
        nameValuePairs.add(new BasicNameValuePair("r1ReturnDate", returnDate));
        nameValuePairs.add(new BasicNameValuePair("ReturnFlexibleDatesHidden", "0"));
        nameValuePairs.add(new BasicNameValuePair("ReturnAdultsNb", seats + ""));
        nameValuePairs.add(new BasicNameValuePair("ReturnChildrenNb", "0"));
        nameValuePairs.add(new BasicNameValuePair("ReturnTotalPassengerNb", seats + ""));
        nameValuePairs.add(new BasicNameValuePair("ReturnCabin", "Economy"));

        httget.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        //  httpclient.execute(httget).close();
        response = httpclient.execute(httget);
        entity = response.getEntity();
        String entityContent = ParserUtils.responseToString(entity.getContent());
        httget.abort();

        httget = new HttpPost("https://www4.aeroplan.com/adr/Results_Ajax.jsp?searchType=return&forceIkk=false");

        response = httpclient.execute(httget);
        entity = response.getEntity();

        String html = ParserUtils.responseToString(entity.getContent());

        if (!isJSONValid(html)) {

            // ComplexTrip complexTrip = new ComplexTrip();
            return new ComplexTrip();

            // ComplexAward complexAward = new ComplexAward();

            // return complexAward;
        }

        JSONObject jsonObj = new JSONObject(html);

        if (jsonObj.isNull("NormalResults")) {

            return new ComplexTrip();
            // return new ComplexAward();
        }

        JSONArray products = jsonObj.getJSONObject("NormalResults").getJSONArray("product");

        JSONObject filters = jsonObj.getJSONObject("NormalResults").getJSONObject("filters");

        HashMap<String, String> airports = new HashMap<String, String>();

        Iterator<String> iter = filters.getJSONObject("airports").keys();

        while (iter.hasNext()) {

            String key = iter.next();
            String value = filters.getJSONObject("airports").getString(key);

            airports.put(key, value);

        }

        HashMap<String, Integer> rewarQuoteStar = new HashMap<String, Integer>();
        HashMap<String, Integer> directRewarQuote = new HashMap<String, Integer>();
        HashMap<String, Integer> connectRewarQuote = new HashMap<String, Integer>();

        Iterator<String> mileageIter = jsonObj.getJSONObject("RewardQuoteStar").keys();

        while (mileageIter.hasNext()) {

            String key = mileageIter.next();
            Integer value = jsonObj.getJSONObject("RewardQuoteStar").getInt(key);

            rewarQuoteStar.put(key, value);
        }

        JSONObject directReward = jsonObj.getJSONObject("RewardQuote").getJSONObject("Direct");

        Iterator<String> directIter = directReward.keys();

        while (directIter.hasNext()) {

            String key = directIter.next();
            Integer value = directReward.getInt(key);

            directRewarQuote.put(key, value);
        }

        JSONObject connectReward = jsonObj.getJSONObject("RewardQuote").getJSONObject("Connect");

        Iterator<String> connectIter = connectReward.keys();

        while (connectIter.hasNext()) {

            String key = connectIter.next();
            Integer value = connectReward.getInt(key);

            connectRewarQuote.put(key, value);
        }

        boolean oReturn = false;

        ComplexAward complexAward = new ComplexAward();
        ComplexTrip complexTrip = new ComplexTrip();

        for (int i = 0; i < 1; i++) {

            JSONObject product = products.getJSONObject(i);

            JSONArray tripComponents = product.getJSONArray("tripComponent");

            for (int j = 0; j < 2; j++) {

                if (oReturn) {

//                    break;
                }

                JSONObject tripComponent = tripComponents.getJSONObject(j);

                if (!tripComponent.has("ODoption")) {

                    continue;
                }

                JSONArray ODoptions = tripComponent.getJSONArray("ODoption");

                if (j == 1) {

                    oReturn = true;
                }

                for (int k = 0; k < ODoptions.length(); k++) {

                    JSONObject ODoption = ODoptions.getJSONObject(k);

                    //position
                    String position = ODoption.getInt("position") + "";

                    String awardCabin = ODoption.getString("cabin");

                    String flightClass = ODoption.getString("class");

                    if (flightClass.contains("W")) {

                        flightClass = flightClass.replaceAll("W", "X");

                    }

                    if (flightClass.contains("D")) {

                        flightClass = flightClass.replaceAll("D", "I");
                    }

                    int max = 0;

                    String totalDuration = ODoption.getString("totalDuration");

                    // Award award = new Award();
                    Trip trip = new Trip();

                    List<Flight> flights = new LinkedList<>();
                    List<String> connections = new LinkedList<>();

                    JSONArray segments = ODoption.getJSONArray("segment");

                    String groupArray = "";

                    boolean add = true;

                    for (int l = 0; l < segments.length(); l++) {

                        JSONObject segment = segments.getJSONObject(l);

                        Flight flight = new Flight();

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                        Date fullStart = formatter.parse(segment.getString("departureDateTime"));
                        Date fullEnd = formatter.parse(segment.getString("arrivalDateTime"));

                        String aircraft = segment.getString("aircraft");
                        String arriveDate = segment.getString("arrivalDateTime").split("T")[0];
                        String arriveCode = segment.getString("destination");
                        String carrierCode = segment.getString("airline");

                        String arrivePlace = airports.get(segment.getString("destination"));
                        System.out.println(arrivePlace);
                        String arriveTime = segment.getString("arrivalDateTime").split("T")[1];
                        String departDate = segment.getString("departureDateTime").split("T")[0];
                        String departCode = segment.getString("origin");
                        String departPlace = airports.get(segment.getString("origin"));

                        System.out.println(departPlace);
                        String departTime = segment.getString("departureDateTime").split("T")[1];
                        String flightN = segment.getString("flightNo");
                        String meal = segment.getString("meal");
                        String travelTime = segment.getString("duration");

                        String nextConnection = segment.getString("nextConnection");
                        String flightCabin = segment.getString("cabin");
                        groupArray = groupArray + segment.getString("group");

                        flight.setArriveDate(dateFormat.parse(arriveDate));
                        flight.setDepartDate(dateFormat.parse(departDate));
                        flight.setFullStartDate(fullStart);
                        flight.setFullEndDate(fullEnd);
                        flight.setParser("AC");
                        flight.setPosition(l);
                        flight.setCarrierName(carrierCode);
                        flight.setCarrierCode(carrierCode);
                        flight.setLayover("[]");
                        flight.setAircraft(aircraft);
                        // flight.setArriveDate(arriveDate);
                        flight.setArrivePlace(arrivePlace);
                        flight.setArriveCode(arriveCode);
                        flight.setArriveTime(arriveTime.substring(0, 5));
                        // flight.setDepartDate(departDate);
                        flight.setDepartPlace(departPlace);
                        flight.setDepartCode(departCode);
                        flight.setDepartTime(departTime.substring(0, 5));
                        flight.setFlightNumber(flightN);
                        //flight.setMeal(meal);
                        flight.setFlightDuration(travelTime);
                        flight.setCreatedAt(new Date());
                        flight.setUpdatedAt(new Date());

                        if (flightCabin.equals(ECONOMY)) {

                            flight.setCabin(ECONOMY);

                        } else if (flightCabin.equals(BUSINESS)) {

                            flight.setCabin(BUSINESS);

                        } else if (flightCabin.equals(FIRST)) {

                            flight.setCabin(FIRST);

                        } else if (flightCabin.equals("P")) {

                            flight.setCabin(PREMIUM_ECONOMY);
                        }

                        if (!"0h 00 min".equals(nextConnection)) {

                            connections.add(nextConnection);
                        }

                        //airline
                        flights.add(flight);
                    }

                    int mileage = ODoption.getInt("mileage");

                    if (mileage > 0) {

                        max = mileage;

                    } else {

                        for (char item : flightClass.toCharArray()) {

                            if (rewarQuoteStar.containsKey(item + "")) {

                                int priceItem = rewarQuoteStar.get(item + "");

                                if (priceItem > max) {

                                    max = priceItem;
                                }
                            }

                        }

                        if (max <= 0) {

                            if (flights.size() > 1) {

                                for (char item : flightClass.toCharArray()) {

                                    if (connectRewarQuote.containsKey(item + "")) {

                                        int priceItem = connectRewarQuote.get(item + "");

                                        if (priceItem > max) {

                                            max = priceItem;
                                        }
                                    }

                                }

                            } else {

                                for (char item : flightClass.toCharArray()) {

                                    if (directRewarQuote.containsKey(item + "")) {

                                        int priceItem = directRewarQuote.get(item + "");

                                        if (priceItem > max) {

                                            max = priceItem;
                                        }
                                    }

                                }

                            }

                        }

                    }

                    List<ClasInfo> clasInfos = new ArrayList<>();
                    ClasInfo info = new ClasInfo();
                   /* Info info = new Info();*/
                    info.setMileage((seats * max) / 2 + "");
                    info.setNa(false);
                    info.setStatus(1);

//                    info.setTax(taxFull);
//                    info.setCurrency(currency);
                    info.setPosition(position);

                    if (awardCabin.equals(ECONOMY)) {
                        info.setReduction("E");
                        info.setName("Economy");
                        clasInfos.add(info);
                        //award.setEconomy(info);
                        System.out.println("economy");

                    } else if (awardCabin.equals(BUSINESS)) {
                        info.setReduction("B");
                        info.setName("Business");
                        clasInfos.add(info);
                        // award.setBusiness(info);
                        System.out.println("business");

                    } else if (awardCabin.equals(FIRST)) {
                        info.setReduction("F");
                        info.setName("First");
                        clasInfos.add(info);
                        System.out.println("first");
                        // award.setFirst(info);

                    } else if (awardCabin.equals(PREMIUM_ECONOMY)) {
                        System.out.println("premec");
                        info.setReduction("P");
                        info.setName("Premium economy");
                        clasInfos.add(info);
                        // award.setPremiumEconomy(info);

                    } else if (awardCabin.equals(ECONOMY_BUSINESS)) {
                        System.out.println("ecbus");
                        info.setReduction("B");
                        info.setName("Business");
                        clasInfos.add(info);
                        // award.setBusiness(info);

                    } else if (awardCabin.equals(BUSINESS_FIRST)) {
                        System.out.println("busfirst");
                        info.setReduction("F");
                        info.setName("First");
                        clasInfos.add(info);
                        // award.setFirst(info);

                    } else if (awardCabin.contains(FIRST)) {
                        System.out.println("first");
                        info.setReduction("F");
                        info.setName("First");
                        clasInfos.add(info);
                        // award.setFirst(info);

                    } else if (awardCabin.contains(BUSINESS)) {
                        System.out.println("bus");
                        info.setReduction("B");
                        info.setName("Business");
                        clasInfos.add(info);

                        // award.setBusiness(info);

                    } else if (awardCabin.contains(ECONOMY) || awardCabin.contains("M")) {
                        System.out.println("econ");
                        info.setReduction("E");
                        info.setName("Economy");
                        clasInfos.add(info);
                        //award.setEconomy(info);
                    }

                    trip.setClasInfo(clasInfos);
                    trip.setTripDuration(ParserUtils.getTotalTime(totalDuration, this));

                    // award.setConnections(connections);
                    trip.setFlights(flights);

                    if (add) {

                        if (oReturn) {

                            trip.setDirection(1);
                            //award.setDirection(Award.RETURN);
                            System.out.println("award added");
                            complexTrip.getReturnWayList().add(trip);
                            //complexAward.getReturnAward().add(award);

                        } else {
                            System.out.println("award added");

                            trip.setDirection(0);
                            complexTrip.getOneWayList().add(trip);
                            // award.setDirection(Award.DEPARTURE);
                            //complexAward.getOnewayList().add(award);
                        }
                    }
                }
            }
        }
        return complexTrip;
    }

    public boolean isJSONValid(String test) {
        try {

            new JSONObject(test);

        } catch (JSONException ex) {

            try {

                new JSONArray(test);

            } catch (JSONException ex1) {

                return false;
            }
        }

        return true;
    }

    private void setClientProxyProperties(DefaultHttpClient httpclient, Account account) throws IOException {
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

    private DefaultHttpClient login(String loginS1, String loginS2, String loginS3, String password, Account account) throws UnsupportedEncodingException, IOException, IncorrectCredentials, ParseException, MaintenanceException {

        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        httpclient.setCookieStore(new BasicCookieStore());
        httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
        httpclient.setRedirectStrategy(new LaxRedirectStrategy());

        HttpResponse response = null;
        HttpEntity entity = null;

        setClientProxyProperties(httpclient, account);

        System.out.println("127.0.0.1 " + 8081);

        HttpGet httGet = new HttpGet("https://www4.aeroplan.com/home.do");

        response = httpclient.execute(httGet);
        entity = response.getEntity();

        String main = ParserUtils.responseToString(entity.getContent());

        if (main.contains("Sorry")) {

            throw new MaintenanceException();
        }

        HttpPost httget = new HttpPost("https://www4.aeroplan.com/log_in.do");

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        String cust = loginS1 + loginS2 + loginS3;

        nameValuePairs.add(new BasicNameValuePair("cust", cust.length() > 9 ? cust.substring(0, 9) : cust));
        nameValuePairs.add(new BasicNameValuePair("pin", password.length() > 10 ? password.substring(0, 10) : password));
        nameValuePairs.add(new BasicNameValuePair("process", "true"));
        nameValuePairs.add(new BasicNameValuePair("loginFromWrapper", "true"));

        httget.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        response = httpclient.execute(httget);
        entity = response.getEntity();

        String loginStatus = ParserUtils.responseToString(entity.getContent());
        ;

        //LOG INTO YOUR AEROPLAN ACCOUNT
        if (response.getStatusLine().getStatusCode() != 200 || loginStatus.contains("LOG INTO YOUR AEROPLAN ACCOUNT")) {

            return null;
        }

        return httpclient;
    }
}
