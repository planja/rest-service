package com.guru.parser.dl;

import com.guru.domain.model.Airport;
import com.guru.domain.model.Flight;
import com.guru.domain.model.MileCost;
import com.guru.domain.model.Trip;
import com.guru.domain.repository.AirportRepository;
import com.guru.domain.repository.MileCostRepository;
import com.guru.parser.interf.Parser;
import com.guru.parser.utils.ParserUtils;
import com.guru.vo.temp.ProxyUtils;
import com.guru.vo.temp.exceptions.IncorrectCredentials;
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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by Anton on 02.05.2016.
 */

@Component

public class DLParser implements Parser {

    private static final String PARSER_CODE = "DL";
    @Inject
    private MileCostRepository mileCostRepository;
    @Inject
    private AirportRepository airportRepository;
    private HashMap<String, String> databaseCities;

    /**
     * gets list of slices
     *
     * @param resultMap
     * @param slices
     * @return
     * @throws FileNotFoundException
     */
    private static List<String> getSliceList(HashMap<String, String> resultMap, String slices) throws FileNotFoundException {

        List<String> sliceList = new LinkedList<>();

        int i = 0;

        do {

            if (resultMap.containsKey(slices + "[" + i + "]")) {
                sliceList.add(resultMap.get(slices + "[" + i + "]"));
            }

            i++;

        } while (resultMap.containsKey(slices + "[" + i + "]"));

        return sliceList;
    }

    /**
     * finds a single fare
     *
     * @param resultMap
     * @param trip
     * @param fare
     * @throws FileNotFoundException
     */
    private static List<Trip> getFare(HashMap<String, String> resultMap, Trip trip, String fare) throws FileNotFoundException {

        List<Trip> trips = new ArrayList<Trip>();

        int i = 0;


        do {

            if (resultMap.containsKey(fare + "[" + i + "]")) {

                List<Trip> list = getFareItem(resultMap, trip, resultMap.get(fare + "[" + i + "]").replaceAll("\"", ""), i);
                for (Trip newTrip : list) {
                    trips.add(newTrip);
                }
            }

            i++;

        } while (resultMap.containsKey(fare + "[" + i + "]"));

        return trips;
    }

