package viewmodel;

/**
 * Created by Никита on 05.04.2016.
 */
public class ParamsViewModel {

    @Deprecated
    public ParamsViewModel() {
    }

    public ParamsViewModel(String startDate, String endDate, String returnStartDate, String returnEndDate, String origin, String destination, String seats, String flightClass, String requestId, String userId, String type, String except_days, String except_return_days, String login) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.returnStartDate = returnStartDate;
        this.returnEndDate = returnEndDate;
        this.origin = origin;
        this.destination = destination;
        this.seats = seats;
        this.flightClass = flightClass;
        this.requestId = requestId;
        this.userId = userId;
        this.type = type;
        this.except_days = except_days;
        this.except_return_days = except_return_days;
        this.login = login;
    }

    private String startDate;
    private String endDate;
    private String returnStartDate;
    private String returnEndDate;
    private String origin;
    private String destination;
    private String seats;
    private String flightClass;
    private String requestId;
    private String userId;
    private String type;

    private String except_days;
    private String except_return_days;

    private String login;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getReturnStartDate() {
        return returnStartDate;
    }

    public void setReturnStartDate(String returnStartDate) {
        this.returnStartDate = returnStartDate;
    }

    public String getReturnEndDate() {
        return returnEndDate;
    }

    public void setReturnEndDate(String returnEndDate) {
        this.returnEndDate = returnEndDate;
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

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(String flightClass) {
        this.flightClass = flightClass;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExcept_days() {
        return except_days;
    }

    public void setExcept_days(String except_days) {
        this.except_days = except_days;
    }

    public String getExcept_return_days() {
        return except_return_days;
    }

    public void setExcept_return_days(String except_return_days) {
        this.except_return_days = except_return_days;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
