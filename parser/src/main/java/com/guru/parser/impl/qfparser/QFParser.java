package com.guru.parser.impl.qfparser;

import com.guru.domain.model.Trip;
import com.guru.parser.interf.Parser;
import com.guru.parser.utils.ParserUtils;
import com.guru.vo.transfer.RequestData;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parser.exceptions.IncorrectCredentials;
import parser.info.InfoParser;
import parser.model.Award;
import parser.model.FInfo;
import parser.model.Flight;
import parser.utils.ComplexAward;
import parser.utils.Utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Никита on 27.04.2016.
 */
public class QFParser implements Parser {

    @Override
    public Collection<Trip> parse(RequestData requestData) throws Exception {
        return null;
    }

    public static DefaultHttpClient login(String user, String surname, String password) throws IOException, InterruptedException, ExecutionException, IncorrectCredentials {
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

    public ComplexAward getQantas(DefaultHttpClient httpclient, Date date, Date returnDate, String origin, String destination, int seats) throws IOException, InterruptedException, ExecutionException, ParseException {
        ComplexAward ca = new ComplexAward();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        List<Award> awardList = new ArrayList<>
                ((new QFParser.ParserThread(httpclient, sdf1.format(date), sdf1.format(returnDate), origin, destination, seats, this)).call());
        ExecutorService executor = Executors.newCachedThreadPool();
        LinkedList resultList = new LinkedList();
        HashSet callables = new HashSet();
        Iterator futureList = awardList.iterator();

        while (futureList.hasNext()) {
            Award award = (Award) futureList.next();
            callables.add(new QFParser.DataThread(award, httpclient));
        }

        List futureList1 = executor.invokeAll(callables);
        Iterator award2 = futureList1.iterator();

        while (award2.hasNext()) {
            Future award1 = (Future) award2.next();
            resultList.add(award1.get());
        }

        award2 = resultList.iterator();

        while (award2.hasNext()) {
            Award award3 = (Award) award2.next();
            if (award3.getDirection() == 0) {
                //Trip trip = new Trip(award3.);
                ca.getOnewayList().add(award3);
            } else {
                ca.getReturnAward().add(award3);
            }
        }

        return ca;
    }

    public class ParserThread implements Callable<List<Award>> {
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

        public List<Award> call() throws InterruptedException, IOException, ParseException {
            LinkedList trips = new LinkedList();


            LinkedList flights = new LinkedList();
            boolean ex = this.parse(flights);
            for (int i = 0; !ex && i != 2; ++i) {
                ex = this.parse(flights);
            }

            return flights;
        }

        private List<BasicNameValuePair> getBasicNameValuePair() {
            return Arrays.asList(new BasicNameValuePair("depAirports", this.origin + "," + this.destination),
                    new BasicNameValuePair("destAirports", this.destination + "," + this.origin),
                    new BasicNameValuePair("travelDates", this.date + "0000," + this.returnDate + "0000"),
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


        public boolean parse(List<Award> flights) throws ParseException, UnsupportedEncodingException, ClientProtocolException,
                IOException, InterruptedException {

            new SimpleDateFormat("ddMMyyyyHHmm");
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
            Iterator var11 = form.iterator();

            while (var11.hasNext()) {
                Element item = (Element) var11.next();
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

                int firstIndex1 = result1.indexOf("new RFCOForm(") + "new RFCOForm(".length();
                int secondIndex1 = result1.indexOf(");", firstIndex1);
                json = result1.substring(firstIndex1, secondIndex1);
                JSONObject flightDataJson1 = (new JSONObject(json)).getJSONObject("availability");
               Element table = resultDoc1.getElementById("idAvailabilty0");
                this.getAwards(table, flights, 0);
              Element table1 = resultDoc1.getElementById("idAvailabilty1");
                this.getAwards(table1,flights, 1);
            }


            return true;
        }

        //Делаем Trip
        private void getAwards(Element table,List<Award> flights, int direction) throws ParseException, IllegalStateException, JSONException, IOException, InterruptedException {
            int awardIndex = 0;
            boolean newAward;

            for (Iterator var13 = table.getElementsByTag("tbody").iterator(); var13.hasNext(); ++awardIndex) {
                Element tbody = (Element) var13.next();
                Award award = new Award();
                newAward = true;
                Iterator ex = tbody.select(" > tr").iterator();

                while (ex.hasNext()) {
                    Element tr = (Element) ex.next();
                    if (tr.attr("id").contains("idLine")) {
                        Flight flight = new Flight();
                        Elements tdhList = tr.select(" > th");
                        if (newAward) {
                            tdhList.get(1).getElementsByClass("stops").text();
                            newAward = false;
                        }
                        award.setTotalDuration(ParserUtils.getTotalTime(tdhList.get(0).getElementsByClass("duration").first().ownText(), this.qfParser));
                        String data_url = tdhList.get(2).getElementsByTag("a").first().attr("data-url");
                        flight.setUrl(data_url);
                        award.getFlights().add(flight);
                    } else {
                        award.setTotalDuration(ParserUtils.getTotalTime(tr.getElementsByClass("total-duration").text(), this.qfParser));
                    }
                }
                award.setDirection(direction);
                flights.add(award);
            }

        }

    }

    public class DataThread implements Callable<Award> {
        private Award award;
        private DefaultHttpClient httpclient;

        public DataThread(Award award, DefaultHttpClient httpclient) {
            this.award = award;
            this.httpclient = httpclient;
        }

        public Award call() throws Exception {
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

            new SimpleDateFormat("ddMMyyyyHHmm");
            SimpleDateFormat var28 = new SimpleDateFormat("EEE dd MMM yy HH:mm", Locale.US);
            Iterator var4 = this.award.getFlights().iterator();

            while (var4.hasNext()) {
                Flight flight = (Flight) var4.next();
                HttpGet httGet = new HttpGet("https:" + flight.getUrl());
                httGet.addHeader("Referer", "https://book.qantas.com.au/pl/QFAward/wds/OverrideServlet");
                httGet.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
                httGet.addHeader("Accept-Encoding", "gzip, deflate");
                httGet.addHeader("Content-Type", "text/javascript; charset=utf-8");
                httGet.addHeader("X-Requested-With", "XMLHttpRequest");
                CloseableHttpResponse response;
                HttpEntity entity;
                response = httpclient.execute(httGet);
                entity = response.getEntity();
                String flight_info = Utils.gzipResponseToString(entity.getContent());
                JSONObject jObject = (new JSONObject(flight_info)).getJSONObject("model");
                String code = jObject.getString("flightCode");
                String departureLocation = jObject.getString("departureLocation");
                String arrivalLocation = jObject.getString("arrivalLocation");
                String departureDate = jObject.getString("departureDate");
                String arrivalDate = jObject.getString("arrivalDate");
                String departureTime = jObject.getString("departureTime");
                String arrivalTime = jObject.getString("arrivalTime");
                String totalDuration = jObject.getString("totalDuration");
                JSONArray mealsArray = jObject.getJSONArray("meals");
                JSONArray aircraftTypesArray = jObject.getJSONArray("aircraftTypes");
                String meal = "";

                for (int aircraft = 0; aircraft < mealsArray.length(); ++aircraft) {
                    meal = meal + " " + mealsArray.getString(aircraft);
                }

                String var26 = "";

                for (int depDate = 0; depDate < aircraftTypesArray.length(); ++depDate) {
                    var26 = var26 + aircraftTypesArray.getJSONObject(depDate).getString("aircraftName");
                }

                Date var27 = var28.parse(departureDate + " " + departureTime);
                Date arrDate = var28.parse(arrivalDate + " " + arrivalTime);
                FInfo info = InfoParser.getFlightInfo(code, var27, arrDate);
                flight.setAircraft(var26);
                flight.setArriveDate(arrivalDate);
                flight.setArrivePlace(arrivalLocation);
                flight.setArriveAirport(info.getArrive());
                flight.setArriveTime(arrivalTime);
                flight.setDepartDate(departureDate);
                flight.setDepartPlace(departureLocation);
                flight.setDepartAirport(info.getDepart());
                flight.setDepartTime(departureTime);
                flight.setFlight(code);
                flight.setMeal(meal);
                flight.setTravelTime(totalDuration);
                flight.setFlightCabin("");
            }
            return this.award;
        }
    }
}