    /**
     * finds a single fare
     *
     * @param resultMap
     * @param trip
     * @param fareItem
     * @param i
     * @throws FileNotFoundException
     */
    private static List<Trip> getFareItem(HashMap<String, String> resultMap, Trip trip, String fareItem, int i) throws FileNotFoundException {

        List<Trip> trips = new ArrayList<Trip>();
        int j = 0;

        String found = resultMap.get(fareItem + ".baseAwardMiles");

        if (!found.contains("null")) {

            String miscFlightInfos = resultMap.get(fareItem + ".miscFlightInfos");

            boolean first = false;

            do {

                if (resultMap.containsKey(miscFlightInfos + "[" + j + "]")) {


                    String miscFlightInfo = resultMap.get(miscFlightInfos + "[" + j + "]");

                    String bookingCode = resultMap.get(miscFlightInfo + ".bookingCode").replaceAll("\"", "");
                    String cabinName = resultMap.get(miscFlightInfo + ".cabinName").replaceAll("\"", "");

                    String awardName = "";

                    first = false;

                    if (bookingCode.contains("N") || bookingCode.contains("X") || bookingCode.contains("T") || bookingCode.contains("U") || bookingCode.contains("W")) {
//полностью Economy???
                        trip.getFlights().get(j).setCabin("E");

                        //  trip.getMixedCabins().add("Economy");

                  /*      if (info.getMixedCabins().contains("Business") || info.getMixedCabins().contains("First")) {
                            info.setMixed(true);
                        }*/
                    }

                    if (bookingCode.contains("O") || bookingCode.contains("Z") || bookingCode.contains("G")) {

                        trip.getFlights().get(j).setCabin("B");
                    /*    info.getMixedCabins().add("Business");
                        if (info.getMixedCabins().contains("Economy") || info.getMixedCabins().contains("First")) {
                            info.setMixed(true);
                        }*/
                    }

                    if (bookingCode.contains("R") || bookingCode.contains("A")) {

                        trip.getFlights().get(j).setCabin("B");
                      /*  info.getMixedCabins().add("First");
                        if (info.getMixedCabins().contains("Business") || info.getMixedCabins().contains("Economy")) {
                            info.setMixed(true);
                        }*/
                        first = true;
                    }
                }


                j++;

            }
            while (resultMap.containsKey(miscFlightInfos + "[" + j + "]"));


            String cabin = resultMap.get(fareItem + ".cabinName").replaceAll("\"", "");

            if (first) {

                Trip newTrip = new Trip(trip);
                for (Flight flight : newTrip.getFlights()) {
                    flight.setTrip(newTrip);
                }

                newTrip.getFlights().get(0).setCabin(BUSINESS);
                newTrip.setCreatedAt(new Date());
                newTrip.setUpdatedAt(new Date());
                newTrip.setClas(BUSINESS);
                trips.add(newTrip);

            } else if (cabin.contains("Economy") || cabin.contains("Main")) {

                Trip newTrip = new Trip(trip);
                for (Flight flight : newTrip.getFlights()) {
                    flight.setTrip(newTrip);
                }
                newTrip.setCreatedAt(new Date());
                newTrip.setUpdatedAt(new Date());
                newTrip.getFlights().get(0).setCabin(ECONOMY);
                newTrip.setClas(ECONOMY);
                trips.add(newTrip);

            } else if (cabin.contains("Business")) {

                Trip newTrip = new Trip(trip);
                for (Flight flight : newTrip.getFlights()) {
                    flight.setTrip(newTrip);
                }
                newTrip.setCreatedAt(new Date());
                newTrip.setUpdatedAt(new Date());
                newTrip.getFlights().get(0).setCabin(BUSINESS);
                newTrip.setClas(BUSINESS);
                trips.add(newTrip);

            } else if (cabin.contains("One")) {

                Trip newTrip = new Trip(trip);
                for (Flight flight : newTrip.getFlights()) {
                    flight.setTrip(newTrip);
                }
                newTrip.setCreatedAt(new Date());
                newTrip.setUpdatedAt(new Date());
                newTrip.getFlights().get(0).setCabin(BUSINESS);
                newTrip.setClas(BUSINESS);
                trips.add(newTrip);
            }


            cabin = resultMap.get(fareItem + ".cabinName").replaceAll("\"", "");

            String miles = resultMap.get(fareItem + ".totalAwardMiles").replaceAll("\"", "");
            miles = miles.replaceAll(",", "");

            String priceLeft = resultMap.get(fareItem + ".totalPriceLeft").replaceAll("\"", "");

            String priceRight = resultMap.get(fareItem + ".totalPriceRight").replaceAll("\"", "");


            String currencyCode = resultMap.get(fareItem + ".currencyCode").replaceAll("\"", "");

            String tax = priceLeft + priceRight;
            for (Trip newTrip : trips) {
                newTrip.setTax(new BigDecimal(tax));

                newTrip.setMiles(new Integer(miles));
            }


        }

        return trips;
    }

    /**
     * gets origin destination code
     *
     * @param resultMap
     * @param dest
     * @return
     * @throws FileNotFoundException
     */
    private static String getOriginDestinationCode(HashMap<String, String> resultMap, String dest) throws FileNotFoundException {

        String airportCode = resultMap.get(dest + ".airportCode").replaceAll("\"", "");

        String airportName = resultMap.get(dest + ".airportName").replaceAll("\"", "");

        return airportCode;
    }

    /**
     * gets origin destination name
     *
     * @param resultMap
     * @param dest
     * @return
     * @throws FileNotFoundException
     */
    private static String getOriginDestinationName(HashMap<String, String> resultMap, String dest) throws FileNotFoundException {

        String airportCode = resultMap.get(dest + ".airportCode").replaceAll("\"", "");

        String airportName = resultMap.get(dest + ".airportName").replaceAll("\"", "");

        return airportName;
    }

    /**
     * gets layovers for the whole trip
     *
     * @param resultMap
     * @param trip
     * @param layover
     * @return
     * @throws FileNotFoundException
     */
    private static String getLayover(HashMap<String, String> resultMap, Trip trip, String layover) throws FileNotFoundException {

        if (resultMap.containsKey(layover + "[0]")) {

            String layoverId = resultMap.get(layover + "[0]").replaceAll("\"", "");

            String duration = resultMap.get(layoverId + ".duration").replaceAll("\"", "").trim();
            StringBuilder sb = new StringBuilder();
            try {
                sb = new StringBuilder(trip.getLayovers());
            } catch (Exception e) {
            }
            if (sb.length() == 0)
                sb.append("[");
            try {
                duration = ParserUtils.getTotalTime(duration, new DLParser());
                sb.append("\"" + duration + "\",");
            } catch (ParseException e) {
            }
//сделать json
            trip.setLayovers(sb.toString());
            return duration;
        } else {
            return "00:00";
        }
    }

