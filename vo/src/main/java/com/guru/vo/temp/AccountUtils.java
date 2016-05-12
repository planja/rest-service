package com.guru.vo.temp;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Anton on 29.04.2016.
 */
public class AccountUtils {
    public AccountUtils() {
    }

    public static Account getAccount(String parser) throws IOException {
        Account account = new Account();
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter("http.protocol.cookie-policy", "compatibility");
        httpclient.setCookieStore(new BasicCookieStore());
        httpclient.getParams().setParameter("http.useragent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
        httpclient.setRedirectStrategy(new LaxRedirectStrategy());
        HttpGet httpGet = new HttpGet("https://fly3z.com/accounts/getBestAccount/" + parser + "/JSON");
        CloseableHttpResponse hResponse = null;
        HttpEntity entity = null;
        hResponse = httpclient.execute(httpGet);
        entity = hResponse.getEntity();
        String callback = responseToString(entity.getContent());
        if (callback.equals("[]")) {
            return account;
        } else {
            JSONObject jsonObj = new JSONObject(callback);
            String id = jsonObj.getJSONObject("Account").getString("id");
            String login = jsonObj.getJSONObject("Account").getString("account_login");
            String password = jsonObj.getJSONObject("Account").getString("account_password");
            String pin = jsonObj.getJSONObject("Account").getString("account_pin");
            account.setId(id);
            account.setLogin(login);
            account.setPassword(password);
            account.setPin(pin);
            return account;
        }
    }

    public static void badAccount(String id) throws IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter("http.protocol.cookie-policy", "compatibility");
        httpclient.setCookieStore(new BasicCookieStore());
        httpclient.getParams().setParameter("http.useragent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
        httpclient.setRedirectStrategy(new LaxRedirectStrategy());
        HttpGet httpGet = new HttpGet("https://fly3z.com/accounts/markAccountAsBroken/" + id);
        CloseableHttpResponse hResponse = null;
        HttpEntity entity = null;
        hResponse = httpclient.execute(httpGet);
        entity = hResponse.getEntity();
        String callback = responseToString(entity.getContent());
    }

    private static String responseToString(InputStream inputStream) throws IOException {
        return IOUtils.toString(inputStream);
    }
}