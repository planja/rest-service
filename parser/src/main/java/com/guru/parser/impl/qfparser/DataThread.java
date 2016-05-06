package com.guru.parser.impl.qfparser;

import com.guru.domain.model.ClasInfo;
import com.guru.domain.model.Flight;
import com.guru.domain.model.Trip;
import com.guru.parser.utils.ParserUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Никита on 28.04.2016.
 */
class DataThread implements Callable<Trip> {
    private Trip trip;
    private DefaultHttpClient httpclient;
    private int counter = 0;

    public DataThread(Trip trip, DefaultHttpClient httpclient) {
        this.trip = trip;
        this.httpclient = httpclient;
    }

    public Trip call() throws IOException, Exception {
        SimpleDateFormat format = new SimpleDateFormat("EEE dd MMM yy", Locale.US);
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter("http.protocol.cookie-policy", "compatibility");
        httpclient.setCookieStore(new BasicCookieStore());
        httpclient.setRedirectStrategy(new LaxRedirectStrategy());
        httpclient.getParams().setParameter("http.useragent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
        Iterator sdf = this.httpclient.getCookieStore().getCookies().iterator();

        while (sdf.hasNext()) {
            Cookie dt_format_or = (Cookie) sdf.next();
            httpclient.getCookieStore().addCookie(dt_format_or);
        }

        Iterator iterator = this.trip.getFlights().iterator();

        while (iterator.hasNext()) {
            Flight flight = (Flight) iterator.next();
            HttpGet httGet = new HttpGet("https:" + flight.getUrl());
            httGet.addHeader("Referer", "https://book.qantas.com.au/pl/QFAward/wds/OverrideServlet");
            httGet.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
            httGet.addHeader("Accept-Encoding", "gzip, deflate");
            httGet.addHeader("Content-Type", "text/javascript; charset=utf-8");
            httGet.addHeader("X-Requested-With", "XMLHttpRequest");

            CloseableHttpResponse response = httpclient.execute(httGet);
            HttpEntity entity = response.getEntity();
            String flight_info = ParserUtils.gzipResponseToString(entity.getContent());
            JSONObject jObject = (new JSONObject(flight_info)).getJSONObject("model");
            String departureLocation = jObject.getString("departureLocation");
            String arrivalLocation = jObject.getString("arrivalLocation");
            String departureDate = jObject.getString("departureDate");
            String arrivalDate = jObject.getString("arrivalDate");
            String departureTime = jObject.getString("departureTime");
            String arrivalTime = jObject.getString("arrivalTime");
            String totalDuration = jObject.getString("totalDuration");
            JSONArray aircraftTypesArray = jObject.getJSONArray("aircraftTypes");
            String airCraft = aircraftTypesArray.getJSONObject(0).getString("aircraftName");
            String company = jObject.getString("operatedBy");
            String tripType = jObject.getString("tripType");
            String flightNumber = jObject.getString("flightCode");

            String code = jObject.getString("flightCode");

            SimpleDateFormat var28 = new SimpleDateFormat("EEE dd MMM yy HH:mm", Locale.US);
            Date var27 = var28.parse(departureDate + " " + departureTime);
            Date arrDate = var28.parse(arrivalDate + " " + arrivalTime);
            Info info = ParserUtils.getInfo(code, var27, arrDate);
            flight.setFullStartDate(var27);
            flight.setFullEndDate(arrDate);

            Pattern pat = Pattern.compile("[-]?[0-9]+(.[0-9]+)?");
            Matcher matcher = pat.matcher(totalDuration);
            List<Integer> integerList = new ArrayList<>();
            while (matcher.find()) {

                integerList.add(Integer.valueOf(matcher.group()));
            }

            int mins = integerList.get(0) * 60 + integerList.get(1);
            totalDuration = ParserUtils.convertMinutes(mins);


            flight.setPosition(this.counter);
            flight.setParser("QF");
            flight.setCarrierName(flightNumber.substring(0, 2));
            flight.setCarrierCode(flightNumber.substring(0, 2));
            flight.setFlightDuration(totalDuration);
            flight.setCabin(tripType);
            flight.setDepartDate(format.parse(departureDate));
            flight.setDepartPlace(departureLocation);
            flight.setDepartTime(departureTime);
            flight.setDepartCode(info.getDepart());
            flight.setArriveDate(format.parse(arrivalDate));
            flight.setArrivePlace(arrivalLocation);
            flight.setArriveTime(arrivalTime);
            flight.setArriveCode(info.getArrive());
            flight.setFlightNumber(flightNumber);
            flight.setLayover("[]");
            flight.setAircraft(airCraft);
            flight.setUpdatedAt(new Date());
            flight.setCreatedAt(new Date());

            this.counter++;
        }
        // this.trip.getFlights().s
        //this.getMiles(this.award.getSaverEconomy() == null?null:this.award.getSaverEconomy().getUrl(), this.award.getSaverEconomy());
        //this.getMiles(this.award.getSaverBusiness() == null?null:this.award.getSaverBusiness().getUrl(), this.award.getSaverBusiness());
        //this.getMiles(this.award.getSaverFirst() == null?null:this.award.getSaverFirst().getUrl(), this.award.getSaverFirst());
        //this.getMiles(this.award.getSaverPremium() == null?null:this.award.getSaverPremium().getUrl(), this.award.getSaverPremium());

        List<ClasInfo> clasInfos = new ArrayList<>();
        for (ClasInfo info : trip.getClasInfo()) {
            clasInfos.add(getMiles(info.getUrl(), info));
        }
        trip.setClasInfo(clasInfos);

        return this.trip;
    }

    private ClasInfo getMiles(String urlMiles, ClasInfo info) throws IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter("http.protocol.cookie-policy", "compatibility");
        httpclient.setCookieStore(new BasicCookieStore());
        httpclient.setRedirectStrategy(new LaxRedirectStrategy());
        httpclient.getParams().setParameter("http.useragent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
        Iterator httGet = this.httpclient.getCookieStore().getCookies().iterator();

        Cookie response;
        while (httGet.hasNext()) {
            response = (Cookie) httGet.next();
            httpclient.getCookieStore().addCookie(response);
        }

        if (urlMiles != null) {
            HttpGet httGet1 = new HttpGet(urlMiles);
            httGet1.addHeader("Referer", "https://book.qantas.com.au/pl/QFAward/wds/OverrideServlet");
            httGet1.addHeader("Accept", "*/*");
            httGet1.addHeader("Accept-Encoding", "gzip, deflate");
            response = null;
            HttpEntity entity = null;
            CloseableHttpResponse response1 = httpclient.execute(httGet1);
            entity = response1.getEntity();
            String milesJson = ParserUtils.gzipResponseToString(entity.getContent());
            if (milesJson.contains("error")) {
                info.setMileage("");
            }

            int fIndex = milesJson.indexOf("\"costTaxExclusiveWithDiscount\":\"") + "\"costTaxExclusiveWithDiscount\":\"".length();
            int sIndex = milesJson.indexOf("\",", fIndex);
            if (fIndex >= 0 && sIndex >= 0 && sIndex > fIndex) {
                String miles = milesJson.substring(fIndex, sIndex);
                Pattern pattern = Pattern.compile("[A-Za-z]");
                Matcher matcher = pattern.matcher(miles);
                if (matcher.matches() || miles.contains("\"")){
                    info.setMileage("");
                    System.out.println("bad");
                    System.out.println(miles);
                    info.setStatus(0);
                }

                else
                    info.setMileage(miles);
            } else {
                info.setMileage("");
                info.setStatus(0);
            }
        }
        return info;
    }

}