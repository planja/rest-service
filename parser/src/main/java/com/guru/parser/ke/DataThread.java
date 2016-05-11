package com.guru.parser.ke;

import com.guru.domain.model.Trip;
import com.guru.vo.temp.Account;
import com.guru.vo.temp.AccountUtils;
import org.apache.http.impl.client.DefaultHttpClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Anton on 03.05.2016.
 */
public class DataThread implements Callable<List<Trip>> {
    private int requestId;
    private String origin;
    private String destination;
    private List<String> cabins;
    private int seats;
    private Date date;

    public DataThread(Date date, int seats, List<String> cabins, String destination, String origin, int requestId) {
        this.date = date;
        this.seats = seats;
        this.cabins = cabins;
        this.destination = destination;
        this.origin = origin;
        this.requestId = requestId;
    }

    @Override

    public List<Trip> call() throws Exception {
        List<Trip> trips = new ArrayList<Trip>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = sdf.format(date);
        for (String cabin : cabins) {
            if (cabin.equals("P"))
                continue;

            KEParser keParser = new KEParser();
            Account account = AccountUtils.getAccount("KE");
            DefaultHttpClient loggedInClient = null;
            while (loggedInClient == null) {
                loggedInClient = keParser.login(account);
                if (loggedInClient == null) {
                    if (account != null)
                        AccountUtils.badAccount(account.getId());
                    account = AccountUtils.getAccount("KE");
                } else {
                    break;
                }
            }
            trips.addAll(keParser.getKE(requestId, origin, destination, formattedDate, seats, cabin, loggedInClient));
        }
        return trips;
    }
}
