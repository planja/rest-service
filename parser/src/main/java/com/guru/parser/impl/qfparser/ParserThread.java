package com.guru.parser.impl.qfparser;

import com.guru.domain.model.Flight;
import com.guru.domain.model.Trip;
import com.guru.parser.utils.ParserUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parser.utils.Utils;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by Никита on 28.04.2016.
 */
class ParserThread implements Callable<List<Trip>> {
    private String date;
    private String returnDate;
    private String origin;
    private String destination;
    private Integer seats;
    private QFParser qfParser;
    private DefaultHttpClient httpclient;

    public ParserThread(DefaultHttpClient httpclient, String date, String returnDate, String origin, String destination, Integer seats, QFParser qfParser) {
        this.date = date;
        this.returnDate = returnDate;
        this.origin = origin;
        this.destination = destination;
        this.seats = seats;
        this.qfParser = qfParser;
        this.httpclient = httpclient;
    }

    public List<Trip> call() throws InterruptedException, IOException, ParseException {
        LinkedList flights = new LinkedList();
        boolean ex = this.parse(flights);
        for (int i = 0; !ex && i != 2; ++i) {
            ex = this.parse(flights);
        }
        return flights;
    }

    private List<BasicNameValuePair> getBasicNameValuePair() {
        return Arrays.asList(
                new BasicNameValuePair("adults", this.seats + ""),
                new BasicNameValuePair("children", "0"),
                new BasicNameValuePair("infants", "0"),
                new BasicNameValuePair("searchOption", "M"),
                new BasicNameValuePair("tripType", "R"),
                new BasicNameValuePair("travelClass", "ECO"),
                new BasicNameValuePair("PAGE_FROM", "/travel/airlines/flight-search/global/en"),
                new BasicNameValuePair("localeOverride", "en_AU"),
                new BasicNameValuePair("ruleSetId", "i18nAwbTripflow.Main"),
                new BasicNameValuePair("mloginffNumber", ""),
                new BasicNameValuePair("mloginsurname", ""),
                new BasicNameValuePair("mloginpin", ""),
                new BasicNameValuePair("isHome", ""));
    }


    private boolean parse(List<Trip> flights) throws ParseException, IOException, InterruptedException {
        HttpPost httget = new HttpPost("https://www.qantas.com.au/tripflowapp/awardBooking.tripflow");

        List<BasicNameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("depAirports", this.origin + "," + this.destination));
        nameValuePairs.add(new BasicNameValuePair("destAirports", this.destination + "," + this.origin));
        nameValuePairs.add(new BasicNameValuePair("travelDates", this.date + "0000," + this.returnDate + "0000"));
        nameValuePairs.addAll(getBasicNameValuePair());

        httget.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        CloseableHttpResponse response = this.httpclient.execute(httget);
        HttpEntity entity = response.getEntity();
        String data = IOUtils.toString(entity.getContent());
        Document doc = Jsoup.parse(data);
        Elements form = doc.getElementsByAttributeValue("name", "SubmissionDetails");
        Iterator iterator = form.iterator();

        while (iterator.hasNext()) {
            Element item = (Element) iterator.next();
            String url = item.attr("action");
            httget = new HttpPost(url);
            nameValuePairs = new ArrayList();
            Elements paramList = item.getElementsByAttributeValue("type", "hidden");
            Iterator result = paramList.iterator();

            while (result.hasNext()) {
                Element resultDoc = (Element) result.next();
                nameValuePairs.add(new BasicNameValuePair(resultDoc.attr("name"), resultDoc.attr("value")));
            }

            httget.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = this.httpclient.execute(httget);
            entity = response.getEntity();
            String result1 = Utils.responseToString(entity.getContent());
            Document resultDoc1 = Jsoup.parse(result1);
            String json;
            if (resultDoc1.getElementById("wdserror-list") != null && resultDoc1.getElementById("flightBack") != null) {
                httget = new HttpPost("https://www.qantas.com.au/tripflowapp/awardBooking.tripflow");
                nameValuePairs = new ArrayList();
                nameValuePairs.add(new BasicNameValuePair("depAirports", this.origin));
                nameValuePairs.add(new BasicNameValuePair("destAirports", this.destination));
                nameValuePairs.add(new BasicNameValuePair("travelDates", this.date + "0000"));
                nameValuePairs.add(new BasicNameValuePair("adults", this.seats + ""));
                nameValuePairs.addAll(getBasicNameValuePair());

                httget.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = this.httpclient.execute(httget);
                entity = response.getEntity();
                data = Utils.responseToString(entity.getContent());
                doc = Jsoup.parse(data);
                form = doc.getElementsByAttributeValue("name", "SubmissionDetails");

                for (Iterator firstIndex = form.iterator(); firstIndex.hasNext(); resultDoc1 = Jsoup.parse(result1)) {
                    Element secondIndex = (Element) firstIndex.next();
                    json = secondIndex.attr("action");
                    httget = new HttpPost(json);
                    nameValuePairs = new ArrayList();
                    paramList = secondIndex.getElementsByAttributeValue("type", "hidden");
                    Iterator flightDataJson = paramList.iterator();

                    while (flightDataJson.hasNext()) {
                        Element urlConnector = (Element) flightDataJson.next();
                        nameValuePairs.add(new BasicNameValuePair(urlConnector.attr("name"), urlConnector.attr("value")));
                    }

                    httget.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    response = this.httpclient.execute(httget);
                    entity = response.getEntity();
                    result1 = Utils.responseToString(entity.getContent());
                }
            }

            Element table = resultDoc1.getElementById("idAvailabilty0");
            this.getAwards(table, flights);
        }


        return true;
    }

    private void getAwards(Element table, List<Trip> flights) throws ParseException, IllegalStateException, JSONException, IOException, InterruptedException {
        int awardIndex = 0;
        boolean newTrip;

        for (Iterator var13 = table.getElementsByTag("tbody").iterator(); var13.hasNext(); ++awardIndex) {
            Element tbody = (Element) var13.next();
            Trip trip = new Trip();
            newTrip = true;
            Iterator ex = tbody.select(" > tr").iterator();

            while (ex.hasNext()) {
                Element tr = (Element) ex.next();
                if (tr.attr("id").contains("idLine")) {
                    Flight flight = new Flight();
                    Elements tdhList = tr.select(" > th");
                    if (newTrip) {
                        tdhList.get(1).getElementsByClass("stops").text();
                        newTrip = false;
                    }
                    trip.setTripDuration(ParserUtils.getTotalTime(tdhList.get(0).getElementsByClass("duration").first().ownText(), this.qfParser));
                    String data_url = tdhList.get(2).getElementsByTag("a").first().attr("data-url");
                    flight.setUrl(data_url);
                    trip.getFlights().add(flight);
                } else {
                    trip.setTripDuration(ParserUtils.getTotalTime(tr.getElementsByClass("total-duration").text(), this.qfParser));
                }
            }
            flights.add(trip);
        }

    }

}