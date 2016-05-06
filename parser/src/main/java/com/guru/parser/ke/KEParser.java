package com.guru.parser.ke;

import com.guru.domain.model.Flight;
import com.guru.domain.model.MileCost;
import com.guru.domain.model.Trip;
import com.guru.domain.repository.MileCostRepository;
import com.guru.parser.interf.Parser;
import com.guru.parser.utils.ParserUtils;
import com.guru.vo.temp.Account;
import com.guru.vo.temp.AccountUtils;
import com.guru.vo.transfer.RequestData;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by Anton on 27.04.2016.
 */
@Component
public class KEParser implements Parser {

    @Inject
    private MileCostRepository mileCostRepository;


    private static final String PARSER_CODE = "KE";
    private static final HashMap<String, String> PLACES;


    static {
        PLACES = new HashMap<String, String>();
        PLACES.put("AXT", "Akita, Akita Airport");
        PLACES.put("AMS", "Amsterdam, Amsterdam Airport Schiphol");
        PLACES.put("ANC", "Anchorage, Ted Stevens Anchorage International Airport");
        PLACES.put("AOJ", "Aomori, Aomori Airport");
        PLACES.put("ATH", "Athens, Athens International Airport");
        PLACES.put("ATL", "Atlanta, Hartsfield, Jackson Atlanta International Airport");
        PLACES.put("AKL", "Auckland, Auckland International Airport");
        PLACES.put("BKK", "Bangkok, Suvarnabhumi international Airport");
        PLACES.put("BSL", "Basel, EuroAirport Basel-Mulhouse-Freiburg");
        PLACES.put("MLH", "Mulhouse, EuroAirport Basel-Mulhouse-Freiburg");
        PLACES.put("EAP", "Freiburg, EuroAirport Basel-Mulhouse-Freiburg");
        PLACES.put("PEK", "Beijing, Beijing Capital International Airport");
        PLACES.put("BNE", "Brisbane, Brisbane Airport");
        PLACES.put("BCN", "Barcelona, Barcelona - El Prat International Airport");
        PLACES.put("BRU", "Brussels, Brussels Airport");
        PLACES.put("PUS", "Busan, Gimhae International Airport");
        PLACES.put("CAI", "Cairo, Cairo International Airport");
        PLACES.put("YYC", "Calgary, Calgary International Airport");
        PLACES.put("VCP", "Campinas, Viracopos - Campinas International Airport");
        PLACES.put("CEB", "Cebu Mactan, Cebu International Airport");
        PLACES.put("CSX", "Changsha, Changsha Huanghua International Airport");
        PLACES.put("CTU", "Chengdu, Chengdu Shuangliu International Airport");
        PLACES.put("MAA", "Chennai, Chennai International Airport");
        PLACES.put("CJJ", "Cheongju, Cheongju International Airport");
        PLACES.put("CNX", "Chiang Mai, Chiang Mai International Airport");
        PLACES.put("ORD", "Chicago, Chicago O'Hare International Airport");
        PLACES.put("CRK", "Clark, Clark International Airport");
        PLACES.put("CHC", "Christchurch, Christchurch International Airport");
        PLACES.put("CMB", "Colombo, Bandaranaike International Airport");
        PLACES.put("CPH", "Copenhagen, Copenhagen Airport[ Cargo]");
        PLACES.put("DAD", "Da Nang, Da Nang International Airport");
        PLACES.put("TAE", "Daegu, Daegu International Airport");
        PLACES.put("DLC", "Dalian, Dalian Zhoushuizi International Airport");
        PLACES.put("DFW", "Dallas, Dallas/Fort Worth International Airport");
        PLACES.put("DEL", "Delhi, Indira Gandhi International Airport");
        PLACES.put("DPS", "Denpasar, Ngurah Rai International Airport");
        PLACES.put("DAC", "Dhaka, Shahjalal International Airport");
        PLACES.put("DXB", "Dubai, Dubai International Airport");
        PLACES.put("FRA", "Frankfurt, Frankfurt Airport");
        PLACES.put("FUK", "Fukuoka, Fukuoka Airport");
        PLACES.put("GLA", "Glasgow, Glasgow Airport");
        PLACES.put("GDL", "Guadalajara, Miguel Hidalgo y Costilla International Airport");
        PLACES.put("KWE", "Guiyang, Guiyang Longdongbao Airport");
        PLACES.put("GUM", "Guam, Antonio B.Won Pat International Airport");
        PLACES.put("CAN", "Guangzhou, Guangzhou Baiyun International Airport");
        PLACES.put("KWE", "Guiyang, Guiyang Longdongbao Airport");
        PLACES.put("KUV", "Gunsan, Gunsan Airport");
        PLACES.put("HKD", "Hakodate, Hakodate Airport");
        PLACES.put("YHZ", "Halifax, Halifax International Airport");
        PLACES.put("HGH", "Hangzhou, Hangzhou Xiaoshan International Airport");
        PLACES.put("HAN", "Hanoi, Noi Bai International Airport");
        PLACES.put("HFE", "Hefei, Hefei Xinqiao International Airport");
        PLACES.put("SGN", "Ho Chi Minh City, Tan Son Nhat International Airport");
        PLACES.put("HKG", "Hong Kong, Hong Kong International Airport");
        PLACES.put("HNL", "Honolulu, Honolulu International Airport");
        PLACES.put("IAH", "Houston, George Bush Intercontinental Airport");
        PLACES.put("IKT", "Irkutsk, Irkutsk International Airport");
        PLACES.put("IST", "Istanbul, Istanbul Atat?rk International Airport");
        PLACES.put("CGK", "Jakarta, Soekarno–Hatta International Airport");
        PLACES.put("JED", "Jeddah, King Abdulaziz International Airport");
        PLACES.put("CJU", "Jeju, Jeju International Airport");
        PLACES.put("TNA", "Jinan, Jinan Yaoqiang International Airport");
        PLACES.put("HIN", "Jinju, Sacheon Airport");
        PLACES.put("KHH", "Kaohsiung, Kaohsiung International Airport");
        PLACES.put("KOJ", "Kagoshima, Kagoshima Airport");
        PLACES.put("KTM", "Kathmandu, Tribhuvan International Airport");
        PLACES.put("KMQ", "Komatsu/Kanazawa, Komatsu Airport");
        PLACES.put("ROR", "Koror, Roman Tmetuchl International Airport");
        PLACES.put("KHH", "Kaohsiung, Kaohsiung International Airport");
        PLACES.put("BKI", "Kota Kinabalu, Kota Kinabalu International Airport");
        PLACES.put("KUL", "Kuala Lumpur, Kuala Lumpur International Airport");
        PLACES.put("KMG", "Kunming, Kunming Changshui International Airport");
        PLACES.put("KBV", "Krabi, Krabi Airport");
        PLACES.put("LAS", "Las Vegas, McCarran International Airport");
        PLACES.put("LIM", "Lima, Jorge Ch?vez International Airport");
        PLACES.put("LHR", "London, London Heathrow International Airport");
        PLACES.put("LGW", "London, London Gatwick International Airport");
        PLACES.put("STN", "London, London Stansted Airport");
        PLACES.put("LAX", "Los Angeles, Los Angeles International Airport");
        PLACES.put("MAD", "Madrid Madrid–Barajas International Airport");
        PLACES.put("MLE", "Mal?, Ibrahim Nasir International Airport");
        PLACES.put("MNL", "Manila, Ninoy Aquino International Airport");
        PLACES.put("MFM", "Macau, Macau International Airport");
        PLACES.put("MRS", "Marseille, Marseille Provence Airport");
        PLACES.put("MIA", "Miami, Miami International Airport");
        PLACES.put("MXP", "Milan, Milan Malpensa Airport");
        PLACES.put("SVO", "Moscow, Sheremetyevo International Airport");
        PLACES.put("MDG", "Mudanjiang, Mudanjiang Airport");
        PLACES.put("BOM", "Mumbai, Chhatrapati Shivaji International Airport");
        PLACES.put("NAN", "Nadi, Nadi International Airport");
        PLACES.put("NGS", "Nagasaki, Nagasaki Airport");
        PLACES.put("NGO", "Nagoya, Ch?bu Centrair International Airport");
        PLACES.put("NNG", "Nanning, Nanning Wuxu International Airport");
        PLACES.put("NKG", "Nanjing, Lukou International Airport");
        PLACES.put("NVI", "Navoi, Navoi International Airport");
        PLACES.put("JFK", "New York City, John F.Kennedy International Airport");
        PLACES.put("CRX", "Nha Trang, Cam Ranh International Airport");
        PLACES.put("KIJ", "Niigata, Niigata Airport");
        PLACES.put("OIT", "Oita, Oita Airport");
        PLACES.put("OKA", "Okinawa, Naha Airport");
        PLACES.put("OKJ", "Okayama, Okayama Airport");
        PLACES.put("KIX", "Osaka, Kansai International Airport");
        PLACES.put("OSL", "Oslo, Oslo Gardermoen Airport");
        PLACES.put("CDG", "Paris, Paris - Charles de Gaulle International Airport");
        PLACES.put("PEN", "Penang, Penang International Airport");
        PLACES.put("PMO", "Palermo, Falcone–Borsellino Airport");
        PLACES.put("PNH", "Phnom Penh, Phnom Penh International Airport");
        PLACES.put("HKT", "Phuket, Phuket International Airport");
        PLACES.put("KPO", "Pohang, Pohang Airport");
        PLACES.put("PRG", "Prague, V?clav Havel Airport Prague");
        PLACES.put("TAO", "Qingdao, Qingdao Liuting International Airport");
        PLACES.put("RUH", "Riyadh, King Khalid International Airport");
        PLACES.put("FCO", "Rome, Leonardo da Vinci - Fiumicino International Airport");
        PLACES.put("LED", "Saint Petersburg, Pulkovo International Airport");
        PLACES.put("SFO", "San Francisco, San Francisco International Airport");
        PLACES.put("SCL", "Santiago, Comodoro Arturo Merino Ben?tez International Airport");
        PLACES.put("SYX", "Sanya, Sanya Phoenix International Airport");
        PLACES.put("GRU", "S?o Paulo, S?o Paulo - Guarulhos International Airport");
        PLACES.put("CTS", "Sapporo, New Chitose Airport");
        PLACES.put("SEA", "Seattle, Seattle - Tacoma International Airport");
        PLACES.put("GMP", "Seoul, Gimpo International Airport");
        PLACES.put("ICN", "Seoul, Incheon International Airport");
        PLACES.put("SHA", "Shanghai, Shanghai Hongqiao International Airport");
        PLACES.put("PVG", "Shanghai, Shanghai Pudong International Airport");
        PLACES.put("SHE", "Shenyang, Shenyang Taoxian International Airport");
        PLACES.put("SZX", "Shenzhen, Shenzhen Bao 'an International Airport");
        PLACES.put("FSZ", "Shizuoka, Shizuoka Airport");
        PLACES.put("REP", "Siem Reap, Siem Reap International Airport");
        PLACES.put("SIN", "Singapore, Singapore Changi International Airport");
        PLACES.put("ARN", "Stockholm, Stockholm - Arlanda Airport");
        PLACES.put("SYD", "Sydney, Sydney Kingsford -Smith International Airport");
        PLACES.put("TPE", "Taipei, Taoyuan International Airport");
        PLACES.put("RMQ", "Taichung, Taichung International Airport");
        PLACES.put("TAS", "Tashkent, Tashkent International Airport");
        PLACES.put("TLV", "Tel Aviv, Ben Gurion International Airport");
        PLACES.put("TSN", "Tianjin, Binhai International Airport");
        PLACES.put("HND", "Tokyo, Haneda International Airport");
        PLACES.put("NRT", "Tokyo, Narita International Airport");
        PLACES.put("YYZ", "Toronto, Toronto Pearson International Airport");
        PLACES.put("ULN", "Ulan Bator, Chinggis Khaan International Airport");
        PLACES.put("USN", "Ulsan, Ulsan Airport");
        PLACES.put("URC", "Urumqi, Diwopu International Airport");
        PLACES.put("YVR", "Vancouver, Vancouver International Airport");
        PLACES.put("VIE", "Vienna, Vienna International Airport");
        PLACES.put("VVO", "Vladivostok, Vladivostok International Airport");
        PLACES.put("VTE", "Vientiane, Wattay International Airport");
        PLACES.put("IAD", "Washington D.C.,Washington Dulles International Airport");
        PLACES.put("WEH", "Weihai, Weihai Dashuibo Airport");
        PLACES.put("WJU", "Wonju, Wonju Airport");
        PLACES.put("WUH", "Wuhan, Wuhan Tianhe International Airport");
        PLACES.put("XIY", "Xi'an, Xi'an Xianyang International Airport");
        PLACES.put("XMN", "Xiamen, Xiamen Gaoqi International Airport");
        PLACES.put("XNN", "Xining, Xining Airport");
        PLACES.put("RGN", "Yangon, Yangon International Airport");
        PLACES.put("YNJ", "Yanji, Yanji Chaoyangchuan Airport");
        PLACES.put("RSU", "Yeosu, Yeosu Airport");
        PLACES.put("ZAG", "Zagreb, Zagreb International Airport");
        PLACES.put("ZAZ", "Zaragoza, Zaragoza Airport");
        PLACES.put("CGO", "Zhengzhou, Zhengzhou Xinzheng International Airport");
        PLACES.put("ZRH", "Z?rich, Zurich Airport");
        PLACES.put("AUH", "Abu Dhabi, Abu Dhabi International Airport");
    }