    /**
     * sets some info about the flight
     *
     * @param resultMap
     * @param flight
     * @param leg
     * @throws FileNotFoundException
     */
    private static void getLegs(HashMap<String, String> resultMap, Flight flight, String leg) throws FileNotFoundException {

        String legItem = resultMap.get(leg + "[0]").replaceAll("\"", "");

        String aircraft = getAircraft(resultMap, resultMap.get(legItem + ".aircraft").replaceAll("\"", ""));

        String layoverInfoList = resultMap.get(legItem + ".layoverInfos").replaceAll("\"", "");

        int i = 0;
        while (resultMap.containsKey(layoverInfoList + "[" + i + "]")) {

            String stop = resultMap.get(layoverInfoList + "[" + i + "]").replaceAll("\"", "");

            String arpStop = resultMap.get(stop + ".equipChgAptCode").replaceAll("\"", "");
            String duration = resultMap.get(stop + ".duration").replaceAll("\"", "");
//сделать тут layovers

            //     s.setAirport(arpStop);


            i++;
        }

        String duration = resultMap.get(legItem + ".duration").replaceAll("\"", "");

        String fNumber = getFNumber(resultMap, resultMap.get(legItem + ".operatingAirline").replaceAll("\"", ""));

        flight.setAircraft(aircraft);
        try {
            flight.setFlightDuration(ParserUtils.getTotalTime(duration, new DLParser()));
        } catch (ParseException e) {
        }
        flight.setFlightNumber(fNumber);
        flight.setCarrierCode(fNumber.split(" ")[0]);
        flight.setCarrierName(fNumber.split(" ")[0]);

    }

    /**
     * gets flight number
     *
     * @param resultMap
     * @param operAir
     * @return
     * @throws FileNotFoundException
     */
    private static String getFNumber(HashMap<String, String> resultMap, String operAir) throws FileNotFoundException {

        String airline = resultMap.get(operAir + ".airline").replaceAll("\"", "");

        String number = resultMap.get(operAir + ".flightNbr").replaceAll("\"", "");

        String code = resultMap.get(airline + ".airlineCode").replaceAll("\"", "");

        return code + " " + number;
    }

    /**
     * @param resultMap
     * @param aircraft
     * @return
     * @throws FileNotFoundException
     */
    private static String getAircraft(HashMap<String, String> resultMap, String aircraft) throws FileNotFoundException {

        return resultMap.get(aircraft + ".shortName").replaceAll("\"", "");
    }

    private static void setProxyInfo(DefaultHttpClient httpclient, String credent, String ipport) throws IOException {
        HttpResponse response = null;
        HttpEntity entity = null;

        HttpHost proxy = new HttpHost(ipport.split(":")[0], Integer.parseInt(ipport.split(":")[1]));
        Credentials credentials = new UsernamePasswordCredentials(credent.split(":")[0], credent.split(":")[1]);
        AuthScope authScope = new AuthScope(ipport.split(":")[0], Integer.parseInt(ipport.split(":")[1]));
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(authScope, credentials);
        httpclient.setCredentialsProvider(credsProvider);
        httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
    }

