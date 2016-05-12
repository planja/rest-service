package com.guru.parser.impl.qfparser;

import com.guru.domain.model.ClasInfo;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            String result1 = IOUtils.toString(entity.getContent());
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
                data = IOUtils.toString(entity.getContent());
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
                    result1 = IOUtils.toString(entity.getContent());
                }
            }
            SimpleDateFormat dt_format_or = new SimpleDateFormat("EEE dd MMM yy HH:mm", Locale.US);


            int firstIndex1 = result1.indexOf("new RFCOForm(") + "new RFCOForm(".length();
            int secondIndex1 = result1.indexOf(");", firstIndex1);
            json = result1.substring(firstIndex1, secondIndex1);
             try{
                 JSONObject flightDataJson1 = (new JSONObject(json)).getJSONObject("availability");
                 String urlConnector1 = flightDataJson1.getString("urlConnector");
                 JSONObject itenJson = flightDataJson1.getJSONArray("bounds").getJSONObject(0).getJSONObject("listItineraries").getJSONObject("itinerariesAsMap");
                 JSONObject flightJson = flightDataJson1.getJSONArray("bounds").getJSONObject(0).getJSONObject("flights");
                 JSONObject locationJson = flightDataJson1.getJSONArray("bounds").getJSONObject(0).getJSONObject("listItineraries").getJSONObject("locations");
                 Element table = resultDoc1.getElementById("idAvailabilty0");
                 this.getAwards(table, dt_format_or, itenJson, locationJson, flightJson, urlConnector1, flights, 0);
             }catch (JSONException j){
                 return true;
             }
             /* try{
                JSONObject itenJson1 = flightDataJson1.getJSONArray("bounds").getJSONObject(1).getJSONObject("listItineraries").getJSONObject("itinerariesAsMap");
                JSONObject flightJson1 = flightDataJson1.getJSONArray("bounds").getJSONObject(1).getJSONObject("flights");
                JSONObject locationJson1 = flightDataJson1.getJSONArray("bounds").getJSONObject(1).getJSONObject("listItineraries").getJSONObject("locations");
                Element table1 = resultDoc1.getElementById("idAvailabilty1");
                this.getAwards(table1, dt_format_or, itenJson1, locationJson1, flightJson1, urlConnector1, flights, 1);
            }catch (JSONException j){
                return true;
            }*/


        }


        return true;
    }

    private void getAwards(Element table, SimpleDateFormat dt_format_or, JSONObject itenJson, JSONObject locationJson, JSONObject flightJson, String urlConnector, List<Trip> flights, int direction) throws ParseException, IllegalStateException, JSONException, IOException, InterruptedException {
        int awardIndex = 0;
        boolean newTrip;
        if(table==null){
            System.out.println("table null");
            return;
        }

        for (Iterator var13 = table.getElementsByTag("tbody").iterator(); var13.hasNext(); ++awardIndex) {
            Element tbody = (Element) var13.next();
            Trip trip = new Trip();
            newTrip = true;
            String stringIndex = tbody.attr("id").split("_")[2];
            int fligtIndex = 0;

            try {
                if(stringIndex != null) {
                    fligtIndex = Integer.parseInt(stringIndex);
                }
            } catch (NumberFormatException var26) {
                var26.printStackTrace();
            }
            Iterator ex = tbody.select(" > tr").iterator();

            while (ex.hasNext()) {
                Element tr = (Element) ex.next();
                if (tr.attr("id").contains("idLine")) {
                    Flight flight = new Flight();
                    Elements tdhList = tr.select(" > th");
                    Elements tdList = tr.select(" > td");
                    if (newTrip) {
                        tdhList.get(1).getElementsByClass("stops").text();
                        newTrip = false;
                    }
                    trip.setTripDuration(ParserUtils.getTotalTime(tdhList.get(0).getElementsByClass("duration").first().ownText(), this.qfParser));
                    String data_url = tdhList.get(2).getElementsByTag("a").first().attr("data-url");
                    flight.setUrl(data_url);


                    Elements thList = table.select(" > thead").first().getElementsByTag("th");
                    ClasInfo fInfo;
                    if(thList.size() > 3 && tdList.size() > 0) {
                        if(thList.get(3).attr("id").contains("PECO")) {
                            fInfo = this.getInfo(tdList, 0,"SaverPremium","P");
                            if(!fInfo.na) {
                                fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "PECO", this.httpclient));
                            }
                            trip.getClasInfo().add(fInfo);
                        } else if(thList.get(3).attr("id").contains("ECO")) {
                            fInfo = this.getInfo(tdList, 0,"SaverEconomy","E");
                            if(!fInfo.na) {
                                fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "ECO", this.httpclient));
                            }
                            trip.getClasInfo().add(fInfo);
                        } else if(thList.get(3).attr("id").contains("BUS")) {
                            fInfo = this.getInfo(tdList, 0,"SaverBusiness","B");
                            if(!fInfo.na) {
                                fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "BUS", this.httpclient));
                            }
                            trip.getClasInfo().add(fInfo);

                        } else if(thList.get(3).attr("id").contains("FIR")) {
                            fInfo = this.getInfo(tdList, 0,"SaverFirst","F");
                            if(!fInfo.na) {
                                fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "FIR", this.httpclient));
                            }
                            trip.getClasInfo().add(fInfo);

                        }
                    }

                    if(thList.size() > 4 && tdList.size() > 1) {
                        if(thList.get(4).attr("id").contains("PECO")) {
                            fInfo = this.getInfo(tdList, 1,"SaverPremium","P");
                            if(!fInfo.na) {
                                fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "PECO", this.httpclient));
                            }
                            trip.getClasInfo().add(fInfo);

                        } else if(thList.get(4).attr("id").contains("ECO")) {
                            fInfo = this.getInfo(tdList, 1,"SaverEconomy","E");
                            if(!fInfo.na) {
                                fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "ECO", this.httpclient));
                            }
                            trip.getClasInfo().add(fInfo);

                        } else if(thList.get(4).attr("id").contains("BUS")) {
                            fInfo = this.getInfo(tdList, 1,"SaverBusiness","B");
                            if(!fInfo.na) {
                                fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "BUS", this.httpclient));
                            }
                            trip.getClasInfo().add(fInfo);

                        } else if(thList.get(4).attr("id").contains("FIR")) {
                            fInfo = this.getInfo(tdList, 1,"SaverFirs","F");
                            if(!fInfo.na) {
                                fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "FIR", this.httpclient));
                            }
                            trip.getClasInfo().add(fInfo);

                        }
                    }

                    if(thList.size() > 5 && tdList.size() > 2) {
                        if(thList.get(5).attr("id").contains("PECO")) {
                            fInfo = this.getInfo(tdList, 2,"SaverPremium","P");
                            if(!fInfo.na) {
                                fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "PECO", this.httpclient));
                            }
                            trip.getClasInfo().add(fInfo);

                        } else if(thList.get(5).attr("id").contains("ECO")) {
                            fInfo = this.getInfo(tdList, 2,"SaverEconomy","E");
                            if(!fInfo.na) {
                                fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "ECO", this.httpclient));
                            }
                            trip.getClasInfo().add(fInfo);

                        } else if(thList.get(5).attr("id").contains("BUS")) {
                            fInfo = this.getInfo(tdList, 2,"SaverBusiness","B");
                            if(!fInfo.na) {
                                fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "BUS", this.httpclient));
                            }
                            trip.getClasInfo().add(fInfo);

                        } else if(thList.get(5).attr("id").contains("FIR")) {
                            fInfo = this.getInfo(tdList, 2,"SaverFirst","F");
                            if(!fInfo.na) {
                                fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "FIR", this.httpclient));
                            }
                            trip.getClasInfo().add(fInfo);

                        }
                    }

                    if(thList.size() > 6 && tdList.size() > 3) {
                        if(thList.get(6).attr("id").contains("PECO")) {
                            fInfo = this.getInfo(tdList, 3,"SaverPremium","P");
                            if(!fInfo.na) {
                                fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "PECO", this.httpclient));
                            }
                            trip.getClasInfo().add(fInfo);

                        } else if(thList.get(6).attr("id").contains("ECO")) {
                            fInfo = this.getInfo(tdList, 3,"SaverEconomy","E");
                            if(!fInfo.na) {
                                fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "ECO", this.httpclient));
                            }
                            trip.getClasInfo().add(fInfo);

                        } else if(thList.get(6).attr("id").contains("BUS")) {
                            fInfo = this.getInfo(tdList, 3,"SaverBusiness","B");
                            if(!fInfo.na) {
                                fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "BUS", this.httpclient));
                            }
                            trip.getClasInfo().add(fInfo);

                        } else if(thList.get(6).attr("id").contains("FIR")) {
                            fInfo = this.getInfo(tdList, 3,"SaverFirst","F");
                            if(!fInfo.na) {
                                fInfo.setUrl(this.getMileLink(itenJson, locationJson, flightJson, urlConnector, fligtIndex, "FIR", this.httpclient));
                            }
                            trip.getClasInfo().add(fInfo);

                        }
                    }

                    trip.getFlights().add(flight);
                } else {
                    trip.setTripDuration(ParserUtils.getTotalTime(tr.getElementsByClass("total-duration").text(), this.qfParser));
                }
            }
            trip.setDirection(direction);
            flights.add(trip);
        }

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
        for(int rdbsArray = 0; rdbsArray < segmentArray.length(); ++rdbsArray) {
            if(rdbsArray == segmentArray.length() - 1) {
                sLast = true;
            }

            if(sLast) {
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
            SECTORS = SECTORS + bLocation + "/" + eLocation + (!sLast?",":"");
            FLIGHT_NUMBERS = FLIGHT_NUMBERS + urlMiles + (!sLast?",":"");
            AIRLINE_CODES = AIRLINE_CODES + cachedAirlineCode + (!sLast?",":"");
            NB_STOPS = NB_STOPS + nbrOfStops + (!sLast?",":"");
            DATES = DATES + bDate + "/" + eDate + (!sLast?",":"");
        }

        JSONArray var34 = flightJson.getJSONObject(awardIndex + "").getJSONObject("listRecommendation").getJSONObject(cabin + "A").getJSONArray("rbds");
        String var35 = "";
        boolean var36 = false;

        for(int var37 = 0; var37 < var34.length(); ++var37) {
            if(var37 == var34.length() - 1) {
                var36 = true;
            }

            var35 = var35 + var34.getString(var37) + (!var36?",":"");
        }

        urlMiles = urlConnector + "&SECTORS=" + SECTORS + "&SEGMENT_ID=" + SEGMENT_ID + "&FLIGHT_NUMBERS=" + FLIGHT_NUMBERS + "&AIRLINE_CODES=" + AIRLINE_CODES + "&OPERATING_CODES=" + OPERATING_CODES + "&NB_STOPS=" + NB_STOPS + "&DATES=" + DATES + "&CLASSES=" + var35 + "&REQUEST_ID=" + REQUEST_ID;
        return urlMiles;
    }




    private ClasInfo getInfo(Elements tdList, int index,String name,String reduction) {
        ClasInfo info = new ClasInfo();
        info.setName(name);
        info.setNa(true);
        info.setReduction(reduction);
        info.setStatus(0);
        if(!tdList.get(index).getElementsByTag("i").isEmpty() && tdList.get(index).getElementsByTag("i").attr("data-title").contains("will be in")) {
            info.setNa(false);
            info.setStatus(1);
            info.setMixed(true);
            String mixed = tdList.get(index).getElementsByTag("i").text();
            mixed = mixed.replaceAll("Your flight ", "");
            mixed = mixed.replaceAll(" will be in", ":");
            String[] array = mixed.split("from");

            for(int i = 1; i < array.length; ++i) {
                info.getMixedCabins().add(array[i]);
            }
        } else {
            if(tdList.get(index).getElementsByTag("div").first().text().contains("Seat Available")) {
                info.setNa(false);
                info.setStatus(1);
            }

            tdList.get(index).getElementsByTag("div").first().text().contains("No Seats");
        }

        return info;
    }

}