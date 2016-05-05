package com.guru.parser.impl.qfparser;

/**
 * Created by Никита on 29.04.2016.
 */
public class Info {

    private String depart;
    private String arrive;

    public Info() {
    }

    public Info(String depart, String arrive) {
        this.depart = depart;
        this.arrive = arrive;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getArrive() {
        return arrive;
    }

    public void setArrive(String arrive) {
        this.arrive = arrive;
    }
}
