package com.guru.parser.ke;

import com.guru.domain.model.Trip;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.List;

/**
 * Created by Anton on 27.04.2016.
 */
public class KEMain {
    public static void main(String[] args) throws  Exception{
        KEParser keParser = new KEParser();
        DefaultHttpClient client = keParser.login();
        List<Trip> results = keParser.getKE(1,"ICN","TYO","06-01-2016",1,"E",client);
        System.out.println(results);
    }
}
