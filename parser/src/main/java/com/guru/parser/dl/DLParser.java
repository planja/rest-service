package com.guru.parser.dl;

import com.guru.domain.model.Flight;
import com.guru.domain.model.Trip;
import com.guru.vo.temp.ProxyUtils;
import com.guru.vo.temp.Utils;
import com.guru.vo.temp.exceptions.IncorrectCredentials;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.regex.MatchResult;

/**
 * Created by Anton on 02.05.2016.
 */
public class DLParser {
    public static final String SAVER = "SAVER";
    public static final String STANDART = "STANDART";
    public static final String FULL = "FULL";
    public static final String SAVER_STANDART = "SAVER STANDART";
    public static final String STANDART_FULL = "STANDART FULL";

 /*   public static String getResultList(DefaultHttpClient httpclient, HttpPost httpPost, String session, String scriptSessionId, String currentSessionCheckSum, HttpResponse response, HttpEntity entity, List<Trip> tripList, String cacheKey, int index, boolean resum) throws IOException, FileNotFoundException, ParseException {

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
        httpPost.addHeader("Accept", "*//**");
/*
        StringEntity stringEntity = new StringEntity(dwrInfo, "UTF-8");

        httpPost.setEntity(stringEntity);

        response = httpclient.execute(httpPost);
        entity = response.getEntity();

        String data = Utils.gzipResponseToString(entity.getContent());

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

            tripList.add(getTrip(resultMap, value));
        }
        return numberOfPages;
    }

    public static Trip getTrip(HashMap<String, String> resultMap, String itin) throws FileNotFoundException, ParseException {

        Trip trip = new Trip();

        String slices = resultMap.get(itin + ".slices").replaceAll("\"", "");

        String totalFare = resultMap.get(itin + ".totalFare").replaceAll("\"", "");

        List<String> sliceList = getSliceList(resultMap, slices);

        for (String item : sliceList) {

            getSlice(resultMap, trip, item);
        }

        getFare(resultMap, trip, totalFare);

        return trip;
    }

    public static List<String> getSliceList(HashMap<String, String> resultMap, String slices) throws FileNotFoundException {

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

    public static void getSlice(HashMap<String, String> resultMap, Trip trip, String slice) throws FileNotFoundException, ParseException {

        String duration = resultMap.get(slice + ".duration").replaceAll("\"", "");

        if (!duration.contains("m")) {

            duration = duration + " 0m";
        }

        trip.setTripDuration(Utils.getTotalTime(duration, new DLParser()));

        String flights = resultMap.get(slice + ".flights").replaceAll("\"", "");

        int i = 0;

        do {

            if (resultMap.containsKey(flights + "[" + i + "]")) {

                trip.getFlights().add(getFlight(resultMap, trip, resultMap.get(flights + "[" + i + "]").replaceAll("\"", "")));

            }

            i++;

        } while (resultMap.containsKey(flights + "[" + i + "]"));
    }

    public static void getFare(HashMap<String, String> resultMap, Trip trip, String fare) throws FileNotFoundException {

        int i = 0;

        do {

            if (resultMap.containsKey(fare + "[" + i + "]")) {

                getFareItem(resultMap, trip, resultMap.get(fare + "[" + i + "]").replaceAll("\"", ""), i);
            }

            i++;

        } while (resultMap.containsKey(fare + "[" + i + "]"));

    }

    public static void getFareItem(HashMap<String, String> resultMap, Trip trip, String fareItem, int i) throws FileNotFoundException {

        String found = resultMap.get(fareItem + ".baseAwardMiles");

        if (!found.contains("null")) {

            String cabin = resultMap.get(fareItem + ".cabinName").replaceAll("\"", "");

            String miles = resultMap.get(fareItem + ".totalAwardMiles").replaceAll("\"", "");

            String priceLeft = resultMap.get(fareItem + ".totalPriceLeft").replaceAll("\"", "");

            String priceRight = resultMap.get(fareItem + ".totalPriceRight").replaceAll("\"", "");

            String miscFlightInfos = resultMap.get(fareItem + ".miscFlightInfos");

            String currencyCode = resultMap.get(fareItem + ".currencyCode").replaceAll("\"", "");

            Info info = new Info();
            info.setNa(false);
            info.setStatus(Info.AVAILABLE);
            info.setMileage(miles);
            info.setTax(priceLeft + priceRight);
            info.setCurrency(currencyCode);

            int j = 0;

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
                        trip.getFlights().get(j).setCabin("Economy");
                        info.getMixedCabins().add("Economy");

                        if (info.getMixedCabins().contains("Business") || info.getMixedCabins().contains("First")) {

                            info.setMixed(true);
                        }
                    }

                    if (bookingCode.contains("O") || bookingCode.contains("Z") || bookingCode.contains("G")) {

                        trip.getFlights().get(j).setCabin("Business");
                        info.getMixedCabins().add("Business");

                        if (info.getMixedCabins().contains("Economy") || info.getMixedCabins().contains("First")) {

                            info.setMixed(true);
                        }
                    }

                    if (bookingCode.contains("R") || bookingCode.contains("A")) {

                        trip.getFlights().get(j).setCabin("First");
                        info.getMixedCabins().add("First");

                        if (info.getMixedCabins().contains("Business") || info.getMixedCabins().contains("Economy")) {

                            info.setMixed(true);
                        }

                        first = true;
                    }

                    if (bookingCode.equals("N") || bookingCode.equals("X") || bookingCode.equals("T") || bookingCode.equals("U") || bookingCode.equals("W")) {

                        awardName = SAVER;
                    }

                    if (bookingCode.equals("O") || bookingCode.equals("Z") || bookingCode.equals("G")) {

                        awardName = SAVER;
                    }

                    if (bookingCode.equals("R") || bookingCode.equals("A")) {

                        awardName = SAVER;
                    }

                    if (bookingCode.contains("L")) {

                        awardName = SAVER_STANDART;
                    }

                    if (bookingCode.contains("D")) {

                        awardName = STANDART;
                    }

                    if (bookingCode.contains("S")) {

                        awardName = STANDART_FULL;
                    }

                    if (bookingCode.contains("K")) {

                        awardName = FULL;
                    }

                    info.getAwardNames().add(awardName);
                }

                j++;

            } while (resultMap.containsKey(miscFlightInfos + "[" + j + "]"));


            if (first) {

                trip.setFirst(info);

            } else if (cabin.contains("Economy") || cabin.contains("Main")) {

                trip.setEconomy(info);

            } else if (cabin.contains("Business")) {

                trip.setBusiness(info);

            } else if (cabin.contains("One")) {

                trip.setBusiness(info);
            }

        }
    }

    public static Flight getFlight(HashMap<String, String> resultMap, Trip trip, String flight) throws FileNotFoundException {

        Flight flightResult = new Flight();

        String flightArrivalDate = resultMap.get(flight + ".flightArrivalDate").replaceAll("\"", "");

        String flightArrivalTime = resultMap.get(flight + ".flightArrivalTime").replaceAll("\"", "");

        String flightDepartureDate = resultMap.get(flight + ".flightDepartureDate").replaceAll("\"", "");

        String flightDepartureTime = resultMap.get(flight + ".flightDepartureTime").replaceAll("\"", "");

        String flightDestinationCode = getOriginDestinationCode(resultMap, resultMap.get(flight + ".flightDestination").replaceAll("\"", ""));
        String flightDestinationName = getOriginDestinationName(resultMap, resultMap.get(flight + ".flightDestination").replaceAll("\"", ""));

        String flightOriginCode = getOriginDestinationCode(resultMap, resultMap.get(flight + ".flightOrigin").replaceAll("\"", ""));
        String flightOriginName = getOriginDestinationName(resultMap, resultMap.get(flight + ".flightOrigin").replaceAll("\"", ""));

        String layoverInfos = resultMap.get(flight + ".layoverInfos").replaceAll("\"", "");

        getLayover(resultMap, trip, layoverInfos);

        String legs = resultMap.get(flight + ".legs").replaceAll("\"", "");

        getLegs(resultMap, flightResult, legs);

        flightResult.setArriveDate(flightArrivalDate);
        flightResult.setArrivePlace(flightDestinationName);
    //    flightResult.setArriveAirport(flightDestinationCode);
        flightResult.setArriveTime(flightArrivalTime);
        flightResult.setDepartDate(flightDepartureDate);
        flightResult.setDepartPlace(flightOriginName);
   //     flightResult.setDepartAirport(flightOriginCode);
        flightResult.setDepartTime(flightDepartureTime);
        flightResult.setCabin("");
   //     flightResult.setMeal("");

        return flightResult;
    }

    public static String getOriginDestinationCode(HashMap<String, String> resultMap, String dest) throws FileNotFoundException {

        String airportCode = resultMap.get(dest + ".airportCode").replaceAll("\"", "");

        String airportName = resultMap.get(dest + ".airportName").replaceAll("\"", "");

        return airportCode;
    }

    public static String getOriginDestinationName(HashMap<String, String> resultMap, String dest) throws FileNotFoundException {

        String airportCode = resultMap.get(dest + ".airportCode").replaceAll("\"", "");

        String airportName = resultMap.get(dest + ".airportName").replaceAll("\"", "");

        return airportName;
    }

    public static void getLayover(HashMap<String, String> resultMap, Trip trip, String layover) throws FileNotFoundException {

        if (resultMap.containsKey(layover + "[0]")) {

            String layoverId = resultMap.get(layover + "[0]").replaceAll("\"", "");

            String duration = resultMap.get(layoverId + ".duration").replaceAll("\"", "");
//сделать json
            trip.setLayovers(duration);
        }
    }

    public static void getLegs(HashMap<String, String> resultMap, Flight flight, String leg) throws FileNotFoundException {

        String legItem = resultMap.get(leg + "[0]").replaceAll("\"", "");

        String aircraft = getAircraft(resultMap, resultMap.get(legItem + ".aircraft").replaceAll("\"", ""));

        String layoverInfoList = resultMap.get(legItem + ".layoverInfos").replaceAll("\"", "");

        int i = 0;

        while (resultMap.containsKey(layoverInfoList + "[" + i + "]")) {

            String stop = resultMap.get(layoverInfoList + "[" + i + "]").replaceAll("\"", "");

            String arpStop = resultMap.get(stop + ".equipChgAptCode").replaceAll("\"", "");
            String duration = resultMap.get(stop + ".duration").replaceAll("\"", "");
//сделать тут layovers
            Stop s = new Stop();
            s.setAirport(arpStop);
            s.setDuration(duration);

            flight.getHiddenStopList().add(s);

            i++;
        }

        String duration = resultMap.get(legItem + ".duration").replaceAll("\"", "");

        String fNumber = getFNumber(resultMap, resultMap.get(legItem + ".operatingAirline").replaceAll("\"", ""));

        flight.setAircraft(aircraft);
        flight.setFlightDuration(duration);
        flight.setFlightNumber(fNumber);
    }

    public static String getFNumber(HashMap<String, String> resultMap, String operAir) throws FileNotFoundException {

        String airline = resultMap.get(operAir + ".airline").replaceAll("\"", "");

        String number = resultMap.get(operAir + ".flightNbr").replaceAll("\"", "");

        String code = resultMap.get(airline + ".airlineCode").replaceAll("\"", "");

        return code + " " + number;
    }

    public static String getAircraft(HashMap<String, String> resultMap, String aircraft) throws FileNotFoundException {

        return resultMap.get(aircraft + ".shortName").replaceAll("\"", "");
    }

    public List<Trip> getDelta(String origin, String destination, String date, int seats) throws IOException, IncorrectCredentials, FileNotFoundException, ParseException {

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

        String proxyInfo = ProxyUtils.getProxy("DL");

        System.out.println(proxyInfo);

        String credent = proxyInfo.split("@")[0];
        String ipport = proxyInfo.split("@")[1];

        HttpHost proxy = new HttpHost(ipport.split(":")[0], Integer.parseInt(ipport.split(":")[1]));


        Credentials credentials = new UsernamePasswordCredentials(credent.split(":")[0], credent.split(":")[1]);
        AuthScope authScope = new AuthScope(ipport.split(":")[0], Integer.parseInt(ipport.split(":")[1]));
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(authScope, credentials);

        httpclient.setCredentialsProvider(credsProvider);
        httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

        HttpGet httpGet = new HttpGet("https://www.delta.com/");

        response = httpclient.execute(httpGet);
        entity = response.getEntity();

        String search = Utils.responseToString(entity.getContent());

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

        String html = Utils.responseToString(entity.getContent());
        // System.out.println(html);

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

        String js = Utils.responseToString(entity.getContent());

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

        String numOfPages = getResultList(httpclient, httpPost, session, scriptSessionId, currentSessionCheckSum, response, entity, awardList, cacheKey, 1, false);

        int pages = Integer.parseInt(numOfPages);

        for (int i = 1; i < pages; i++) {

            getResultList(httpclient, httpPost, session, scriptSessionId, currentSessionCheckSum, response, entity, awardList, cacheKey, i + 1, true);
        }

        return awardList;

    }
*/
}