    /**
     * returns number of pages with result, inside does all the job for getting the result via getTrip() function call
     *
     * @param httpclient
     * @param httpPost
     * @param session
     * @param scriptSessionId
     * @param currentSessionCheckSum
     * @param response
     * @param entity
     * @param tripList
     * @param cacheKey
     * @param index
     * @param resum
     * @param requestId
     * @param cities
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ParseException
     */
    private String getResultList(DefaultHttpClient httpclient, HttpPost httpPost, String session, String scriptSessionId, String currentSessionCheckSum, HttpResponse response, HttpEntity entity, List<Trip> tripList, String cacheKey, int index, boolean resum, int requestId, List<Airport> cities) throws IOException, FileNotFoundException, ParseException {

        String url = "";
        String dwrInfo = "";

        if (resum) {

            url = "https://www.delta.com/air-shopping/dwr/call/plaincall/ResummarizeFlightResultsDWR.pageResults.dwr";

            dwrInfo = "callCount=1\n"
                    + "page=/air-shopping/findFlights.action\n"
                    + "httpSessionId=" + session + "\n"
                    + "scriptSessionId=" + scriptSessionId + "\n"
                    + "c0-scriptName=ResummarizeFlightResultsDWR\n"
                    + "c0-methodName=pageResults\n"
                    + "c0-id=0\n"
                    + "c0-param0=string:" + index + "\n"
                    + "c0-param1=string:" + currentSessionCheckSum + "\n"
                    + "c0-param2=string:" + cacheKey + "\n"
                    + "c0-param3=boolean:false\n"
                    + "c0-param4=boolean:false\n"
                    + "batchId=" + index + "";

        } else {

            url = "https://www.delta.com/air-shopping/dwr/call/plaincall/SearchFlightResultsDWR.searchResults.dwr";

            dwrInfo = "callCount=1\n"
                    + "page=/air-shopping/findFlights.action\n"
                    + "httpSessionId=" + session + "\n"
                    + "scriptSessionId=" + scriptSessionId + "\n"
                    + "c0-scriptName=SearchFlightResultsDWR\n"
                    + "c0-methodName=searchResults\n"
                    + "c0-id=0\n"
                    + "c0-param0=string:" + currentSessionCheckSum + "\n"
                    + "c0-e1=string:deltaScheduleAward\n"
                    + "c0-e2=boolean:false\n"
                    + "c0-param1=Array:[reference:c0-e1,reference:c0-e2]\n"
                    + "c0-param2=string:null\n"
                    + "c0-param3=string:" + cacheKey + "\n"
                    + "c0-param4=boolean:false\n"
                    + "c0-param5=boolean:false\n"
                    + "batchId=0";

        }

        httpPost = new HttpPost(url);

        httpPost.addHeader("Content-Type", "text/plain; charset=UTF-8");
        httpPost.addHeader("Referer", "https://www.delta.com/air-shopping/findFlights.action");
        httpPost.addHeader("Accept-Encoding", "gzip, deflate, Izma");
        httpPost.addHeader("Accept", "*/*");

        StringEntity stringEntity = new StringEntity(dwrInfo, "UTF-8");

        httpPost.setEntity(stringEntity);

        response = httpclient.execute(httpPost);
        entity = response.getEntity();

        String data = ParserUtils.gzipResponseToString(entity.getContent());

        HashMap<String, String> resultMap = new HashMap<>();

        String[] data1 = data.split(";");

        for (String data1Item : data1) {

            if (data1Item.contains("=")) {

                String[] keyvalue = data1Item.split("=");
                resultMap.put(keyvalue[0], keyvalue[1]);

            }
        }

        Scanner s = new Scanner(data);

        if (data.contains("No flight options were found for this trip")) {

            return "0";
        }


        MatchResult result = null;

        try {

            s.findInLine("currentPageIndex:\"(\\d+)\"");
            result = s.match();

        } catch (IllegalStateException ex) {
            return "0";
        }


        String currentPageIndex = result.group(1);

        s.findInLine("fareTypesToDisplay:([a-z]\\d+)");
        result = s.match();

        String fareTypesToDisplay = result.group(1);

        s.findInLine("itineraries:([a-z]\\d+)");
        result = s.match();

        String itineraries = result.group(1);

        s.findInLine("numberOfPages:\"(\\d+)\"");
        result = s.match();

        String numberOfPages = result.group(1);

        s.findInLine("resultsPerPage:\"(\\d+)\"");
        result = s.match();

        int resultsPerPage = Integer.parseInt(result.group(1));

        s.close();

        for (int i = 0; i < resultsPerPage; i++) {

            String value = resultMap.get(itineraries + "[" + i + "]");

            tripList.addAll(getTrip(resultMap, value, requestId, cities));
        }
        return numberOfPages;
    }

    /**
     * finds a single Trip
     *
     * @param resultMap
     * @param itin
     * @param requestId
     * @param cities
     * @return
     * @throws FileNotFoundException
     * @throws ParseException
     */
    private List<Trip> getTrip(HashMap<String, String> resultMap, String itin, int requestId, List<Airport> cities) throws FileNotFoundException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMMMM yyyy", Locale.US);
        Trip trip = new Trip();
        trip.setQueryId((long) requestId);

        String slices = resultMap.get(itin + ".slices").replaceAll("\"", "");

        String totalFare = resultMap.get(itin + ".totalFare").replaceAll("\"", "");
        String tripDate = resultMap.get(slices + "[0]");
        tripDate = resultMap.get(tripDate + ".sliceDepartureDateOutbound").replaceAll("\"", "");
        trip.setTripDate(sdf.parse(tripDate));

        List<String> sliceList = getSliceList(resultMap, slices);

        for (String item : sliceList) {

            getSlice(resultMap, trip, item, cities);
        }

