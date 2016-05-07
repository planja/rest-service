package com.guru.parser.utils;

import com.guru.parser.dl.DLParser;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * Created by Никита on 27.04.2016.
 */
public class ParserUtils {

    public static String getTotalTime(String totalTime, Object parser) throws ParseException {
       String regexp = "";
        if (parser instanceof QFParser)
            regexp = "((\\d*)h\\s)?(\\d*)m";


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
            if(totalTime.contains("m")) minutes = matcher.group(3) != null && matcher.group(3).trim().length() != 0 ? matcher.group(3) : "0";
            else
                minutes = "0"; DecimalFormat acFormat = new DecimalFormat("##00");
            return acFormat.format((long) Integer.parseInt(hours)) + ":" + acFormat.format((long) Integer.parseInt(minutes));
        } else {
            return null;
        }
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
        return (int)(Math.random() * (double)max);
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