    public static DefaultHttpClient login() throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        httpclient.setCookieStore(new BasicCookieStore());
        httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
        HttpResponse response = null;
        Account account = AccountUtils.getAccount("KE");
        HttpEntity entity = null;
        HttpGet httpGet = new HttpGet("https://www.koreanair.com/global/en.html");
        response = httpclient.execute(httpGet);
        entity = response.getEntity();
        String html = ParserUtils.responseToString(entity.getContent());
        httpGet = new HttpGet("https://www.koreanair.com/content/koreanair-admin/cross-region/en/login-config/jcr:content/par/login_0.html");
        response = httpclient.execute(httpGet);
        entity = response.getEntity();
        html = ParserUtils.responseToString(entity.getContent());
        HttpPost httpPost = new HttpPost("https://www.koreanair.com/api/skypass");
        httpPost.setEntity(new StringEntity("{\"password\":\"" + account.getPassword() + "\",\"username\":\"" + account.getLogin() + "\"}",
                ContentType.create("application/json")));
        response = httpclient.execute(httpPost);
        if (response.getStatusLine().toString().contains("500")) {
            return null;
        }
        entity = response.getEntity();
        html = ParserUtils.responseToString(entity.getContent());
        Document loadingDoc = Jsoup.parse(html);
        if (loadingDoc.title().equals("Service Unavailable")) {
            login();
        }
        return httpclient;
    }

    public List<Trip> getKE(int requestId, String origin, String destination, String date, int seats, String cabin, DefaultHttpClient client) throws Exception {
        if (client == null)
            return new ArrayList<Trip>();
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        httpclient.setCookieStore(new BasicCookieStore());
        httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
        for (Cookie cookie : client.getCookieStore().getCookies()) {
            httpclient.getCookieStore().addCookie(cookie);
        }
        HttpResponse response = null;
        HttpEntity entity = null;
        HttpPost httpPost = new HttpPost("https://www.koreanair.com/api/TealeafTarget.jsp");
        response = httpclient.execute(httpPost);
        entity = response.getEntity();
        String html = ParserUtils.responseToString(entity.getContent());
        HttpGet httpGet = new HttpGet("https://www.koreanair.com/global/en/booking/booking-gate.html");
        response = httpclient.execute(httpGet);
        entity = response.getEntity();
        html = ParserUtils.responseToString(entity.getContent());

        String url = "https://www.koreanair.com/api/fly/award/from/" + origin + "/to/" + destination + "/on/" + date + "?";
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        switch (cabin) {
            case "E":
                cabin = "ECONOMY";
                break;
            case "B":
                cabin = "PRESTIGE";
                break;
            case "F":
                cabin = "FIRST";
                break;
            default:
                return new ArrayList<Trip>();
        }
        nameValuePairs.add(new BasicNameValuePair("flexDays", "0"));
        nameValuePairs.add(new BasicNameValuePair("scheduleDriven", "false"));
        nameValuePairs.add(new BasicNameValuePair("purchaseThirdPerson", "false"));
        nameValuePairs.add(new BasicNameValuePair("domestic", "false"));
        nameValuePairs.add(new BasicNameValuePair("isUpgradeableCabin", "false"));
        nameValuePairs.add(new BasicNameValuePair("currency", "USD"));
        nameValuePairs.add(new BasicNameValuePair("adults", "1"));
        nameValuePairs.add(new BasicNameValuePair("children", "0"));
        nameValuePairs.add(new BasicNameValuePair("infants", "0"));
        nameValuePairs.add(new BasicNameValuePair("cabinClass", cabin));
        nameValuePairs.add(new BasicNameValuePair("adultDiscounts", ""));
        nameValuePairs.add(new BasicNameValuePair("adultInboundDiscounts", ""));
        nameValuePairs.add(new BasicNameValuePair("childDiscounts", ""));
        nameValuePairs.add(new BasicNameValuePair("childInboundDiscounts", ""));
        nameValuePairs.add(new BasicNameValuePair("infantDiscounts", ""));
        nameValuePairs.add(new BasicNameValuePair("infantInboundDiscounts", ""));
        nameValuePairs.add(new BasicNameValuePair("_", new Date().getTime() + ""));
        url = url + URLEncodedUtils.format(nameValuePairs, "utf-8");
        httpGet = new HttpGet(url);

        response = httpclient.execute(httpGet);
        entity = response.getEntity();
        html = ParserUtils.responseToString(entity.getContent());
        List<Trip> resultList = new ArrayList<>();
        if (!isJSONValid(html)) {
            return resultList;
        }
        JSONObject jsonObj = new JSONObject(html);

        JSONArray outBound = jsonObj.getJSONArray("outbound");
        for (int i = 0; i < outBound.length(); i++) {
            if (cabin.length() == 1) {
                switch (cabin) {
                    case "E":
                        cabin = "ECONOMY";
                        break;
                    case "B":
                        cabin = "PRESTIGE";
                        break;
                    case "F":
                        cabin = "FIRST";
                        break;
                    default:
                        return new ArrayList<Trip>();
                }
            }
            Trip trip = new Trip();
            JSONObject jsonAward = (JSONObject) outBound.get(i);
            JSONObject availableSeats = (JSONObject) jsonAward.get("remainingSeatsByCabinClass");
            System.out.println(Integer.parseInt(availableSeats.get(cabin).toString()));
            if (Integer.parseInt(availableSeats.get(cabin).toString()) < seats) {
                continue;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date arrivalDate = sdf.parse((String) jsonAward.get("arrival"));
            Date departureDate = sdf.parse((String) jsonAward.get("departure"));
            long millis = (arrivalDate.getTime() - departureDate.getTime());
            String duration = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))
            );
            trip.setTripDuration(duration);
            JSONObject baseTripFareMapper = (JSONObject) jsonObj.get("baseTripFareMapper");
            ArrayList fareMapperList = new ArrayList(baseTripFareMapper.keySet());
            String fares = (String) baseTripFareMapper.get(fareMapperList.get(i).toString());
            JSONObject jsonFares = (JSONObject) (((JSONObject) jsonObj.get("fares")).getJSONObject(fares).getJSONArray("fares")).get(0);
            Integer miles = ((Integer) jsonFares.get("totalMiles"));
            String tax = ((Double) jsonFares.get("tax")).toString();
            trip.setTax(new BigDecimal(tax));
            trip.setMiles(miles);
            //   info.setCurrency((jsonFares.get("currencyCode")).toString());
          /*  if (cabin.equals("ECONOMY")) {
               award.setEconomy(info);
            } else if (cabin.equals("PRESTIGE")) {
                award.setBusiness(info);

            } else if (cabin.equals("FIRST")) {
                award.setFirst(info);
            }
*/
            switch (cabin) {
                case "ECONOMY":
                    cabin = "E";
                    break;
                case "PRESTIGE":
                    cabin = "B";
                    break;
                case "FIRST":
                    cabin = "F";
                    break;
            }

            List<Flight> flightList = trip.getFlights();
            JSONArray flights = jsonAward.getJSONArray("flights");
            for (int j = 0; j < flights.length(); j++) {
                JSONObject jsonFlight = (JSONObject) flights.get(j);
                Flight flight = new Flight();
                flight.setAircraft((String) jsonFlight.get("aircraftName"));

                Date arriveDate = sdf.parse((String) jsonFlight.get("arrival"));
                Date departDate = sdf.parse((String) jsonFlight.get("departure"));
                flight.setArriveDate(arriveDate);
                flight.setDepartDate(departDate);
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                flight.setArriveTime(timeFormat.format(arriveDate));
                flight.setDepartTime(timeFormat.format(departDate));
                String arriveCode = (String) jsonFlight.get("destinationAirportCode");
                String departCode = (String) jsonFlight.get("departureAirportCode");

                String[] arriveInfo = PLACES.get(arriveCode).split(", ");
                String[] departInfo = PLACES.get(departCode).split(", ");
                //   flight.setArriveAirport(arriveInfo[1]);
                flight.setArrivePlace(arriveInfo[0]);
                //   flight.setArriveCity(arriveInfo[0]);
                //   flight.setDepartAirport(departInfo[1]);
                flight.setDepartPlace(departInfo[0]);
                flight.setArriveCode(arriveCode);
                flight.setDepartCode(departCode);
                //   flight.setDepartCity(departInfo[0]);
                flight.setFlightNumber((String) jsonFlight.get("flightNumber"));
                //     flight.setMeal("");
                long longFlightDuration = (flight.getArriveDate().getTime() - flight.getDepartDate().getTime());
                String flightDuration = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(longFlightDuration),
                        TimeUnit.MILLISECONDS.toMinutes(longFlightDuration) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(longFlightDuration))
                );
                flight.setFlightDuration(flightDuration);
                flight.setCarrierCode(flight.getFlightNumber().substring(0, 2));
                flight.setCarrierName(flight.getFlightNumber().substring(0, 2));
                flight.setTrip(trip);
                flight.setCabin(cabin);
                flight.setPosition(flightList.size());
                flight.setParser(PARSER_CODE);
                flight.setLayover("00:00");
                flight.setCreatedAt(new Date());
                flight.setUpdatedAt(new Date());
                flightList.add(flight);
                //without cabin info

            }
            trip.setFlights(flightList);
            // trip.setRequestId((long) requestId);
            trip.setDepartCode(flightList.get(0).getDepartCode());
            trip.setArriveCode(flightList.get(flightList.size() - 1).getArriveCode());
            trip.setDepartPlace(flightList.get(0).getDepartPlace());
            trip.setArrivePlace(flightList.get(flightList.size() - 1).getArrivePlace());
            trip.setTripDate(flightList.get(0).getDepartDate());
            trip.setQueryId((long) requestId);
            if (flightList.size() > 1) {
                StringBuilder stops = new StringBuilder("[");
                for (int index = 1; index < flightList.size(); index++) {
                    stops.append("\"" + flightList.get(index).getDepartCode() + "\",");
                    Date depDate = flightList.get(index).getDepartDate();
                    Date arrDate = flightList.get(index - 1).getArriveDate();
                    long longLayoverDuration = (depDate.getTime() - arrDate.getTime());
                    String layoverDuration = String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toHours(longLayoverDuration),
                            TimeUnit.MILLISECONDS.toMinutes(longLayoverDuration) -
                                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(longLayoverDuration))
                    );
                    flightList.get(index).setLayover(layoverDuration);
                }
                stops.deleteCharAt(stops.length() - 1);
                stops.append("]");
                trip.setStops(stops.toString());

            } else {
                trip.setStops("[]");
                trip.getFlights().get(0).setLayover("00:00");
            }


            StringBuilder carriers = new StringBuilder("[");
            StringBuilder flightLegs = new StringBuilder("[");
            StringBuilder flightNumbers = new StringBuilder("[");
            StringBuilder cabins = new StringBuilder("[");
            StringBuilder layovers = new StringBuilder("[");
            for (Flight flight : flightList) {
                carriers.append("\"" + flight.getCarrierCode() + "\",");
                flightLegs.append("\"" + flight.getFlightDuration() + "\",");
                flightNumbers.append("\"" + flight.getFlightNumber() + "\",");
                cabins.append("\"" + flight.getCabin() + "\",");
                if (!flight.getLayover().equals("00:00")) {
                    layovers.append("\"" + flight.getLayover() + "\",");
                }
            }
            carriers.deleteCharAt(carriers.length() - 1);
            carriers.append("]");
            flightLegs.deleteCharAt(flightLegs.length() - 1);
            flightLegs.append("]");
            flightNumbers.deleteCharAt(flightNumbers.length() - 1);
            flightNumbers.append("]");
            cabins.deleteCharAt(cabins.length() - 1);
            cabins.append("]");
            if (layovers.length() > 1)
                layovers.deleteCharAt(layovers.length() - 1);
            layovers.append("]");
            trip.setCarriers(carriers.toString());
            trip.setFlightLegs(flightLegs.toString());
            trip.setFlightNumbers(flightNumbers.toString());
            trip.setCabins(cabins.toString());
            trip.setLayovers(layovers.toString());
            trip.setCreatedAt(new Date());
            trip.setUpdatedAt(new Date());
            resultList.add(trip);
        }
        return resultList;
    }

    private void setMiles2Trip(List<Trip> trips, MileCost mileCost) {
        if (mileCost == null) return;
        for (Trip trip : trips) {
            double parserCost = trip.getMiles() / 100 * mileCost.getCost().doubleValue() + trip.getTax().doubleValue();//ещё сложить таксы
            trip.setCost(BigDecimal.valueOf(parserCost));
        }
    }

    private static boolean isJSONValid(String test) {
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


    @Override
    public Collection<Trip> parse(RequestData requestData) throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Trip> results = new ArrayList<>();
        DefaultHttpClient loggedInClient = login();
        String origin = requestData.getOrigin();
        String destination = requestData.getDestination();
        List<String> cabins = requestData.getCabins();
        int seats = requestData.getSeats();
        int requestId = requestData.getRequest_id();
        List<Date> owDates = requestData.getOwDates();
        List<Date> returnDates = requestData.getReturnDates();
        Set<Callable<List<Trip>>> callables = new HashSet<Callable<List<Trip>>>();
        for (Date date : owDates) {
            callables.add(new DataThread(date, seats, cabins, destination, origin, requestId));
        }
        for (Date date : returnDates) {
            callables.add(new DataThread(date, seats, cabins, origin, destination, requestId));
        }
        List<Future<List<Trip>>> futureList = executor.invokeAll(callables);
        for (Future<List<Trip>> futureItem : futureList) {
            try {
                List<Trip> trips = futureItem.get();
                results.addAll(trips);
            } catch (Exception ex) {
                return results;
            }
        }
        executor.shutdown();
        MileCost mileCost = null;
        if (results.size() != 0) {
            List<MileCost> miles = StreamSupport.stream(Spliterators.spliteratorUnknownSize(mileCostRepository.findAll().iterator(), Spliterator.ORDERED), false)
                    .collect(Collectors.toCollection(ArrayList::new));
            mileCost = miles.stream().filter(o -> Objects.equals(o.getParser(), results.get(0).getFlights().get(0).getParser()))
                    .findFirst().get();
            // result = setMiles2Trip(result,mileCost);
            results.get(0).setIsComplete(true);
        }
        setMiles2Trip(results, mileCost);
        return results;
    }
}
