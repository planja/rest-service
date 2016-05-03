package com.guru.vo.temp;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.params.CoreProtocolPNames;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

        String proxyList = Utils.responseToString(entity.getContent());

        String[] proxyArray = proxyList.split("\n");

        int randIndex = Utils.randInt(proxyArray.length);

        String proxyInfo = proxyArray[randIndex];

        return proxyInfo;
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

    public static boolean checkProxy(String proxy) {

        return false;
    }

}
