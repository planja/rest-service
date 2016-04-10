package viewmodel;

import com.fasterxml.jackson.annotation.JsonFormat;
import service.processrequest.ProcessRequestHelperService;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by Никита on 08.04.2016.
 */
public class RequestParamsVewModel {

    @Deprecated
    public RequestParamsVewModel() {
    }

    public RequestParamsVewModel(String parser, String user, String origin, String destination, Date ow_start_date, Date ow_end_date, Date rt_start_date, Date rt_end_date, List<Date> ow_except_dates, List<Date> rt_except_dates, int seats, List<String> cabins, String type, int request_id, int user_id) {
        this.parser = parser;
        this.user = user;
        this.origin = origin;
        this.destination = destination;
        this.ow_start_date = ow_start_date;
        this.ow_end_date = ow_end_date;
        this.rt_start_date = rt_start_date;
        this.rt_end_date = rt_end_date;
        this.ow_except_dates = ow_except_dates;
        this.rt_except_dates = rt_except_dates;
        this.seats = seats;
        this.cabins = cabins;
        this.type = type;
        this.request_id = request_id;
        this.user_id = user_id;
    }

    private String parser;
    private String user;
    private String origin;
    private String destination;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date ow_start_date;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date ow_end_date;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date rt_start_date;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date rt_end_date;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private List<Date> ow_except_dates;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private List<Date> rt_except_dates;
    private int seats;
    private List<String> cabins;
    private String type;
    private int request_id;
    private int user_id;


    private List<Date> owDates;
    private List<Date> returnDates;


    public String getParser() {
        return parser;
    }

    public void setParser(String parser) {
        this.parser = parser;
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

    public Date getOw_start_date() {
        return ow_start_date;
    }

    public void setOw_start_date(Date ow_start_date) {
        this.ow_start_date = ow_start_date;
    }

    public Date getOw_end_date() {
        return ow_end_date;
    }

    public void setOw_end_date(Date ow_end_date) {
        this.ow_end_date = ow_end_date;
    }

    public Date getRt_start_date() {
        return rt_start_date;
    }

    public void setRt_start_date(Date rt_start_date) {
        this.rt_start_date = rt_start_date;
    }

    public Date getRt_end_date() {
        return rt_end_date;
    }

    public void setRt_end_date(Date rt_end_date) {
        this.rt_end_date = rt_end_date;
    }

    public List<Date> getOw_except_dates() {
        return ow_except_dates;
    }

    public void setOw_except_dates(List<Date> ow_except_dates) {
        this.ow_except_dates = ow_except_dates;
    }

    public List<Date> getRt_except_dates() {
        return rt_except_dates;
    }

    public void setRt_except_dates(List<Date> rt_except_dates) {
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

    public List<Date> getOwDates() throws ParseException {
        List<Date> dates = ProcessRequestHelperService.getDaysBetweenDates(getOw_start_date(), getOw_end_date());
        List<Date> returnDates = ProcessRequestHelperService.getReturnDates(getRt_start_date(), getRt_end_date(), dates);
        return ProcessRequestHelperService.getODates(dates, returnDates, this.getOw_except_dates());
    }

    public List<Date> getReturnDates() throws ParseException {
        List<Date> dates = ProcessRequestHelperService.getDaysBetweenDates(getOw_start_date(), getOw_end_date());
        List<Date> returnDates = ProcessRequestHelperService.getReturnDates(getRt_start_date(), getRt_end_date(), dates);
        return ProcessRequestHelperService.getRDates(dates, returnDates, this.getRt_except_dates());

    }

}
