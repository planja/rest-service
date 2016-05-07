package com.guru.parser.utils;

import com.guru.parser.account.Account;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONObject;


import java.io.IOException;

/**
 * Created by Никита on 06.05.2016.
 */
public class AccountUtils {

    public static Account getAccount(String parser) throws IOException {

        Account account = new Account();

        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        httpclient.setCookieStore(new BasicCookieStore());
        httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
        httpclient.setRedirectStrategy(new LaxRedirectStrategy());

        HttpGet httpGet = new HttpGet("https://fly3z.com/accounts/getBestAccount/" + parser + "/JSON");

        HttpResponse hResponse = null;
        HttpEntity entity = null;

        hResponse = httpclient.execute(httpGet);
        entity = hResponse.getEntity();

        String callback = ParserUtils.responseToString(entity.getContent());
        if (callback.equals("[]")) {
            return account;
        }
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

    public static void badAccount(String id) throws IOException {

        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        httpclient.setCookieStore(new BasicCookieStore());
        httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
        httpclient.setRedirectStrategy(new LaxRedirectStrategy());

        HttpGet httpGet = new HttpGet("https://fly3z.com/accounts/markAccountAsBroken/" + id);

        HttpResponse hResponse = null;
        HttpEntity entity = null;

        hResponse = httpclient.execute(httpGet);
        entity = hResponse.getEntity();

        String callback = ParserUtils.responseToString(entity.getContent());
    }

}