        List<Trip> trips = getFare(resultMap, trip, totalFare);
        for (Trip newTrip : trips) {
            StringBuilder cabins = new StringBuilder("[");
            for (Flight flight : newTrip.getFlights()) {
                cabins.append("\"" + flight.getCabin() + "\",");
            }
            cabins.deleteCharAt(cabins.length() - 1);
            cabins.append("]");
            newTrip.setCabins(cabins.toString());
            StringBuilder layovers = new StringBuilder();
            try {
                layovers = new StringBuilder(newTrip.getLayovers());
            } catch (Exception e) {
            }
            if (layovers.length() > 1) {
                layovers.deleteCharAt(layovers.length() - 1);
                layovers.append("]");

                newTrip.setLayovers(layovers.toString());
            } else {
                layovers = new StringBuilder("[]");
                newTrip.setLayovers(layovers.toString());
            }
            newTrip.setTripDate(newTrip.getFlights().get(0).getDepartDate());
            if (newTrip.getFlights().size() > 1) {
                StringBuilder stops = new StringBuilder("[");
                String tempLayovers = newTrip.getLayovers().replace("[", "").replace("]", "");
                String[] arrLayovers = tempLayovers.split(",");
                for (int i = 1; i < newTrip.getFlights().size(); i++) {
                    stops.append("\"" + newTrip.getFlights().get(i).getDepartCode() + "\",");
                    newTrip.getFlights().get(i).setLayover(arrLayovers[i - 1].replaceAll("\"", ""));
                }
                stops.deleteCharAt(stops.length() - 1);
                stops.append("]");
                newTrip.setStops(stops.toString());
            } else {
                newTrip.setStops("[]");
            }
            StringBuilder carriers = new StringBuilder("[");
            StringBuilder flightLegs = new StringBuilder("[");
            StringBuilder flightNumbers = new StringBuilder("[");


            for (Flight flight : newTrip.getFlights()) {
                carriers.append("\"" + flight.getCarrierCode() + "\",");
                flightLegs.append("\"" + flight.getFlightDuration() + "\",");
                flightNumbers.append("\"" + flight.getFlightNumber() + "\",");

            }
            carriers.deleteCharAt(carriers.length() - 1);
            carriers.append("]");
            flightLegs.deleteCharAt(flightLegs.length() - 1);
            flightLegs.append("]");
            flightNumbers.deleteCharAt(flightNumbers.length() - 1);
            flightNumbers.append("]");

            newTrip.getFlights().get(0).setLayover("00:00");
            Flight firstFlight = newTrip.getFlights().get(0);
            Flight lastFlight = newTrip.getFlights().get(newTrip.getFlights().size() - 1);
            newTrip.setDepartCode(firstFlight.getDepartCode());
            newTrip.setArriveCode(lastFlight.getArriveCode());
            newTrip.setArrivePlace(lastFlight.getArrivePlace());
            newTrip.setDepartPlace(firstFlight.getDepartPlace());
            newTrip.setCreatedAt(new Date());
            newTrip.setUpdatedAt(new Date());
            newTrip.setCarriers(carriers.toString());
            newTrip.setFlightLegs(flightLegs.toString());
            newTrip.setFlightNumbers(flightNumbers.toString());
        }
        return trips;
    }

    /**
     * gets a single slice
     *
     * @param resultMap
     * @param trip
     * @param slice
     * @param cities
     * @throws FileNotFoundException
     * @throws ParseException
     */
    private void getSlice(HashMap<String, String> resultMap, Trip trip, String slice, List<Airport> cities) throws FileNotFoundException, ParseException {

        String duration = resultMap.get(slice + ".duration").replaceAll("\"", "");

        if (!duration.contains("m")) {

            duration = duration + " 0m";
        }

        trip.setTripDuration(ParserUtils.getTotalTime(duration, new DLParser()));

        String flights = resultMap.get(slice + ".flights").replaceAll("\"", "");

        int i = 0;

        do {

            if (resultMap.containsKey(flights + "[" + i + "]")) {
                Flight flight = getFlight(resultMap, trip, resultMap.get(flights + "[" + i + "]").replaceAll("\"", ""), cities);
                if (flight != null)
                    flight.setPosition(trip.getFlights().size());
                flight.setTrip(trip);
                trip.getFlights().add(flight);
            }

            i++;

        } while (resultMap.containsKey(flights + "[" + i + "]"));
    }

    /**
     * finds a single flight
     *
     * @param resultMap
     * @param trip
     * @param flight
     * @param cities
     * @return
     * @throws FileNotFoundException
     */
    private Flight getFlight(HashMap<String, String> resultMap, Trip trip, String flight, List<Airport> cities) throws FileNotFoundException {

        Flight flightResult = new Flight();
        String year = new SimpleDateFormat("yyyy").format(trip.getTripDate());
        String flightArrivalTime = resultMap.get(flight + ".flightArrivalTime").replaceAll("\"", "");
        String flightArrivalDate = resultMap.get(flight + ".flightArrivalDate").replaceAll("\"", "") + " " + year + " " + flightArrivalTime;
        String flightDepartureTime = resultMap.get(flight + ".flightDepartureTime").replaceAll("\"", "");
        String flightDepartureDate = resultMap.get(flight + ".flightDepartureDate").replaceAll("\"", "") + " " + year + " " + flightDepartureTime;
        String flightDestinationCode = getOriginDestinationCode(resultMap, resultMap.get(flight + ".flightDestination").replaceAll("\"", ""));
        String flightDestinationName = getOriginDestinationName(resultMap, resultMap.get(flight + ".flightDestination").replaceAll("\"", ""));

        String flightOriginCode = getOriginDestinationCode(resultMap, resultMap.get(flight + ".flightOrigin").replaceAll("\"", ""));
        String flightOriginName = getOriginDestinationName(resultMap, resultMap.get(flight + ".flightOrigin").replaceAll("\"", ""));

        String layoverInfos = resultMap.get(flight + ".layoverInfos").replaceAll("\"", "");

        String layover = getLayover(resultMap, trip, layoverInfos);
        String legs = resultMap.get(flight + ".legs").replaceAll("\"", "");

        getLegs(resultMap, flightResult, legs);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy h:mma", Locale.US);
        SimpleDateFormat sdf1 = new SimpleDateFormat("h:mma");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

        try {
            flightResult.setArriveDate(sdf.parse(flightArrivalDate));
            flightResult.setDepartPlace(flightOriginName);
            flightResult.setArrivePlace(flightDestinationName);

            flightResult.setArrivePlace(flightDestinationName);
            flightResult.setArriveCode(flightDestinationCode);
            flightResult.setDepartCode(flightOriginCode);

            //    flightResult.setArriveAirport(flightDestinationCode);
            String arriveTime = sdf2.format(sdf1.parse(flightArrivalTime));
            flightResult.setArriveTime(arriveTime);
            flightResult.setDepartDate(sdf.parse(flightDepartureDate));
            flightResult.setDepartPlace(flightOriginName);

            setFlightCitiesByCodes(cities, flightResult);
            //     flightResult.setDepartAirport(flightOriginCode);
            String departTime = sdf2.format(sdf1.parse(flightDepartureTime));
            flightResult.setDepartTime(departTime);
            flightResult.setCabin("");
            flightResult.setCreatedAt(new Date());
            flightResult.setUpdatedAt(new Date());
            flightResult.setParser(PARSER_CODE);

            //     flightResult.setMeal("");

        } catch (ParseException e) {
            System.out.println("ParseEx");
            return null;
        }
        return flightResult;
    }

    /**
     * does all the job, calls all the functions for one day
     *
     * @param origin
     * @param destination
     * @param date
     * @param seats
     * @param requestId
     * @param cities
     * @return
     * @throws IOException
     * @throws IncorrectCredentials
     * @throws FileNotFoundException
     * @throws ParseException
     */
    public List<Trip> getDelta(String origin, String destination, String date, int seats, int requestId, List<Airport> cities) throws IOException, IncorrectCredentials, FileNotFoundException, ParseException {

        //12/6/2014
        List<Trip> awardList = new LinkedList<>();

        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        httpclient.setCookieStore(new BasicCookieStore());
        httpclient.setRedirectStrategy(new LaxRedirectStrategy());
        httpclient.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
        httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");

        HttpResponse response = null;
        HttpEntity entity = null;
        HttpGet httpGet;
        boolean isActive = false;
        while (!isActive) {
            String proxyInfo = ProxyUtils.getProxy("DL");
            System.out.println(proxyInfo);
            String credent = proxyInfo.split("@")[0];
            String ipport = proxyInfo.split("@")[1];
            setProxyInfo(httpclient, credent, ipport);
            httpGet = new HttpGet("https://www.delta.com/");
            response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                isActive = true;
                ProxyUtils.markProxyAs(proxyInfo, PARSER_CODE, true);
            } else
                ProxyUtils.markProxyAs(proxyInfo, PARSER_CODE, false);
        }
        entity = response.getEntity();


        String search = ParserUtils.responseToString(entity.getContent());

        Document searchForm = Jsoup.parse(search);

        Element flightSearchForm = searchForm.getElementById("flightSearchForm");
        String url = flightSearchForm.attr("action");

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        Elements inputList = flightSearchForm.select(" > input[type = hidden]");


        for (Element item : inputList) {

            if (item.attr("name").equals("awardTravel")) {

                nameValuePairs.add(new BasicNameValuePair(item.attr("name"), "true"));

            } else {

                nameValuePairs.add(new BasicNameValuePair(item.attr("name"), item.attr("value")));
            }
        }
