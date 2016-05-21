package com.guru.parser.impl.qfparser;

import com.guru.domain.model.ClasInfo;
import com.guru.domain.model.Flight;
import com.guru.domain.model.MileCost;
import com.guru.domain.model.Trip;
import com.guru.domain.repository.MileCostRepository;
import com.guru.parser.interf.Parser;
import com.guru.parser.utils.ComplexTrip;
import com.guru.parser.utils.ParserUtils;
import com.guru.vo.temp.ProxyUtils;
import com.guru.vo.transfer.RequestData;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NoHttpResponseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.cookie.*;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parser.exceptions.IncorrectCredentials;

import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by Никита on 21.05.2016.
 */
public class QFParser implements Parser {

    @Inject
    private MileCostRepository mileCostRepository;

    public QFParser() {
    }

    @Override
    public Collection<Trip> parse(RequestData requestData) throws Exception {
        List<Trip> trips = new ArrayList<>();


        MileCost mileCost = null;

        ComplexTrip flights = getQantas(requestData.getDefaultHttpClient(), requestData.getOw_start_date(),
                requestData.getRt_start_date(), requestData.getOrigin(), requestData.getDestination(),
                requestData.getSeats());
        if (Objects.equals(requestData.getType(), "ow")) {
            List<Trip> owTrips = getTrips(flights.getOneWayList(), requestData.getCabins(),
                    (long) requestData.getRequest_id());
            if (owTrips.size() != 0) {
                List<MileCost> miles = StreamSupport.stream(Spliterators.spliteratorUnknownSize(mileCostRepository.findAll().iterator(), Spliterator.ORDERED), false)
                        .collect(Collectors.toCollection(ArrayList::new));
                mileCost = miles.stream().filter(o -> Objects.equals(o.getParser(), owTrips.get(0).getFlights().get(0).getParser()))
                        .findFirst().get();
                ParserUtils.setMiles2Trip(owTrips, mileCost);
                return owTrips;
            }
        } else {
            List<Trip> owTrips = getTrips(flights.getOneWayList(), requestData.getCabins(),
                    (long) requestData.getRequest_id());
            List<Trip> rtTrips = getTrips(flights.getReturnWayList(), requestData.getCabins(),
                    (long) requestData.getRequest_id());
            if (owTrips.size() != 0 || rtTrips.size() != 0) {
                List<MileCost> miles = StreamSupport.stream(Spliterators.spliteratorUnknownSize(mileCostRepository.findAll().iterator(), Spliterator.ORDERED), false)
                        .collect(Collectors.toCollection(ArrayList::new));
                if (owTrips.size() == 0) {

                    mileCost = miles.stream().filter(o -> Objects.equals(o.getParser(), rtTrips.get(0).getFlights().get(0).getParser()))
                            .findFirst().get();
                } else {
                    mileCost = miles.stream().filter(o -> Objects.equals(o.getParser(), owTrips.get(0).getFlights().get(0).getParser()))
                            .findFirst().get();
                }
                ParserUtils.setMiles2Trip(rtTrips, mileCost);
                ParserUtils.setMiles2Trip(owTrips, mileCost);
            }
            trips.addAll(owTrips);
            trips.addAll(rtTrips);
            return trips;
        }
        return trips;
    }

    private List<Trip> getTrips(List<Trip> trips, List<String> classes, Long queryId) {
        trips.stream().forEach(o -> {
            o.setFlights(ParserUtils.getFlightDur(o.getFlights()));
            o.setQueryId(queryId);
            o.setArriveCode(o.getFlights().get(o.getFlights().size() - 1).getArriveCode());
            o.setDepartCode(o.getFlights().get(0).getDepartCode());

            o.setArrivePlace(o.getFlights().get(o.getFlights().size() - 1).getArrivePlace());
            o.setDepartPlace(o.getFlights().get(0).getDepartPlace());

            o.setTripDate(o.getFlights().get(0).getDepartDate());
            o.setStops(ParserUtils.getStops(o.getFlights()));
            o.setCarriers(ParserUtils.getCarriers(o.getFlights()));
            o.setFlightLegs(ParserUtils.getFlightLegs(o.getFlights()));
            o.setFlightNumbers(ParserUtils.getFlightNumbers(o.getFlights()));

            o.setCost(BigDecimal.valueOf(1000L + ((long) (new Random().nextDouble() * (2000L - 1000L)))));
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
        return setCabin(trips, classes);
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
                trip.setCabins(ParserUtils.getCabins(trip.getFlights()));
                result.add(trip);
            }
        }
        System.out.println(result.size());
        return result;
    }


