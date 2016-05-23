package com.guru.parser.utils;

import com.guru.domain.model.ClasInfo;
import com.guru.domain.model.Flight;
import com.guru.domain.model.MileCost;
import com.guru.domain.model.Trip;
import com.guru.parser.dl.DLParser;
import com.guru.parser.impl.acparser.ACParser;
import com.guru.parser.impl.qfparser.Info;
import com.guru.parser.impl.qfparser.QFParser;
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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

/**
 * Created by Никита on 27.04.2016.
 */
public class ParserUtils {

    public static String getTotalTime(String totalTime, Object parser) throws ParseException {
        String regexp = "";
        if (parser instanceof QFParser)
            regexp = "((\\d*)h\\s)?(\\d*)m";
        if (parser instanceof QFParser)
            regexp = "((\\d*)h\\s)?(\\d*)m";
        if (parser instanceof ACParser) {

            regexp = "((\\d*)[h]\\s)?(\\d*)[min]";

        }


        if (parser instanceof DLParser) {
            if (totalTime.contains("m"))
                regexp = "((\\d*)[h]\\s)?(\\d*)[m]";
            else
                regexp = "((\\d*)[h])";


        }

        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(totalTime);
        if (matcher.find()) {
            String hours = matcher.group(2) == null ? "0" : matcher.group(2);
            String minutes;
            if (totalTime.contains("m"))
                minutes = matcher.group(3) != null && matcher.group(3).trim().length() != 0 ? matcher.group(3) : "0";
            else
                minutes = "0";
            DecimalFormat acFormat = new DecimalFormat("##00");
            return acFormat.format((long) Integer.parseInt(hours)) + ":" + acFormat.format((long) Integer.parseInt(minutes));
        } else {
            return null;
        }
    }

    public static String getStops(List<Flight> flights) {
        if (flights.size() == 1) return "[]";
        else {
            String str = "[" + flights.subList(1, flights.size()).stream()
                    .map(o -> "\"" + o.getDepartCode() + "\",").collect(Collectors.joining()) + "]";
            int index = str.lastIndexOf(',');//Переделать
            return str.substring(0, index) + str.substring(index + 1, str.length());
        }

    }


    public static String getCarriers(List<Flight> flights) {
        String str = "[" + flights.stream().map(o -> "\"" + o.getCarrierCode() + "\",").collect(Collectors.joining()) + "]";//??????
        int index = str.lastIndexOf(',');//Переделать
        return str.substring(0, index) + str.substring(index + 1, str.length());
    }

    public static String getFlightLegs(List<Flight> flights) {
        String str = "[" + flights.stream().map(o -> "\"" + o.getFlightDuration() + "\",").collect(Collectors.joining()) + "]";
        int index = str.lastIndexOf(',');//Переделать
        return str.substring(0, index) + str.substring(index + 1, str.length());
    }

    public static String getTripDuration(Trip trip) {
        long time = getDateDiff(trip.getFlights().get(0).getFullStartDate(),
                trip.getFlights().get(trip.getFlights().size() - 1).getFullEndDate(), TimeUnit.MINUTES);
        return ParserUtils.convertMinutes((int) time);
    }


