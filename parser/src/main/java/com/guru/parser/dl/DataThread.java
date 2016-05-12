package com.guru.parser.dl;

import com.guru.domain.model.Airport;
import com.guru.domain.model.Trip;

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
    private List<Airport> cities;

    public DataThread(Date date, int seats, String destination, String origin, int requestId, List<Airport> cities) {
        this.date = date;
        this.seats = seats;
        this.destination = destination;
        this.origin = origin;
        this.requestId = requestId;
        this.cities = cities;
    }

    @Override

    public List<Trip> call() throws Exception {
        List<Trip> trips = new ArrayList<Trip>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        System.out.println(date);
        String formattedDate = sdf.format(date);
        DLParser dlParser = new DLParser();
        trips.addAll(dlParser.getDelta(origin, destination, formattedDate, seats, requestId, cities));
        return trips;
    }
}


