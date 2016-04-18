package com.guru.vo.view;

public class IMTFlight {

    private String departTime;
    private String departDate;
    private String departPlace;
    private String departCode;

    private String arriveTime;
    private String arriveDate;
    private String arrivePlace;
    private String arriveCode;

    private String travelTime;

    private String flightNumber;
    private String airlineCompany;
    private String aircraft;
    private String meal;
    private String flightCabin;
    private Integer id;

    public IMTFlight() {
    }

    public IMTFlight(String departTime, String departDate, String departPlace, String departCode, String arriveTime, String arriveDate, String arrivePlace, String arriveCode, String travelTime, String flightNumber, String airlineCompany, String aircraft, String meal, String flightCabin, Integer id) {
        this.departTime = departTime;
        this.departDate = departDate;
        this.departPlace = departPlace;
        this.departCode = departCode;
        this.arriveTime = arriveTime;
        this.arriveDate = arriveDate;
        this.arrivePlace = arrivePlace;
        this.arriveCode = arriveCode;
        this.travelTime = travelTime;
        this.flightNumber = flightNumber;
        this.airlineCompany = airlineCompany;
        this.aircraft = aircraft;
        this.meal = meal;
        this.flightCabin = flightCabin;
        this.id = id;
    }

    public String getDepartTime() {
        return departTime;
    }

    public void setDepartTime(String departTime) {
        this.departTime = departTime;
    }

    public String getDepartDate() {
        return departDate;
    }

    public void setDepartDate(String departDate) {
        this.departDate = departDate;
    }

    public String getDepartPlace() {
        return departPlace;
    }

    public void setDepartPlace(String departPlace) {
        this.departPlace = departPlace;
    }

    public String getDepartCode() {
        return departCode;
    }

    public void setDepartCode(String departCode) {
        this.departCode = departCode;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getArriveDate() {
        return arriveDate;
    }

    public void setArriveDate(String arriveDate) {
        this.arriveDate = arriveDate;
    }

    public String getArrivePlace() {
        return arrivePlace;
    }

    public void setArrivePlace(String arrivePlace) {
        this.arrivePlace = arrivePlace;
    }

    public String getArriveCode() {
        return arriveCode;
    }

    public void setArriveCode(String arriveCode) {
        this.arriveCode = arriveCode;
    }

    public String getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {
        this.travelTime = travelTime;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getAirlineCompany() {
        return airlineCompany;
    }

    public void setAirlineCompany(String airlineCompany) {
        this.airlineCompany = airlineCompany;
    }

    public String getAircraft() {
        return aircraft;
    }

    public void setAircraft(String aircraft) {
        this.aircraft = aircraft;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getFlightCabin() {
        return flightCabin;
    }

    public void setFlightCabin(String flightCabin) {
        this.flightCabin = flightCabin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IMTFlight imtFlight = (IMTFlight) o;

        if (aircraft != null ? !aircraft.equals(imtFlight.aircraft) : imtFlight.aircraft != null) return false;
        if (airlineCompany != null ? !airlineCompany.equals(imtFlight.airlineCompany) : imtFlight.airlineCompany != null)
            return false;
        if (arriveCode != null ? !arriveCode.equals(imtFlight.arriveCode) : imtFlight.arriveCode != null) return false;
        if (arriveDate != null ? !arriveDate.equals(imtFlight.arriveDate) : imtFlight.arriveDate != null) return false;
        if (arrivePlace != null ? !arrivePlace.equals(imtFlight.arrivePlace) : imtFlight.arrivePlace != null)
            return false;
        if (arriveTime != null ? !arriveTime.equals(imtFlight.arriveTime) : imtFlight.arriveTime != null) return false;
        if (departCode != null ? !departCode.equals(imtFlight.departCode) : imtFlight.departCode != null) return false;
        if (departDate != null ? !departDate.equals(imtFlight.departDate) : imtFlight.departDate != null) return false;
        if (departPlace != null ? !departPlace.equals(imtFlight.departPlace) : imtFlight.departPlace != null)
            return false;
        if (departTime != null ? !departTime.equals(imtFlight.departTime) : imtFlight.departTime != null) return false;
        if (flightCabin != null ? !flightCabin.equals(imtFlight.flightCabin) : imtFlight.flightCabin != null)
            return false;
        if (flightNumber != null ? !flightNumber.equals(imtFlight.flightNumber) : imtFlight.flightNumber != null)
            return false;
        if (id != null ? !id.equals(imtFlight.id) : imtFlight.id != null) return false;
        if (meal != null ? !meal.equals(imtFlight.meal) : imtFlight.meal != null) return false;
        if (travelTime != null ? !travelTime.equals(imtFlight.travelTime) : imtFlight.travelTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = departTime != null ? departTime.hashCode() : 0;
        result = 31 * result + (departDate != null ? departDate.hashCode() : 0);
        result = 31 * result + (departPlace != null ? departPlace.hashCode() : 0);
        result = 31 * result + (departCode != null ? departCode.hashCode() : 0);
        result = 31 * result + (arriveTime != null ? arriveTime.hashCode() : 0);
        result = 31 * result + (arriveDate != null ? arriveDate.hashCode() : 0);
        result = 31 * result + (arrivePlace != null ? arrivePlace.hashCode() : 0);
        result = 31 * result + (arriveCode != null ? arriveCode.hashCode() : 0);
        result = 31 * result + (travelTime != null ? travelTime.hashCode() : 0);
        result = 31 * result + (flightNumber != null ? flightNumber.hashCode() : 0);
        result = 31 * result + (airlineCompany != null ? airlineCompany.hashCode() : 0);
        result = 31 * result + (aircraft != null ? aircraft.hashCode() : 0);
        result = 31 * result + (meal != null ? meal.hashCode() : 0);
        result = 31 * result + (flightCabin != null ? flightCabin.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IMTFlight{" +
                "departTime='" + departTime + '\'' +
                ", departDate='" + departDate + '\'' +
                ", departPlace='" + departPlace + '\'' +
                ", departCode='" + departCode + '\'' +
                ", arriveTime='" + arriveTime + '\'' +
                ", arriveDate='" + arriveDate + '\'' +
                ", arrivePlace='" + arrivePlace + '\'' +
                ", arriveCode='" + arriveCode + '\'' +
                ", travelTime='" + travelTime + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                ", airlineCompany='" + airlineCompany + '\'' +
                ", aircraft='" + aircraft + '\'' +
                ", meal='" + meal + '\'' +
                ", flightCabin='" + flightCabin + '\'' +
                ", id=" + id +
                '}';
    }
}
