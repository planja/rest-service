package viewmodel;

import java.util.List;

/**
 * Created by Никита on 08.04.2016.
 */
public class RequestParamsVewModel {

    @Deprecated
    public RequestParamsVewModel() {
    }

    public RequestParamsVewModel(String parser, String user, String login, String name, String origin, String destination, String ow_start_date, String ow_end_date, String rt_start_date, String rt_end_date, List<String> ow_except_dates, List<String> rt_except_dates, String seats, List<String> cabins, String type, String request_id, String user_id) {
        this.parser = parser;
        this.user = user;
        this.login = login;
        this.name = name;
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
    private String login;
    private String name;
    private String origin;
    private String destination;
    private String ow_start_date;
    private String ow_end_date;
    private String rt_start_date;
    private String rt_end_date;
    private List<String> ow_except_dates;
    private List<String> rt_except_dates;
    private String seats;
    private List<String> cabins;
    private String type;
    private String request_id;
    private String user_id;

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getOw_end_date() {
        return ow_end_date;
    }

    public void setOw_end_date(String ow_end_date) {
        this.ow_end_date = ow_end_date;
    }

    public String getRt_start_date() {
        return rt_start_date;
    }

    public void setRt_start_date(String rt_start_date) {
        this.rt_start_date = rt_start_date;
    }

    public String getRt_end_date() {
        return rt_end_date;
    }

    public void setRt_end_date(String rt_end_date) {
        this.rt_end_date = rt_end_date;
    }

    public List<String> getOw_except_dates() {
        return ow_except_dates;
    }

    public void setOw_except_dates(List<String> ow_except_dates) {
        this.ow_except_dates = ow_except_dates;
    }

    public List<String> getRt_except_dates() {
        return rt_except_dates;
    }

    public void setRt_except_dates(List<String> rt_except_dates) {
        this.rt_except_dates = rt_except_dates;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
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

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