    public static DefaultHttpClient getThreadSafeClient() {
        DefaultHttpClient client = new DefaultHttpClient();
        ClientConnectionManager mgr = client.getConnectionManager();
        HttpParams params = client.getParams();
        ThreadSafeClientConnManager tcm = new ThreadSafeClientConnManager(params, mgr.getSchemeRegistry());
        tcm.setDefaultMaxPerRoute(50);
        tcm.setMaxTotal(100);
        client = new DefaultHttpClient(tcm, params);
        return client;
    }

    public static DefaultHttpClient login(String user, String surname, String password) throws IOException, InterruptedException, ExecutionException, IncorrectCredentials {

        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter("http.protocol.cookie-policy", "compatibility");
        CookieStore cookieStore = new BasicCookieStore();

        // Bind custom cookie store to the local context
        httpclient.setCookieStore(cookieStore);
        CookieSpecFactory csf = new CookieSpecFactory() {
            public CookieSpec newInstance(HttpParams params) {
                return new BrowserCompatSpec() {
                    @Override
                    public void validate(Cookie cookie, CookieOrigin origin)
                            throws MalformedCookieException {
                        // Oh, I am easy.
                        // Allow all cookies
                        // log.debug("custom validate");
                    }
                };
            }
        };
        httpclient.getCookieSpecs().register("easy", csf);
        httpclient.getParams().setParameter(
                ClientPNames.COOKIE_POLICY, "easy");
        httpclient.setRedirectStrategy(new LaxRedirectStrategy());
        httpclient.getParams().setParameter("http.useragent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String credent = null;
        String ipport = null;
        String proxyInfo = ProxyUtils.getProxy("QF");
        System.out.println(proxyInfo);

        credent = proxyInfo.split("@")[0];
        ipport = proxyInfo.split("@")[1];

        HttpHost proxy = new HttpHost(ipport.split(":")[0], Integer.parseInt(ipport.split(":")[1]));
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(credent.split(":")[0], credent.split(":")[1]);
        AuthScope authScope = new AuthScope(ipport.split(":")[0], Integer.parseInt(ipport.split(":")[1]));
        BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(authScope, credentials);

        httpclient.setCredentialsProvider(credsProvider);
        httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

        HttpPost httget = new HttpPost("http://www.qantas.com.au");
        httpclient.execute(httget).close();
        httget = new HttpPost("https://www.qantas.com.au/fflyer/dyns/dologin?action=login&origin=homepage");
        ArrayList nameValuePairs = new ArrayList();
        nameValuePairs.add(new BasicNameValuePair("login_ffNumber", user));
        nameValuePairs.add(new BasicNameValuePair("login_surname", surname));
        nameValuePairs.add(new BasicNameValuePair("login_pin", password));
        httget.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        response = httpclient.execute(httget);
        entity = response.getEntity();
        String login = ParserUtils.responseToString(entity.getContent());
        Document logDoc = Jsoup.parse(login);
        if (logDoc.getElementById("errormsgs") != null) {
            return null;
        } else {
            httget = new HttpPost("http://www.qantas.com.au/travel/airlines/home/gb/en");
            response = httpclient.execute(httget);
            entity = response.getEntity();
            String data = ParserUtils.responseToString(entity.getContent());
            return httpclient;
        }
    }

    public ComplexTrip getQantas(DefaultHttpClient httpclient, Date date, Date returnDate, String origin, String destination, int seats) throws IOException, InterruptedException, ExecutionException {

        ComplexTrip ca = new ComplexTrip();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        LinkedList awardList = new LinkedList();
        awardList.addAll((new QFParser.ParserThread(httpclient, sdf1.format(date), sdf1.format(returnDate), origin, destination, Integer.valueOf(seats), this)).call());
        ExecutorService executor = Executors.newCachedThreadPool();
        LinkedList resultList = new LinkedList();
        HashSet callables = new HashSet();
        Iterator futureList = awardList.iterator();

        while (futureList.hasNext()) {
            Trip award = (Trip) futureList.next();
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
            Trip award3 = (Trip) award2.next();
            if (award3.getDirection() == 0) {
                ca.getOneWayList().add(award3);
            } else {
                ca.getReturnWayList().add(award3);
            }
        }

        executor.shutdown();
        return ca;
    }

    public class ParserThread implements Callable<List<Trip>> {
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

        public List<Trip> call() {
            LinkedList flights = new LinkedList();

            try {
                boolean ex = this.parse(flights);

                for (int i = 0; !ex && i != 2; ++i) {
                    ex = this.parse(flights);
                }
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            return flights;
        }

        public boolean parse(List<Trip> flights) {
            try {
                new SimpleDateFormat("ddMMyyyyHHmm");
                SimpleDateFormat dt_format_or = new SimpleDateFormat("EEE dd MMM yy HH:mm", Locale.US);
                CloseableHttpResponse response = null;
                HttpEntity entity = null;
                HttpPost httget = new HttpPost("https://www.qantas.com.au/tripflowapp/awardBooking.tripflow");
                ArrayList nameValuePairs = new ArrayList();
                nameValuePairs.add(new BasicNameValuePair("depAirports", this.origin + "," + this.destination));
                nameValuePairs.add(new BasicNameValuePair("destAirports", this.destination + "," + this.origin));
                nameValuePairs.add(new BasicNameValuePair("travelDates", this.date + "0000," + this.returnDate + "0000"));
                nameValuePairs.add(new BasicNameValuePair("adults", this.seats + ""));
                nameValuePairs.add(new BasicNameValuePair("children", "0"));
                nameValuePairs.add(new BasicNameValuePair("infants", "0"));
                nameValuePairs.add(new BasicNameValuePair("searchOption", "M"));
                nameValuePairs.add(new BasicNameValuePair("tripType", "R"));
                nameValuePairs.add(new BasicNameValuePair("travelClass", "ECO"));
                nameValuePairs.add(new BasicNameValuePair("PAGE_FROM", "/travel/airlines/flight-search/global/en"));
                nameValuePairs.add(new BasicNameValuePair("localeOverride", "en_AU"));
                nameValuePairs.add(new BasicNameValuePair("ruleSetId", "i18nAwbTripflow.Main"));
                nameValuePairs.add(new BasicNameValuePair("mloginffNumber", ""));
                nameValuePairs.add(new BasicNameValuePair("mloginsurname", ""));
                nameValuePairs.add(new BasicNameValuePair("mloginpin", ""));
                nameValuePairs.add(new BasicNameValuePair("isHome", ""));
                httget.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = this.httpclient.execute(httget);
                entity = response.getEntity();
                String data = ParserUtils.responseToString(entity.getContent());
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
                    String result1 = ParserUtils.responseToString(entity.getContent());
                    Document resultDoc1 = Jsoup.parse(result1);
                    String json;
                    if (resultDoc1.getElementById("wdserror-list") != null && resultDoc1.getElementById("flightBack") != null) {
                        httget = new HttpPost("https://www.qantas.com.au/tripflowapp/awardBooking.tripflow");
                        nameValuePairs = new ArrayList();
                        nameValuePairs.add(new BasicNameValuePair("depAirports", this.origin));
                        nameValuePairs.add(new BasicNameValuePair("destAirports", this.destination));
                        nameValuePairs.add(new BasicNameValuePair("travelDates", this.date + "0000"));
                        nameValuePairs.add(new BasicNameValuePair("adults", this.seats + ""));
                        nameValuePairs.add(new BasicNameValuePair("children", "0"));
                        nameValuePairs.add(new BasicNameValuePair("infants", "0"));
                        nameValuePairs.add(new BasicNameValuePair("searchOption", "M"));
                        nameValuePairs.add(new BasicNameValuePair("tripType", "O"));
                        nameValuePairs.add(new BasicNameValuePair("travelClass", "ECO"));
                        nameValuePairs.add(new BasicNameValuePair("PAGE_FROM", "/travel/airlines/flight-search/global/en"));
                        nameValuePairs.add(new BasicNameValuePair("localeOverride", "en_AU"));
                        nameValuePairs.add(new BasicNameValuePair("ruleSetId", "i18nAwbTripflow.Main"));
                        nameValuePairs.add(new BasicNameValuePair("mloginffNumber", ""));
                        nameValuePairs.add(new BasicNameValuePair("mloginsurname", ""));
                        nameValuePairs.add(new BasicNameValuePair("mloginpin", ""));
                        nameValuePairs.add(new BasicNameValuePair("isHome", ""));
                        httget.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        response = this.httpclient.execute(httget);
                        entity = response.getEntity();
                        data = ParserUtils.responseToString(entity.getContent());
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
                            result1 = ParserUtils.responseToString(entity.getContent());
                        }
                    }

                    int firstIndex1 = result1.indexOf("new RFCOForm(") + "new RFCOForm(".length();
                    int secondIndex1 = result1.indexOf(");", firstIndex1);
                    json = result1.substring(firstIndex1, secondIndex1);
                    JSONObject flightDataJson1 = (new JSONObject(json)).getJSONObject("availability");
                    String urlConnector1 = flightDataJson1.getString("urlConnector");
                    JSONObject itenJson = flightDataJson1.getJSONArray("bounds").getJSONObject(0).getJSONObject("listItineraries").getJSONObject("itinerariesAsMap");
                    JSONObject flightJson = flightDataJson1.getJSONArray("bounds").getJSONObject(0).getJSONObject("flights");
                    JSONObject locationJson = flightDataJson1.getJSONArray("bounds").getJSONObject(0).getJSONObject("listItineraries").getJSONObject("locations");
                    Element table = resultDoc1.getElementById("idAvailabilty0");
                    this.getAwards(table, dt_format_or, itenJson, locationJson, flightJson, urlConnector1, flights, 0);
                    JSONObject itenJson1 = flightDataJson1.getJSONArray("bounds").getJSONObject(1).getJSONObject("listItineraries").getJSONObject("itinerariesAsMap");
                    JSONObject flightJson1 = flightDataJson1.getJSONArray("bounds").getJSONObject(1).getJSONObject("flights");
                    JSONObject locationJson1 = flightDataJson1.getJSONArray("bounds").getJSONObject(1).getJSONObject("listItineraries").getJSONObject("locations");
                    Element table1 = resultDoc1.getElementById("idAvailabilty1");
                    this.getAwards(table1, dt_format_or, itenJson1, locationJson1, flightJson1, urlConnector1, flights, 1);
                    System.out.println("");
                }
            } catch (SocketException var30) {
                return false;
            } catch (SocketTimeoutException var31) {
                return false;
            } catch (NoHttpResponseException var32) {
                return false;
            } catch (IllegalStateException var33) {
                return false;
            } catch (IOException var34) {
                var34.printStackTrace();
            } catch (Exception var35) {
                var35.printStackTrace();
            }

            return true;
        }

        private void getAwards(Element table, SimpleDateFormat dt_format_or, JSONObject itenJson, JSONObject locationJson, JSONObject flightJson, String urlConnector, List<Trip> flights, int direction) throws ParseException, IllegalStateException, JSONException, IOException, InterruptedException {
            int awardIndex = 0;
            boolean newAward = false;

            for (Iterator var13 = table.getElementsByTag("tbody").iterator(); var13.hasNext(); ++awardIndex) {
                Element tbody = (Element) var13.next();
                Trip award = new Trip();
                newAward = true;
                String stringIndex = tbody.attr("id").split("_")[2];
                int fligtIndex = 0;

                try {
                    if (stringIndex != null) {
                        fligtIndex = Integer.parseInt(stringIndex);
                    }
                } catch (NumberFormatException var26) {
                    var26.printStackTrace();
                }

                Iterator ex = tbody.select(" > tr").iterator();

                while (ex.hasNext()) {
                    Element tr = (Element) ex.next();
                    if (tr.attr("id").contains("idLine")) {
                        com.guru.domain.model.Flight flight = new com.guru.domain.model.Flight();
                        Elements tdList = tr.select(" > td");
                        Elements tdhList = tr.select(" > th");
                        if (newAward) {
                            tdhList.get(1).getElementsByClass("stops").text();
                            newAward = false;
                        }

                        award.setTripDuration(ParserUtils.getTotalTime(tdhList.get(0).getElementsByClass("duration").first().ownText(), this.qfParser));
                        String data_url = tdhList.get(2).getElementsByTag("a").first().attr("data-url");
                        flight.setUrl(data_url);
                        Elements thList = table.select(" > thead").first().getElementsByTag("th");
                        ClasInfo fInfo;
                        if (thList.size() > 3 && tdList.size() > 0) {
                            if (thList.get(3).attr("id").contains("PECO")) {
                                fInfo = this.getInfo(tdList, 0, "SaverEconomy", "E");
                                if (!fInfo.na) {
                                    fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "PECO", this.httpclient));
                                }
                                award.getClasInfo().add(fInfo);
                            } else if (thList.get(3).attr("id").contains("ECO")) {
                                fInfo = this.getInfo(tdList, 0, "SaverEconomy", "E");
                                if (!fInfo.na) {
                                    fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "ECO", this.httpclient));
                                }
                                award.getClasInfo().add(fInfo);
                            } else if (thList.get(3).attr("id").contains("BUS")) {
                                fInfo = this.getInfo(tdList, 0, "SaverBusiness", "B");
                                if (!fInfo.na) {
                                    fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "BUS", this.httpclient));
                                }
                                award.getClasInfo().add(fInfo);
                            } else if (thList.get(3).attr("id").contains("FIR")) {
                                fInfo = this.getInfo(tdList, 0, "SaverFirst", "F");
                                if (!fInfo.na) {
                                    fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "FIR", this.httpclient));
                                }
                                award.getClasInfo().add(fInfo);
                            }
                        }

                        if (thList.size() > 4 && tdList.size() > 1) {
                            if (thList.get(4).attr("id").contains("PECO")) {
                                fInfo = this.getInfo(tdList, 1, "SaverEconomy", "E");
                                if (!fInfo.na) {
                                    fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "PECO", this.httpclient));
                                }
                                award.getClasInfo().add(fInfo);
                            } else if (thList.get(4).attr("id").contains("ECO")) {
                                fInfo = this.getInfo(tdList, 1, "SaverEconomy", "E");
                                if (!fInfo.na) {
                                    fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "ECO", this.httpclient));
                                }
                                award.getClasInfo().add(fInfo);
                            } else if (thList.get(4).attr("id").contains("BUS")) {
                                fInfo = this.getInfo(tdList, 1, "SaverBusiness", "B");
                                if (!fInfo.na) {
                                    fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "BUS", this.httpclient));
                                }
                                award.getClasInfo().add(fInfo);
                            } else if (thList.get(4).attr("id").contains("FIR")) {
                                fInfo = this.getInfo(tdList, 1, "SaverFirst", "F");
                                if (!fInfo.na) {
                                    fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "FIR", this.httpclient));
                                }
                                award.getClasInfo().add(fInfo);
                            }
                        }

                        if (thList.size() > 5 && tdList.size() > 2) {
                            if (thList.get(5).attr("id").contains("PECO")) {
                                fInfo = this.getInfo(tdList, 2, "SaverEconomy", "E");
                                if (!fInfo.na) {
                                    fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "PECO", this.httpclient));
                                }
                                award.getClasInfo().add(fInfo);
                            } else if (thList.get(5).attr("id").contains("ECO")) {
                                fInfo = this.getInfo(tdList, 2, "SaverEconomy", "E");
                                if (!fInfo.na) {
                                    fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "ECO", this.httpclient));
                                }
                                award.getClasInfo().add(fInfo);

                            } else if (thList.get(5).attr("id").contains("BUS")) {
                                fInfo = this.getInfo(tdList, 2, "SaverBusiness", "B");
                                if (!fInfo.na) {
                                    fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "BUS", this.httpclient));
                                }
                                award.getClasInfo().add(fInfo);

                            } else if (thList.get(5).attr("id").contains("FIR")) {
                                fInfo = this.getInfo(tdList, 2, "SaverFirst", "F");
                                if (!fInfo.na) {
                                    fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "FIR", this.httpclient));
                                }
                                award.getClasInfo().add(fInfo);
                            }
                        }

                        if (thList.size() > 6 && tdList.size() > 3) {
                            if (thList.get(6).attr("id").contains("PECO")) {
                                fInfo = this.getInfo(tdList, 3, "SaverEconomy", "E");
                                if (!fInfo.na) {
                                    fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "PECO", this.httpclient));
                                }
                                award.getClasInfo().add(fInfo);
                            } else if (thList.get(6).attr("id").contains("ECO")) {
                                fInfo = this.getInfo(tdList, 3, "SaverEconomy", "E");
                                if (!fInfo.na) {
                                    fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "ECO", this.httpclient));
                                }
                                award.getClasInfo().add(fInfo);
                            } else if (thList.get(6).attr("id").contains("BUS")) {
                                fInfo = this.getInfo(tdList, 3, "SaverBusiness", "B");
                                if (!fInfo.na) {
                                    fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "BUS", this.httpclient));
                                }
                                award.getClasInfo().add(fInfo);
                            } else if (thList.get(6).attr("id").contains("FIR")) {
                                fInfo = this.getInfo(tdList, 3, "SaverFirst", "F");
                                if (!fInfo.na) {
                                    fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "FIR", this.httpclient));
                                }
                                award.getClasInfo().add(fInfo);
                            }
                        }

                        award.getFlights().add(flight);
                    } else {
                        award.setTripDuration(ParserUtils.getTotalTime(tr.getElementsByClass("total-duration").text(), this.qfParser));
                    }
                }

                award.setDirection(direction);
                flights.add(award);
            }

        }

        private ClasInfo getInfo(Elements tdList, int index, String name, String reduction) {
            ClasInfo info = new ClasInfo();
            info.setName(name);
            info.setNa(true);
            info.setReduction(reduction);
            info.setStatus(0);
            if (!tdList.get(index).getElementsByTag("i").isEmpty() && tdList.get(index).getElementsByTag("i").attr("data-title").contains("will be in")) {
                info.setNa(false);
                info.setStatus(1);
                info.setMixed(true);
                String mixed = tdList.get(index).getElementsByTag("i").text();
                mixed = mixed.replaceAll("Your flight ", "");
                mixed = mixed.replaceAll(" will be in", ":");
                String[] array = mixed.split("from");

                for (int i = 1; i < array.length; ++i) {
                    info.getMixedCabins().add(array[i]);
                }
            } else {
                if (tdList.get(index).getElementsByTag("div").first().text().contains("Seat Available")) {
                    info.setNa(false);
                    info.setStatus(1);
                }

                tdList.get(index).getElementsByTag("div").first().text().contains("No Seats");
            }

            return info;
        }

        private String getMileLink(JSONObject itenJson, JSONObject locationJson, JSONObject flightJson, String urlConnector, int awardIndex, String cabin, DefaultHttpClient httpclient) throws IOException, InterruptedException {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmm");
            JSONArray segmentArray = itenJson.getJSONObject(awardIndex + "").getJSONArray("segments");
            String SECTORS = "";
            String SEGMENT_ID = "";
            String FLIGHT_NUMBERS = "";
            String AIRLINE_CODES = "";
            String OPERATING_CODES = "*,*";
            String NB_STOPS = "";
            String DATES = "";
            String CLASSES = "";
            String REQUEST_ID = "10" + awardIndex;
            boolean sLast = false;

            String urlMiles;
            for (int rdbsArray = 0; rdbsArray < segmentArray.length(); ++rdbsArray) {
                if (rdbsArray == segmentArray.length() - 1) {
                    sLast = true;
                }

                if (sLast) {
                    OPERATING_CODES = OPERATING_CODES + "*";
                    SEGMENT_ID = SEGMENT_ID + rdbsArray + "";
                } else {
                    OPERATING_CODES = OPERATING_CODES + "*,";
                    SEGMENT_ID = SEGMENT_ID + rdbsArray + ",";
                }

                Long rdbs = Long.valueOf(segmentArray.getJSONObject(rdbsArray).getLong("beginDate"));
                Long rdbsLast = Long.valueOf(segmentArray.getJSONObject(rdbsArray).getLong("endDate"));
                urlMiles = segmentArray.getJSONObject(rdbsArray).getString("flightNumber");
                Integer nbrOfStops = Integer.valueOf(segmentArray.getJSONObject(rdbsArray).getInt("nbrOfStops"));
                String cachedAirlineCode = segmentArray.getJSONObject(rdbsArray).getString("cachedAirlineCode");
                String beginLocationCode = segmentArray.getJSONObject(rdbsArray).getString("beginLocationCode");
                String endLocationCode = segmentArray.getJSONObject(rdbsArray).getString("endLocationCode");
                String bLocation = locationJson.getJSONObject(beginLocationCode).getString("locationCode");
                String eLocation = locationJson.getJSONObject(endLocationCode).getString("locationCode");
                Date d1 = new Date(rdbs.longValue());
                Date d2 = new Date(rdbsLast.longValue());
                String bDate = sdf.format(d1);
                String eDate = sdf.format(d2);
                SECTORS = SECTORS + bLocation + "/" + eLocation + (!sLast ? "," : "");
                FLIGHT_NUMBERS = FLIGHT_NUMBERS + urlMiles + (!sLast ? "," : "");
                AIRLINE_CODES = AIRLINE_CODES + cachedAirlineCode + (!sLast ? "," : "");
                NB_STOPS = NB_STOPS + nbrOfStops + (!sLast ? "," : "");
                DATES = DATES + bDate + "/" + eDate + (!sLast ? "," : "");
            }

            JSONArray var34 = flightJson.getJSONObject(awardIndex + "").getJSONObject("listRecommendation").getJSONObject(cabin + "A").getJSONArray("rbds");
            String var35 = "";
            boolean var36 = false;

            for (int var37 = 0; var37 < var34.length(); ++var37) {
                if (var37 == var34.length() - 1) {
                    var36 = true;
                }

                var35 = var35 + var34.getString(var37) + (!var36 ? "," : "");
            }

            urlMiles = urlConnector + "&SECTORS=" + SECTORS + "&SEGMENT_ID=" + SEGMENT_ID + "&FLIGHT_NUMBERS=" + FLIGHT_NUMBERS + "&AIRLINE_CODES=" + AIRLINE_CODES + "&OPERATING_CODES=" + OPERATING_CODES + "&NB_STOPS=" + NB_STOPS + "&DATES=" + DATES + "&CLASSES=" + var35 + "&REQUEST_ID=" + REQUEST_ID;
            return urlMiles;
        }
    }

    public class DataThread implements Callable<Trip> {
        private Trip award;
        private DefaultHttpClient httpclient;
        private int counter = 0;

        public DataThread(Trip award, DefaultHttpClient httpclient) {
            this.award = award;
            this.httpclient = httpclient;
        }

        public Trip call() throws IOException, Exception {
            SimpleDateFormat format = new SimpleDateFormat("EEE dd MMM yy", Locale.US);
            DefaultHttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter("http.protocol.cookie-policy", "compatibility");
            httpclient.setCookieStore(new BasicCookieStore());
            httpclient.setRedirectStrategy(new LaxRedirectStrategy());

            // QFParser.setClientProxyProperties(httpclient, account);


            httpclient.getParams().setParameter("http.useragent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
            Iterator sdf = this.httpclient.getCookieStore().getCookies().iterator();

            while (sdf.hasNext()) {
                Cookie dt_format_or = (Cookie) sdf.next();
                httpclient.getCookieStore().addCookie(dt_format_or);
            }

            Iterator iterator = this.award.getFlights().iterator();

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
            for (ClasInfo info : award.getClasInfo()) {
                clasInfos.add(getMiles(info.getUrl(), info));
            }
            award.setClasInfo(clasInfos);

            return this.award;
        }

        private ClasInfo getMiles(String urlMiles, ClasInfo info) throws IOException {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter("http.protocol.cookie-policy", "compatibility");
            CookieStore cookieStore = new BasicCookieStore();

            // Bind custom cookie store to the local context
            httpclient.setCookieStore(cookieStore);
            CookieSpecFactory csf = new CookieSpecFactory() {
                public CookieSpec newInstance(HttpParams params) {
                    return new BrowserCompatSpec() {
                        @Override
                        public void validate(Cookie cookie, CookieOrigin origin)
                                throws MalformedCookieException {
                            // Oh, I am easy.
                            // Allow all cookies
                            //  log.debug("custom validate");
                        }
                    };
                }
            };
            httpclient.getCookieSpecs().register("easy", csf);
            httpclient.getParams().setParameter(
                    ClientPNames.COOKIE_POLICY, "easy");
            httpclient.setRedirectStrategy(new LaxRedirectStrategy());
            httpclient.getParams().setParameter("http.useragent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
            Iterator httGet = this.httpclient.getCookieStore().getCookies().iterator();

            Cookie response;
            while (httGet.hasNext()) {
                response = (Cookie) httGet.next();
                httpclient.getCookieStore().addCookie(response);
            }

            httpclient.setCredentialsProvider(this.httpclient.getCredentialsProvider());
            httpclient.getParams().setParameter("http.route.default-proxy", this.httpclient.getParams().getParameter("http.route.default-proxy"));
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
                    info.setMileage(miles);
                } else {
                    info.setMileage("");
                }
            }
            return info;
        }
    }

}
