package com.guru.domain.model;

import scala.Serializable;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Никита on 12.04.2016.
 */

@Entity
@Table(name = "flights")
public class Flight implements scala.Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "position")
    private Integer position;

    @Column(name = "parser")
    private String parser;

    @Column(name = "carrier_code")
    private String carrierCode;

    @Column(name = "carrier_name")
    private String carrierName;

    @Column(name = "flight_duration")
    private String flightDuration;

    @Column(name = "cabin")
    private String cabin;

    @Column(name = "depart_time")
    private String departTime;

    @Column(name = "depart_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date departDate;

    @Column(name = "depart_place")
    private String departPlace;

    @Column(name = "depart_code")
    private String departCode;

    @Column(name = "arrive_time")
    private String arriveTime;

    @Column(name = "arrive_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date arriveDate;

    @Column(name = "arrive_place")
    private String arrivePlace;

    @Column(name = "arrive_code")
    private String arriveCode;

    @Column(name = "flight_number")
    private String flightNumber;

    @Column(name = "layover")
    private String layover;

    @Column(name = "aircraft")
    private String aircraft;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trips_id", nullable = false)
    private Trip trip;


    private String url;

    public Flight() {
    }

    public Flight(Long id, Trip trip, Integer position, String parser, String carrierCode, String carrierName, String flightDuration, String cabin, String departTime, Date departDate, String departPlace, String departCode, String arriveTime, Date arriveDate, String arrivePlace, String arriveCode, String flightNumber, String layover, String aircraft, Date createdAt, Date updatedAt) {
        this.id = id;
        this.trip = trip;
        this.position = position;
        this.parser = parser;
        this.carrierCode = carrierCode;
        this.carrierName = carrierName;
        this.flightDuration = flightDuration;
        this.cabin = cabin;
        this.departTime = departTime;
        this.departDate = departDate;
        this.departPlace = departPlace;
        this.departCode = departCode;
        this.arriveTime = arriveTime;
        this.arriveDate = arriveDate;
        this.arrivePlace = arrivePlace;
        this.arriveCode = arriveCode;
        this.flightNumber = flightNumber;
        this.layover = layover;
        this.aircraft = aircraft;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getParser() {
        return parser;
    }

    public void setParser(String parser) {
        this.parser = parser;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(String flightDuration) {
        this.flightDuration = flightDuration;
    }

    public String getCabin() {
        return cabin;
    }

    public void setCabin(String cabin) {
        this.cabin = cabin;
    }

    public String getDepartTime() {
        return departTime;
    }

    public void setDepartTime(String departTime) {
        this.departTime = departTime;
    }

    public Date getDepartDate() {
        return departDate;
    }

    public void setDepartDate(Date departDate) {
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

    public Date getArriveDate() {
        return arriveDate;
    }

    public void setArriveDate(Date arriveDate) {
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

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getLayover() {
        return layover;
    }

    public void setLayover(String layover) {
        this.layover = layover;
    }

    public String getAircraft() {
        return aircraft;
    }

    public void setAircraft(String aircraft) {
        this.aircraft = aircraft;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Flight flight = (Flight) o;

        if (id != null ? !id.equals(flight.id) : flight.id != null) return false;
        if (position != null ? !position.equals(flight.position) : flight.position != null) return false;
        if (parser != null ? !parser.equals(flight.parser) : flight.parser != null) return false;
        if (carrierCode != null ? !carrierCode.equals(flight.carrierCode) : flight.carrierCode != null) return false;
        if (carrierName != null ? !carrierName.equals(flight.carrierName) : flight.carrierName != null) return false;
        if (flightDuration != null ? !flightDuration.equals(flight.flightDuration) : flight.flightDuration != null)
            return false;
        if (cabin != null ? !cabin.equals(flight.cabin) : flight.cabin != null) return false;
        if (departTime != null ? !departTime.equals(flight.departTime) : flight.departTime != null) return false;
        if (departDate != null ? !departDate.equals(flight.departDate) : flight.departDate != null) return false;
        if (departPlace != null ? !departPlace.equals(flight.departPlace) : flight.departPlace != null) return false;
        if (departCode != null ? !departCode.equals(flight.departCode) : flight.departCode != null) return false;
        if (arriveTime != null ? !arriveTime.equals(flight.arriveTime) : flight.arriveTime != null) return false;
        if (arriveDate != null ? !arriveDate.equals(flight.arriveDate) : flight.arriveDate != null) return false;
        if (arrivePlace != null ? !arrivePlace.equals(flight.arrivePlace) : flight.arrivePlace != null) return false;
        if (arriveCode != null ? !arriveCode.equals(flight.arriveCode) : flight.arriveCode != null) return false;
        if (flightNumber != null ? !flightNumber.equals(flight.flightNumber) : flight.flightNumber != null)
            return false;
        if (layover != null ? !layover.equals(flight.layover) : flight.layover != null) return false;
        if (aircraft != null ? !aircraft.equals(flight.aircraft) : flight.aircraft != null) return false;
        if (createdAt != null ? !createdAt.equals(flight.createdAt) : flight.createdAt != null) return false;
        return !(updatedAt != null ? !updatedAt.equals(flight.updatedAt) : flight.updatedAt != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (parser != null ? parser.hashCode() : 0);
        result = 31 * result + (carrierCode != null ? carrierCode.hashCode() : 0);
        result = 31 * result + (carrierName != null ? carrierName.hashCode() : 0);
        result = 31 * result + (flightDuration != null ? flightDuration.hashCode() : 0);
        result = 31 * result + (cabin != null ? cabin.hashCode() : 0);
        result = 31 * result + (departTime != null ? departTime.hashCode() : 0);
        result = 31 * result + (departDate != null ? departDate.hashCode() : 0);
        result = 31 * result + (departPlace != null ? departPlace.hashCode() : 0);
        result = 31 * result + (departCode != null ? departCode.hashCode() : 0);
        result = 31 * result + (arriveTime != null ? arriveTime.hashCode() : 0);
        result = 31 * result + (arriveDate != null ? arriveDate.hashCode() : 0);
        result = 31 * result + (arrivePlace != null ? arrivePlace.hashCode() : 0);
        result = 31 * result + (arriveCode != null ? arriveCode.hashCode() : 0);
        result = 31 * result + (flightNumber != null ? flightNumber.hashCode() : 0);
        result = 31 * result + (layover != null ? layover.hashCode() : 0);
        result = 31 * result + (aircraft != null ? aircraft.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", position=" + position +
                ", parser='" + parser + '\'' +
                ", carrierCode='" + carrierCode + '\'' +
                ", carrierName='" + carrierName + '\'' +
                ", flightDuration='" + flightDuration + '\'' +
                ", cabin='" + cabin + '\'' +
                ", departTime='" + departTime + '\'' +
                ", departDate=" + departDate +
                ", departPlace='" + departPlace + '\'' +
                ", departCode='" + departCode + '\'' +
                ", arriveTime='" + arriveTime + '\'' +
                ", arriveDate=" + arriveDate +
                ", arrivePlace='" + arrivePlace + '\'' +
                ", arriveCode='" + arriveCode + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                ", layover='" + layover + '\'' +
                ", aircraft='" + aircraft + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
