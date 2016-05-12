package com.guru.parser.utils;

import com.guru.domain.model.Trip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Никита on 11.05.2016.
 */
public class ComplexTrip {

    List<Trip> oneWayList = new ArrayList<>();
    List<Trip> returnWayList = new ArrayList<>();

    public List<Trip> getOneWayList() {
        return oneWayList;
    }

    public void setOneWayList(List<Trip> oneWayList) {
        this.oneWayList = oneWayList;
    }

    public List<Trip> getReturnWayList() {
        return returnWayList;
    }

    public void setReturnWayList(List<Trip> returnWayList) {
        this.returnWayList = returnWayList;
    }
}
