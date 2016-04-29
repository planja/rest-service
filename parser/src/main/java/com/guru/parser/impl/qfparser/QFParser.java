package com.guru.parser.impl.qfparser;

import com.guru.domain.model.Flight;
import com.guru.domain.model.Trip;
import com.guru.parser.interf.Parser;
import com.guru.vo.transfer.RequestData;
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
import parser.exceptions.IncorrectCredentials;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by Никита on 27.04.2016.
 */

public class QFParser implements Parser {

    @Override
    public Collection<Trip> parse(RequestData requestData) throws Exception {
        return getQantas(requestData);
    }

    private DefaultHttpClient login(String user, String surname, String password) throws IOException, InterruptedException, ExecutionException, IncorrectCredentials {
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

    private List<Trip> getQantas(RequestData requestData) throws IncorrectCredentials, IOException, InterruptedException, ExecutionException, ParseException {
        DefaultHttpClient httpclient = this.login("1924112640", "Kin", "4152");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        List<Trip> awardList = new ArrayList<>
                ((new ParserThread(httpclient, sdf1.format(requestData.getOw_start_date()),
                        sdf1.format(requestData.getOw_end_date()), requestData.getOrigin(),
                        requestData.getDestination(), requestData.getSeats(), this)).call());
        ExecutorService executor = Executors.newCachedThreadPool();
        LinkedList resultList = new LinkedList();
        HashSet callables = new HashSet();
        Iterator futureList = awardList.iterator();

        while (futureList.hasNext()) {
            Trip trip = (Trip) futureList.next();
            callables.add(new DataThread(trip, httpclient));
        }

        List futureList1 = executor.invokeAll(callables);
        Iterator award2 = futureList1.iterator();

        while (award2.hasNext()) {
            Future award1 = (Future) award2.next();
            resultList.add(award1.get());
        }

        award2 = resultList.iterator();

        List<Trip> trips = new ArrayList<>();
        List<Trip> ow = new ArrayList<>();
        List<Trip> rt = new ArrayList<>();


        while (award2.hasNext()) {
            Trip next = (Trip) award2.next();
            if (next.getDirection() == 0) {
                ow.add(next);
            } else {
                rt.add(next);
            }
        }
        rt.forEach(o->o.setQueryId(Long.valueOf("0")));
        trips.addAll(ow);
        trips.addAll(rt);


        /*SimpleDateFormat sdf_qr = new SimpleDateFormat("MM/dd/yyyy");
        parser.qf.QFParser qfParser = new parser.qf.QFParser();
        DefaultHttpClient client = parser.qf.QFParser.login("1905029755", "Myasnyankin", "7759");
       ComplexAward complexAward = qfParser.getQantas(client, sdf_qr.parse("05/01/2016"), sdf_qr.parse("05/01/2016"), "LGW", "ADL", 1);
 */
        getTrips(trips);
        return trips;
    }


    private void getTrips(List<Trip> trips) {
        trips.stream().forEach(o -> {
            o.setQueryId((long) (new Random().nextDouble() * 123L));
            o.setArriveCode(o.getFlights().get(0).getArriveCode());
            o.setDepartCode(o.getFlights().get(o.getFlights().size() - 1).getDepartCode());

            o.setArrivePlace(o.getFlights().get(0).getArrivePlace());
            o.setDepartPlace(o.getFlights().get(o.getFlights().size() - 1).getArrivePlace());

            o.setTripDate(o.getFlights().get(0).getDepartDate());
            o.setStops("[\"stops\"]");
            o.setCabins(getCabins(o.getFlights()));
            o.setCarriers(getCarriers(o.getFlights()));
            o.setLayovers(null);
            o.setFlightLegs(getFlightLegs(o.getFlights()));
            o.setFlightNumbers(getFlightNumbers(o.getFlights()));
            o.setCost(BigDecimal.valueOf(2000));
            o.setUpdatedAt(new Date());
            o.setCreatedAt(new Date());
            o.getFlights().forEach(k -> k.setTrip(o));
            o.setLayovers("[\"layovers\"]");
        });
    }

    private String getCabins(List<Flight> flights) {
        String str = "[" + flights.stream().map(o -> "\"" + o.getCabin() + "\",").collect(Collectors.joining()) + "]";
        int index = str.lastIndexOf(',');//Переделать
        return str.substring(0, index) + str.substring(index + 1, str.length());


    }

    private String getCarriers(List<Flight> flights) {
        String str = "[" + flights.stream().map(o -> "\"" + o.getCarrierCode() + "\",").collect(Collectors.joining()) + "]";//??????
        int index = str.lastIndexOf(',');//Переделать
        return str.substring(0, index) + str.substring(index + 1, str.length());
    }

    private String getFlightLegs(List<Flight> flights) {
        String str = "[" + flights.stream().map(o -> "\"" + o.getFlightDuration() + "\",").collect(Collectors.joining()) + "]";
        int index = str.lastIndexOf(',');//Переделать
        return str.substring(0, index) + str.substring(index + 1, str.length());
    }


    private String getFlightNumbers(List<Flight> flights) {
        String str = "[" + flights.stream().map(o -> "\"" + o.getFlightNumber() + "\",").collect(Collectors.joining()) + "]";
        int index = str.lastIndexOf(',');//Переделать
        return str.substring(0, index) + str.substring(index + 1, str.length());
    }
}
