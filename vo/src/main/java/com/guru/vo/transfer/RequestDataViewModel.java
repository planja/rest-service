package com.guru.vo.transfer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Никита on 06.05.2016.
 */
public class RequestDataViewModel {


    private List<String> parsers;
    private String user;
    private String origin;
    private String destination;
    private String ow_start_date;
    private String ow_end_date;
    private String rt_start_date;
    private String rt_end_date;
    private List<ExceptDateViewModel> ow_except_dates;
    private List<ExceptDateViewModel> rt_except_dates;
    private int seats;
    private List<String> cabins;
    private String type;
    private int request_id;
    private int user_id;
    private List<Date> owDates;
    private List<Date> returnDates;

    public RequestData toRequestData() throws ParseException {
        RequestData requestData = new RequestData();
        requestData.setParsers(this.user == null ? null : this.getParsers());
        requestData.setUser(this.user == null ? null : this.getUser());
        requestData.setOrigin(this.origin == null ? null : this.getOrigin());
        requestData.setDestination(this.destination == null ? null : this.getDestination());

        requestData.setOw_start_date(this.ow_start_date == null ? null : convertDate(this.getOw_start_date()));
        requestData.setOw_end_date(this.ow_end_date == null ? null : convertDate(this.getOw_end_date()));

        requestData.setRt_start_date(this.rt_start_date == null ? null : convertDate(this.getRt_start_date()));
        requestData.setRt_end_date(this.rt_end_date == null ? null : convertDate(this.getRt_end_date()));


        if (this.rt_except_dates != null) {
            List<ExceptDate> exceptDates = new ArrayList<>();
            for (ExceptDateViewModel exceptDateViewModel : this.getRt_except_dates())
                exceptDates.add(convertExceptDate(exceptDateViewModel));
            requestData.setRt_except_dates(exceptDates);
        }

        if (this.ow_except_dates != null) {
            List<ExceptDate> owExceptDates = new ArrayList<>();
            for (ExceptDateViewModel exceptDateViewModel : this.getOw_except_dates())
                owExceptDates.add(convertExceptDate(exceptDateViewModel));
            requestData.setOw_except_dates(owExceptDates);
        }

        requestData.setSeats(this.getSeats());
        requestData.setCabins(this.cabins == null ? null : this.getCabins());
        requestData.setType(this.type == null ? null : this.getType());
        requestData.setRequest_id(this.getRequest_id());
        requestData.setUser_id(this.getUser_id());
        return requestData;
    }

    private Date convertDate(String string) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.parse(string + " UTC");
    }

    private ExceptDate convertExceptDate(ExceptDateViewModel exceptDateViewModel) throws ParseException {
        ExceptDate exceptDate = new ExceptDate();
        exceptDate.setParser(exceptDateViewModel.getParser() == null ? null : exceptDateViewModel.getParser());
        exceptDate.setDate(exceptDateViewModel.getDate() == null ? null : convertDate(exceptDateViewModel.getDate()));
        return exceptDate;
    }

    public RequestDataViewModel() {
    }

    public List<String> getParsers() {
        return parsers;
    }

    public void setParsers(List<String> parsers) {
        this.parsers = parsers;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOw_start_date() {
        return ow_start_date;
    }

    public void setOw_start_date(String ow_start_date) {
        this.ow_start_date = ow_start_date;
    }

    public String getRt_end_date() {
        return rt_end_date;
    }

    public void setRt_end_date(String rt_end_date) {
        this.rt_end_date = rt_end_date;
    }

    public String getRt_start_date() {
        return rt_start_date;
    }

    public void setRt_start_date(String rt_start_date) {
        this.rt_start_date = rt_start_date;
    }

    public String getOw_end_date() {
        return ow_end_date;
    }

    public void setOw_end_date(String ow_end_date) {
        this.ow_end_date = ow_end_date;
    }

    public List<ExceptDateViewModel> getOw_except_dates() {
        return ow_except_dates;
    }

    public void setOw_except_dates(List<ExceptDateViewModel> ow_except_dates) {
        this.ow_except_dates = ow_except_dates;
    }

    public List<ExceptDateViewModel> getRt_except_dates() {
        return rt_except_dates;
    }

    public void setRt_except_dates(List<ExceptDateViewModel> rt_except_dates) {
        this.rt_except_dates = rt_except_dates;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public List<String> getCabins() {
        return cabins;
    }

    public void setCabins(List<String> cabins) {
        this.cabins = cabins;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


}
