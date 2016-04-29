package com.guru.parser.impl.qfparser;

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


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.Callable;

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

    public Trip call() throws Exception {
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

            Date var27 = format.parse(departureDate + " " + departureTime);
            Date arrDate = format.parse(arrivalDate + " " + arrivalTime);
           // FInfo info = InfoParser.getFlightInfo(code, var27, arrDate);


            flight.setPosition(this.counter);
            flight.setParser("QF");
            flight.setCarrierName(company);
            flight.setCarrierCode(company);
            flight.setFlightDuration(totalDuration);
            flight.setCabin(tripType);
            flight.setDepartDate(format.parse(departureDate));
            flight.setDepartPlace(departureLocation);
            flight.setDepartTime(departureTime);
            flight.setDepartCode("123");
            flight.setArriveDate(format.parse(arrivalDate));
            flight.setArrivePlace(arrivalLocation);
            flight.setArriveTime(arrivalTime);
            flight.setArriveCode("123");
            flight.setFlightNumber(flightNumber);
            flight.setLayover("layover");
            flight.setAircraft(airCraft);
            flight.setUpdatedAt(new Date());
            flight.setCreatedAt(new Date());

            this.counter++;
        }
        return this.trip;
    }
}