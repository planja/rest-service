package com.guru.domain.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Никита on 30.04.2016.
 */
public class ClasInfo implements scala.Serializable{

    public static final int NA = 0;
    public static final int AVAILABLE = 1;
    public static final int WAITLIST = 2;
    public boolean na = false;
    private String name;
    private String reduction;
    private String url;
    private String position;
    private String mileage = "";
    private String tax = "";
    private String currency;
    private int status = 0;
    private List<String> awardNames = new LinkedList();
    private boolean mixed = false;
    private List<String> mixedCabins = new LinkedList();

    public ClasInfo() {
    }

    public ClasInfo(boolean na) {
        this.na = na;
        if(!this.na) {
            this.status = 1;
        }

    }

    public String getStringStatus() {
        return this.status == 0?"NA":(this.status == 1?"AVAILABLE":(this.status == 2?"WAITLIST":"NA"));
    }

    public String getMileage() {
        return this.mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getTax() {
        return this.tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public boolean isNa() {
        return this.na;
    }

    public void setNa(boolean na) {
        this.na = na;
    }

    public boolean isMixed() {
        return this.mixed;
    }

    public void setMixed(boolean mixed) {
        this.mixed = mixed;
    }

    public List<String> getMixedCabins() {
        return this.mixedCabins;
    }

    public void setMixedCabins(List<String> mixedCabins) {
        this.mixedCabins = mixedCabins;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getAwardNames() {
        return this.awardNames;
    }

    public void setAwardNames(List<String> awardNames) {
        this.awardNames = awardNames;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getReduction() {
        return reduction;
    }

    public void setReduction(String reduction) {
        this.reduction = reduction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