//cashmilesradio
        //flexExactradio
        //tripTypeRadio
        nameValuePairs.add(new BasicNameValuePair("tripTypeRadio", "on")); //
        nameValuePairs.add(new BasicNameValuePair("flexExactRadio", "on")); //
        nameValuePairs.add(new BasicNameValuePair("cashMilesRadio", "on"));
        nameValuePairs.add(new BasicNameValuePair("paxCount", seats + ""));
        nameValuePairs.add(new BasicNameValuePair("is_award_travel", "true"));
        nameValuePairs.add(new BasicNameValuePair("cabinFareClass", "economyBasic"));
        nameValuePairs.add(new BasicNameValuePair("flexDays", "3"));
        nameValuePairs.add(new BasicNameValuePair("datesFlexible", "false"));
        nameValuePairs.add(new BasicNameValuePair("pageName", "advanceSearchPage"));
        nameValuePairs.add(new BasicNameValuePair("action", "findFlights"));
        nameValuePairs.add(new BasicNameValuePair("is_Flex_Search", "false"));
        nameValuePairs.add(new BasicNameValuePair("returnDate", ""));
        nameValuePairs.add(new BasicNameValuePair("departureDate", date));
        nameValuePairs.add(new BasicNameValuePair("destinationCity", destination));
        nameValuePairs.add(new BasicNameValuePair("originCity", origin));
        nameValuePairs.add(new BasicNameValuePair("tripType", "ONE_WAY"));

        HttpPost httpPost = new HttpPost("https://www.delta.com" + url);
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        response = httpclient.execute(httpPost);
        entity = response.getEntity();

        String html = ParserUtils.responseToString(entity.getContent());
        String compHome = "";

        Document scriptDoc = Jsoup.parse(search);

        for (Element scriptItem : scriptDoc.getElementsByTag("script")) {

            if (scriptItem.attr("src").contains("compositeHome.min.js")) {

                compHome = scriptItem.attr("src");
            }
        }
        httpGet = new HttpGet("http:" + compHome);
        DefaultHttpClient cl = new DefaultHttpClient();
        response = cl.execute(httpGet);
        // response = httpclient.execute(httpGet);
        entity = response.getEntity();

        String js = ParserUtils.responseToString(entity.getContent());

        int firstIndex = js.indexOf("dwr.engine._origScriptSessionId=\"") + "dwr.engine._origScriptSessionId=\"".length();
        int secondIndex = js.indexOf("\"", firstIndex);

        String scriptSessionId = js.substring(firstIndex, secondIndex) + new Double(Math.floor(1000 * Math.random())).intValue();
        String session = "";

        for (Cookie item : httpclient.getCookieStore().getCookies()) {

            if (item.getName().equals("JSESSIONID")) {

                session = item.getValue();
            }
        }
        int fIndex = html.indexOf("var loginRefreshURL");

        int sIndex = html.indexOf("var shrImageID", fIndex);


        if (fIndex < 0 || sIndex < 0) {
            return awardList;
        }
        String blockWithSum = html.substring(fIndex, sIndex);

        fIndex = blockWithSum.indexOf("var currentSessionCheckSum = \"") + "var currentSessionCheckSum = \"".length();

        sIndex = blockWithSum.indexOf("\" ;", fIndex);
        if (fIndex < 0 || sIndex < 0) {
            return awardList;
        }

        String currentSessionCheckSum = blockWithSum.substring(fIndex, sIndex);
        int f1Index = html.indexOf("&cacheKey=") + "&cacheKey=".length();
        int s2Index = html.indexOf("\";", f1Index);

        String cacheKey = html.substring(f1Index, s2Index);

        String numOfPages = getResultList(httpclient, httpPost, session, scriptSessionId, currentSessionCheckSum, response, entity, awardList, cacheKey, 1, false, requestId, cities);

        int pages = Integer.parseInt(numOfPages);

        for (int i = 1; i < pages; i++) {

            getResultList(httpclient, httpPost, session, scriptSessionId, currentSessionCheckSum, response, entity, awardList, cacheKey, i + 1, true, requestId, cities);
        }
        return awardList;
    }

    /**
     * does all the job, calls all the functions for all days
     *
     * @param requestData
     * @return
     * @throws Exception
     */
    @Override
    public Collection<Trip> parse(RequestData requestData) throws Exception {
        List<Airport> cities = StreamSupport.stream(Spliterators.spliteratorUnknownSize(airportRepository.findAll().iterator(), Spliterator.ORDERED), false)
                .collect(Collectors.toCollection(ArrayList::new));
        List<Trip> results = new ArrayList<>();
        String origin = requestData.getOrigin();
        String destination = requestData.getDestination();
        List<String> cabins = requestData.getCabins();
        int seats = requestData.getSeats();
        int requestId = requestData.getRequest_id();
        List<Date> owDates = requestData.getOwDates();
        System.out.println("owDates" + owDates);
        List<Date> returnDates = requestData.getReturnDates();
        Date date = owDates.get(0);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = sdf.format(date);
        DLParser dlParser = new DLParser();
        List<Trip> delta = dlParser.getDelta(origin, destination, formattedDate, seats, requestId, cities);
        for (Trip trip : delta) {
            results.add(trip);
        }
        System.out.println("results - " + results);
        if (results.size() == 0) {
            return results;
        }
        List<Trip> neededResults = fetchNeededResults(cabins, results);
        System.out.println("needed results - " + neededResults);
        MileCost mileCost = null;
        if (neededResults.size() != 0) {
            List<MileCost> miles = StreamSupport.stream(Spliterators.spliteratorUnknownSize(mileCostRepository.findAll().iterator(), Spliterator.ORDERED), false)
                    .collect(Collectors.toCollection(ArrayList::new));
            mileCost = miles.stream().filter(o -> Objects.equals(o.getParser(), neededResults.get(0).getFlights().get(0).getParser()))
                    .findFirst().get();
            neededResults.get(0).setIsComplete(true);
            setMiles2Trip(neededResults, mileCost);
        }
        return neededResults;
    }

    private void setMiles2Trip(List<Trip> trips, MileCost mileCost) {
        if (mileCost == null) return;
        for (Trip trip : trips) {
            double parserCost = trip.getMiles() / 100 * mileCost.getCost().doubleValue() + trip.getTax().doubleValue();//ещё сложить таксы
            trip.setCost(BigDecimal.valueOf(parserCost));
        }
    }

    /**
     * fetches results with jsu needed cabins
     *
     * @param cabins
     * @param results
     * @return
     */
    private List<Trip> fetchNeededResults(List<String> cabins, List<Trip> results) {
        List<Trip> neededResults = new ArrayList<Trip>();
        Iterator it = results.iterator();
        while (it.hasNext()) {
            Trip trip = (Trip) it.next();
            List<Flight> flights = trip.getFlights();
            for (Flight flight : flights) {
                if (flight.getCabin().equals(FIRST))
                    flight.setCabin(BUSINESS);
            }
            for (String cabin : cabins) {
                if (trip.getFlights().get(0).getCabin().equals(cabin))
                    neededResults.add(trip);
            }
        }
        return neededResults;
    }

    /**
     * gets and then sets flight cities by codes from the database
     *
     * @param cities
     * @param flight
     */
    private void setFlightCitiesByCodes(List<Airport> cities, Flight flight) {
        if (databaseCities == null)
            databaseCities = new HashMap<>();
        boolean isDepartSet = false;
        boolean isArriveSet = false;
        String departCode = flight.getDepartCode();
        String arriveCode = flight.getArriveCode();
        int count = 0;
        if (databaseCities != null && databaseCities.containsKey(departCode)) {
            flight.setDepartPlace(databaseCities.get(departCode));
            isDepartSet = true;
        }
        if (databaseCities != null && databaseCities.containsKey(arriveCode)) {
            flight.setArrivePlace(databaseCities.get(arriveCode));
            isArriveSet = true;
        }
        Iterator it = cities.iterator();
        while (it.hasNext()) {
            if (isDepartSet && isArriveSet)
                break;
            Airport airport = (Airport) it.next();

            if (!isDepartSet && airport.getCodeIata().equals(departCode)) {
                String city = airport.getCity();
                flight.setDepartPlace(city);
                databaseCities.put(departCode, city);
                isDepartSet = true;
                if (isDepartSet && isArriveSet)
                    break;
                continue;
            }
            if (!isArriveSet && airport.getCodeIata().equals(arriveCode)) {
                String city = airport.getCity();
                flight.setArrivePlace(city);
                databaseCities.put(arriveCode, city);
                isArriveSet = true;
                if (isDepartSet && isArriveSet)
                    break;
                continue;
            }
        }
    }

}