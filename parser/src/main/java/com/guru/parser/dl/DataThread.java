package com.guru.parser.dl;

import com.guru.domain.model.Trip;
import com.guru.parser.ke.KEParser;
import org.apache.http.impl.client.DefaultHttpClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Anton on 04.05.2016.
 */

    public class DataThread implements Callable<List<Trip>> {
        private int requestId;
        private String origin;
        private String destination;
        private int seats;
        private Date date;
        private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        public DataThread(Date date, int seats, String destination, String origin, int requestId) {
            this.date = date;
            this.seats = seats;
            this.destination = destination;
            this.origin = origin;
            this.requestId = requestId;
        }

        @Override

        public List<Trip> call() throws Exception {
            List<Trip> trips = new ArrayList<Trip>();
            String formattedDate = sdf.format(date);
            DLParser dlParser = new DLParser();
            trips.addAll(dlParser.getDelta(origin, destination, formattedDate, seats, requestId));
            return trips;
        }
    }