    public static void setMiles2Trip(List<Trip> trips, MileCost mileCost) {
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
            Integer miles;

            try {
                miles = Integer.valueOf(trip.getClasInfo().stream()
                        .filter(o -> Objects.equals(o.getReduction(), trip.getClas()))
                        .findFirst().get().getMileage());
            } catch (NumberFormatException f) {
                miles = 0;
            }
            trip.setMiles(miles);

            BigDecimal tax = BigDecimal.ZERO;
            if (trip.getTax() != null) tax = trip.getTax();
            double parserCost = miles / 100 * mileCost.getCost().doubleValue() + tax.doubleValue();
            trip.setCost(BigDecimal.valueOf(parserCost));


        }
    }

    public static String getCabins(List<Flight> flights) {
        String str = "[" + flights.stream().map(o -> "\"" + o.getCabin() + "\",").collect(Collectors.joining()) + "]";
        int index = str.lastIndexOf(',');
        return str.substring(0, index) + str.substring(index + 1, str.length());
    }

    public static Map<String, List<Flight>> getLayovers(List<Flight> flights) {
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

    public static String getFlightNumbers(List<Flight> flights) {
        String str = "[" + flights.stream().map(o -> "\"" + o.getFlightNumber() + "\",").collect(Collectors.joining()) + "]";
        int index = str.lastIndexOf(',');//Переделать
        return str.substring(0, index) + str.substring(index + 1, str.length());
    }


    public static List<Flight> getFlightDur(List<Flight> flights) {
        for (Flight flight : flights) {
            long time = getDateDiff(flight.getFullStartDate(), flight.getFullEndDate(), TimeUnit.MINUTES);
            flight.setFlightDuration(ParserUtils.convertMinutes((int) time));
        }
        return flights;
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

  /*  public static FInfo getFlightInfo(String fNumber, Date date, Date dateTo) throws UnsupportedEncodingException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat time = new SimpleDateFormat("h:mm aaa");
        FInfo info = new FInfo();
        info.setArrive("");
        info.setDepart("");
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter("http.protocol.cookie-policy", "compatibility");
        httpclient.setCookieStore(new BasicCookieStore());
        httpclient.setRedirectStrategy(new LaxRedirectStrategy());
        httpclient.getParams().setParameter("http.useragent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
        CloseableHttpResponse response = null;
        HttpEntity entity = null;

        try {
            HttpPost ex = new HttpPost("http://www.flightview.com/TravelTools/FlightTrackerQueryResults.asp");
            ArrayList nameValuePairs = new ArrayList();
            nameValuePairs.add(new BasicNameValuePair("qtype", "sfi"));
            nameValuePairs.add(new BasicNameValuePair("sfw", "/FV/Home/Main"));
            nameValuePairs.add(new BasicNameValuePair("whenArrDep", "dep"));
            nameValuePairs.add(new BasicNameValuePair("namal", "Enter name or code"));
            nameValuePairs.add(new BasicNameValuePair("al", ""));
            nameValuePairs.add(new BasicNameValuePair("fn", fNumber.trim()));
            nameValuePairs.add(new BasicNameValuePair("whenDate", sdf.format(date)));
            nameValuePairs.add(new BasicNameValuePair("input", "Track Flight"));
            ex.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpclient.execute(ex);
            entity = response.getEntity();
            String html = IOUtils.toString(entity.getContent());
            Document document = Jsoup.parse(html);
            if(document.getElementsByClass("FlightTrackerList").size() > 0) {
                String timeF = time.format(date);
                String timeT = time.format(dateTo);
                Elements trList = document.getElementsByClass("FlightTrackerList").first().select(" > tbody > tr");
                Iterator var16 = trList.iterator();

                while(true) {
                    while(true) {
                        Element trItem;
                        do {
                            if(!var16.hasNext()) {
                                return info;
                            }

                            trItem = (Element)var16.next();
                        } while(trItem.select(" > td").size() <= 5);

                        Iterator al;
                        Element fn;
                        String al1;
                        String fn1;
                        if(trItem.select(" > td").get(3).text().contains(timeF) && trItem.select(" > td").get(5).text().contains(timeT)) {
                            ex = new HttpPost("http://www.flightview.com/TravelTools/FlightTrackerQueryResults.asp");
                            nameValuePairs = new ArrayList();
                            al = trItem.select(" > input[type=hidden]").iterator();

                            while(al.hasNext()) {
                                fn = (Element)al.next();
                                nameValuePairs.add(new BasicNameValuePair(fn.attr("name"), fn.attr("value")));
                            }

                            al1 = fNumber.substring(0, 2);
                            fn1 = fNumber.substring(2, fNumber.length());
                            nameValuePairs.add(new BasicNameValuePair("al", al1));
                            nameValuePairs.add(new BasicNameValuePair("fn", fn1));
                            ex.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                            response = httpclient.execute(ex);
                            entity = response.getEntity();
                            html = Utils.responseToString(entity.getContent());
                            document = Jsoup.parse(html);
                            info.setDepart(document.getElementById("txt_depapt") == null?"":document.getElementById("txt_depapt").text());
                            info.setArrive(document.getElementById("txt_arrapt") == null?"":document.getElementById("txt_arrapt").text());
                        } else if(trItem.select(" > td").get(3).text().contains(timeF) && !trItem.select(" > td").get(5).text().contains(timeT)) {
                            ex = new HttpPost("http://www.flightview.com/TravelTools/FlightTrackerQueryResults.asp");
                            nameValuePairs = new ArrayList();
                            al = trItem.select(" > input[type=hidden]").iterator();

                            while(al.hasNext()) {
                                fn = (Element)al.next();
                                nameValuePairs.add(new BasicNameValuePair(fn.attr("name"), fn.attr("value")));
                            }

                            al1 = fNumber.substring(0, 2);
                            fn1 = fNumber.substring(2, fNumber.length());
                            nameValuePairs.add(new BasicNameValuePair("al", al1));
                            nameValuePairs.add(new BasicNameValuePair("fn", fn1));
                            ex.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                            response = httpclient.execute(ex);
                            entity = response.getEntity();
                            html = Utils.responseToString(entity.getContent());
                            document = Jsoup.parse(html);
                            info.setDepart(document.getElementById("txt_depapt") == null?"":document.getElementById("txt_depapt").text());
                        } else if(!trItem.select(" > td").get(3).text().contains(timeF) && trItem.select(" > td").get(5).text().contains(timeT)) {
                            ex = new HttpPost("http://www.flightview.com/TravelTools/FlightTrackerQueryResults.asp");
                            nameValuePairs = new ArrayList();
                            al = trItem.select(" > input[type=hidden]").iterator();

                            while(al.hasNext()) {
                                fn = (Element)al.next();
                                nameValuePairs.add(new BasicNameValuePair(fn.attr("name"), fn.attr("value")));
                            }

                            al1 = fNumber.substring(0, 2);
                            fn1 = fNumber.substring(2, fNumber.length());
                            nameValuePairs.add(new BasicNameValuePair("al", al1));
                            nameValuePairs.add(new BasicNameValuePair("fn", fn1));
                            ex.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                            response = httpclient.execute(ex);
                            entity = response.getEntity();
                            html = Utils.responseToString(entity.getContent());
                            document = Jsoup.parse(html);
                            info.setArrive(document.getElementById("txt_arrapt") == null?"":document.getElementById("txt_arrapt").text());
                        }
                    }
                }
            } else {
                info.setDepart(document.getElementById("txt_depapt") == null?"":document.getElementById("txt_depapt").text());
                info.setArrive(document.getElementById("txt_arrapt") == null?"":document.getElementById("txt_arrapt").text());
            }
        } catch (Exception var20) {
            var20.printStackTrace();
        }

        return info;
    }*/

    public static int randInt(int max) {
        return (int) (Math.random() * (double) max);
    }

    public static String gzipResponseToString(InputStream inputStream) {
        StringBuffer buffer = new StringBuffer();

        try {
            GZIPInputStream ex = new GZIPInputStream(inputStream);
            DataInputStream dataInputStream = new DataInputStream(ex);
            InputStreamReader inputStreamReader = new InputStreamReader(dataInputStream);
            BufferedReader buff = new BufferedReader(inputStreamReader);

            String line;
            do {
                line = buff.readLine();
                if (line != null) {
                    buffer.append(line);
                }
            } while (line != null);

            inputStreamReader.close();
            inputStream.close();
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return buffer.toString();
    }

    public static String responseToString(InputStream inputStream) throws IOException {
        return IOUtils.toString(inputStream);
    }

    public static String convertMinutes(int totalMinutes) {
        int minutes = totalMinutes % 60;
        int hours = totalMinutes / 60;
        String resultMinutes = String.valueOf(minutes).length() == 1 ? "0" + String.valueOf(minutes) : String.valueOf(minutes);
        String resultHours = String.valueOf(hours).length() == 1 ? "0" + String.valueOf(hours) : String.valueOf(hours);
        return resultHours + ":" + resultMinutes;
    }

    public static BigDecimal convertCost(String miles, String tax) {
        BigDecimal min = new BigDecimal(1000 + ".0");
        BigDecimal max = new BigDecimal(2000 + ".0");
        BigDecimal randomBigDecimal = min.add(new BigDecimal(Math.random()).multiply(max.subtract(min)));
        return randomBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static Info getInfo(String fNumber, Date date, Date dateTo) throws UnsupportedEncodingException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat time = new SimpleDateFormat("h:mm aaa");
        Info info = new Info();
        info.setArrive("");
        info.setDepart("");
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter("http.protocol.cookie-policy", "compatibility");
        httpclient.setCookieStore(new BasicCookieStore());
        httpclient.setRedirectStrategy(new LaxRedirectStrategy());
        httpclient.getParams().setParameter("http.useragent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
        CloseableHttpResponse response = null;
        HttpEntity entity = null;

        try {
            HttpPost ex = new HttpPost("http://www.flightview.com/TravelTools/FlightTrackerQueryResults.asp");
            ArrayList nameValuePairs = new ArrayList();
            nameValuePairs.add(new BasicNameValuePair("qtype", "sfi"));
            nameValuePairs.add(new BasicNameValuePair("sfw", "/FV/Home/Main"));
            nameValuePairs.add(new BasicNameValuePair("whenArrDep", "dep"));
            nameValuePairs.add(new BasicNameValuePair("namal", "Enter name or code"));
            nameValuePairs.add(new BasicNameValuePair("al", ""));
            nameValuePairs.add(new BasicNameValuePair("fn", fNumber.trim()));
            nameValuePairs.add(new BasicNameValuePair("whenDate", sdf.format(date)));
            nameValuePairs.add(new BasicNameValuePair("input", "Track Flight"));
            ex.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpclient.execute(ex);
            entity = response.getEntity();
            String html = IOUtils.toString(entity.getContent());
            Document document = Jsoup.parse(html);
            if (document.getElementsByClass("FlightTrackerList").size() > 0) {
                String timeF = time.format(date);
                String timeT = time.format(dateTo);
                Elements trList = document.getElementsByClass("FlightTrackerList").first().select(" > tbody > tr");
                Iterator var16 = trList.iterator();

                while (true) {
                    while (true) {
                        Element trItem;
                        do {
                            if (!var16.hasNext()) {
                                return info;
                            }

                            trItem = (Element) var16.next();
                        } while (trItem.select(" > td").size() <= 5);

                        Iterator al;
                        Element fn;
                        String al1;
                        String fn1;
                        if (trItem.select(" > td").get(3).text().contains(timeF) && trItem.select(" > td").get(5).text().contains(timeT)) {
                            ex = new HttpPost("http://www.flightview.com/TravelTools/FlightTrackerQueryResults.asp");
                            nameValuePairs = new ArrayList();
                            al = trItem.select(" > input[type=hidden]").iterator();

                            while (al.hasNext()) {
                                fn = (Element) al.next();
                                nameValuePairs.add(new BasicNameValuePair(fn.attr("name"), fn.attr("value")));
                            }

                            al1 = fNumber.substring(0, 2);
                            fn1 = fNumber.substring(2, fNumber.length());
                            nameValuePairs.add(new BasicNameValuePair("al", al1));
                            nameValuePairs.add(new BasicNameValuePair("fn", fn1));
                            ex.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                            response = httpclient.execute(ex);
                            entity = response.getEntity();
                            html = IOUtils.toString(entity.getContent());
                            document = Jsoup.parse(html);
                            info.setDepart(document.getElementById("txt_depapt") == null ? "" : document.getElementById("txt_depapt").text());
                            info.setArrive(document.getElementById("txt_arrapt") == null ? "" : document.getElementById("txt_arrapt").text());
                        } else if (trItem.select(" > td").get(3).text().contains(timeF) && !trItem.select(" > td").get(5).text().contains(timeT)) {
                            ex = new HttpPost("http://www.flightview.com/TravelTools/FlightTrackerQueryResults.asp");
                            nameValuePairs = new ArrayList();
                            al = trItem.select(" > input[type=hidden]").iterator();

                            while (al.hasNext()) {
                                fn = (Element) al.next();
                                nameValuePairs.add(new BasicNameValuePair(fn.attr("name"), fn.attr("value")));
                            }

                            al1 = fNumber.substring(0, 2);
                            fn1 = fNumber.substring(2, fNumber.length());
                            nameValuePairs.add(new BasicNameValuePair("al", al1));
                            nameValuePairs.add(new BasicNameValuePair("fn", fn1));
                            ex.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                            response = httpclient.execute(ex);
                            entity = response.getEntity();
                            html = IOUtils.toString(entity.getContent());
                            document = Jsoup.parse(html);
                            info.setDepart(document.getElementById("txt_depapt") == null ? "" : document.getElementById("txt_depapt").text());
                        } else if (!trItem.select(" > td").get(3).text().contains(timeF) && trItem.select(" > td").get(5).text().contains(timeT)) {
                            ex = new HttpPost("http://www.flightview.com/TravelTools/FlightTrackerQueryResults.asp");
                            nameValuePairs = new ArrayList();
                            al = trItem.select(" > input[type=hidden]").iterator();

                            while (al.hasNext()) {
                                fn = (Element) al.next();
                                nameValuePairs.add(new BasicNameValuePair(fn.attr("name"), fn.attr("value")));
                            }

                            al1 = fNumber.substring(0, 2);
                            fn1 = fNumber.substring(2, fNumber.length());
                            nameValuePairs.add(new BasicNameValuePair("al", al1));
                            nameValuePairs.add(new BasicNameValuePair("fn", fn1));
                            ex.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                            response = httpclient.execute(ex);
                            entity = response.getEntity();
                            html = IOUtils.toString(entity.getContent());
                            document = Jsoup.parse(html);
                            info.setArrive(document.getElementById("txt_arrapt") == null ? "" : document.getElementById("txt_arrapt").text());
                        }
                    }
                }
            } else {
                info.setDepart(document.getElementById("txt_depapt") == null ? "" : document.getElementById("txt_depapt").text());
                info.setArrive(document.getElementById("txt_arrapt") == null ? "" : document.getElementById("txt_arrapt").text());
            }
        } catch (Exception var20) {
            var20.printStackTrace();
        }

        return info;
    }


}
