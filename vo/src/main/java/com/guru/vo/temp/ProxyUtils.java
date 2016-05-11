package com.guru.vo.temp;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Anton on 02.05.2016.
 */
public class ProxyUtils {
    public static String getProxy(String parser) throws IOException {

        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        httpclient.setCookieStore(new BasicCookieStore());
        httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
        httpclient.setRedirectStrategy(new LaxRedirectStrategy());

//        org.apache.http.conn.ssl.SSLSocketFactory sf = org.apache.http.conn.ssl.SSLSocketFactory.getSocketFactory();
//        sf.setHostnameVerifier(new MyHostnameVerifier());
//        org.apache.http.conn.scheme.Scheme sch = new Scheme("https", 443, sf);
//
//        httpclient.getConnectionManager().getSchemeRegistry().register(sch);

        HttpGet httpGet = new HttpGet("https://fly3z.com/proxies/getproxies/" + parser);

        HttpResponse response = null;
        HttpEntity entity = null;

        response = httpclient.execute(httpGet);
        entity = response.getEntity();

        String proxyList = responseToString(entity.getContent());

        String[] proxyArray = proxyList.split("\n");
        int randIndex = randInt(proxyArray.length);
        String proxyInfo = proxyArray[randIndex];
        return proxyInfo;
    }

    public static void markProxyAs(String proxy, String parser, boolean active) throws IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        httpclient.setCookieStore(new BasicCookieStore());
        httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
        httpclient.setRedirectStrategy(new LaxRedirectStrategy());
        HttpGet httpGet;
        if (active) {
            httpGet = new HttpGet("https://fly3z.com/proxies/markAsActive/" + proxy.substring(proxy.indexOf("@") + 1) + "/" + parser);
            System.out.println(httpGet.getURI());
        } else {
            httpGet = new HttpGet("https://fly3z.com/proxies/markAsBroken/" + proxy.substring(proxy.indexOf("@") + 1) + "/" + parser);
            System.out.println(httpGet.getURI());
        }
        CloseableHttpResponse hResponse = null;
        HttpEntity entity = null;
        hResponse = httpclient.execute(httpGet);
        entity = hResponse.getEntity();
        String callback = responseToString(entity.getContent());
        httpGet.abort();

    }

    public static String getProxy(String url, int index) {

        List<String> proxyList = new LinkedList<String>();

        try {

            File file = new File("/var/lib/tomcat7/proxylist.txt");

            FileReader fileReader = new FileReader(file);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {

                proxyList.add(line);
            }

            fileReader.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (index >= proxyList.size()) {

            index = index - proxyList.size() * (index / proxyList.size());
        }

        String result = proxyList.get(index);

        return result;
    }


    private static String responseToString(InputStream inputStream) throws IOException {
        return IOUtils.toString(inputStream);
    }


    private static int randInt(int max) {

        return (int) (Math.random() * max);
    }

    public static void linkProxyToAccount(String accountId, String proxyInfo, String parser) throws IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        httpclient.setCookieStore(new BasicCookieStore());
        httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
        httpclient.setRedirectStrategy(new LaxRedirectStrategy());
        HttpGet httpGet = new HttpGet("https://fly3z.com/proxies/getproxies/" + parser + "/true");

        HttpResponse response = null;
        HttpEntity entity = null;

        response = httpclient.execute(httpGet);
        entity = response.getEntity();
        String callback = responseToString(entity.getContent());
        if (!callback.equals("[]")) {
            JSONArray jsonArray = new JSONArray(callback);
            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;
                if (jsonObject.getJSONObject("Proxy").getString("proxy").equals(proxyInfo)) {
                    String proxyId = jsonObject.getJSONObject("Proxy").getString("id");
                    httpGet = new HttpGet("https://fly3z.com/accounts/linkAccountToProxy/" + accountId + "/" + proxyId);
                    HttpEntity newEntity = null;
                    CloseableHttpResponse hResponse = httpclient.execute(httpGet);
                    httpGet.abort();
                }
            }
        }
        httpGet.abort();
        httpclient.close();
    }


}
