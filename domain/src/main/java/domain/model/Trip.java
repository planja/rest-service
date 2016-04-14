package domain.model;

import javax.persistence.*;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Никита on 12.04.2016.
 */
@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @Column(name = "depart_code")
    private String departCode;

    @Column(name = "arrive_code")
    private String arriveCode;

    @Column(name = "depart_place")
    private String departPlace;

    @Column(name = "arrive_place")
    private String arrivePlace;

    @Column(name = "trip_date")
    private Date tripDate;

    @Column(name = "trip_duration")
    private String tripDuration;

    @Column(name = "cost")
    private BigDecimal cost;

    public Trip() {
    }

    public Trip(Long id, Request request, String departCode, String arriveCode, String departPlace, String arrivePlace, Date tripDate, String tripDuration, BigDecimal cost) {
        this.id = id;
        this.request = request;
        this.departCode = departCode;
        this.arriveCode = arriveCode;
        this.departPlace = departPlace;
        this.arrivePlace = arrivePlace;
        this.tripDate = tripDate;
        this.tripDuration = tripDuration;
        this.cost = cost;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public String getDepartCode() {
        return departCode;
    }

    public void setDepartCode(String departCode) {
        this.departCode = departCode;
    }

    public String getArriveCode() {
        return arriveCode;
    }

    public void setArriveCode(String arriveCode) {
        this.arriveCode = arriveCode;
    }

    public String getDepartPlace() {
        return departPlace;
    }

    public void setDepartPlace(String departPlace) {
        this.departPlace = departPlace;
    }

    public String getArrivePlace() {
        return arrivePlace;
    }

    public void setArrivePlace(String arrivePlace) {
        this.arrivePlace = arrivePlace;
    }

    public Date getTripDate() {
        return tripDate;
    }

    public void setTripDate(Date tripDate) {
        this.tripDate = tripDate;
    }

    public String getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(String tripDuration) {
        this.tripDuration = tripDuration;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trip trip = (Trip) o;

        if (id != null ? !id.equals(trip.id) : trip.id != null) return false;
        if (departCode != null ? !departCode.equals(trip.departCode) : trip.departCode != null) return false;
        if (arriveCode != null ? !arriveCode.equals(trip.arriveCode) : trip.arriveCode != null) return false;
        if (departPlace != null ? !departPlace.equals(trip.departPlace) : trip.departPlace != null) return false;
        if (arrivePlace != null ? !arrivePlace.equals(trip.arrivePlace) : trip.arrivePlace != null) return false;
        if (tripDate != null ? !tripDate.equals(trip.tripDate) : trip.tripDate != null) return false;
        if (tripDuration != null ? !tripDuration.equals(trip.tripDuration) : trip.tripDuration != null) return false;
        return !(cost != null ? !cost.equals(trip.cost) : trip.cost != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (departCode != null ? departCode.hashCode() : 0);
        result = 31 * result + (arriveCode != null ? arriveCode.hashCode() : 0);
        result = 31 * result + (departPlace != null ? departPlace.hashCode() : 0);
        result = 31 * result + (arrivePlace != null ? arrivePlace.hashCode() : 0);
        result = 31 * result + (tripDate != null ? tripDate.hashCode() : 0);
        result = 31 * result + (tripDuration != null ? tripDuration.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", departCode='" + departCode + '\'' +
                ", arriveCode='" + arriveCode + '\'' +
                ", departPlace='" + departPlace + '\'' +
                ", arrivePlace='" + arrivePlace + '\'' +
                ", tripDate=" + tripDate +
                ", tripDuration='" + tripDuration + '\'' +
                ", cost=" + cost +
                '}';
    }
}
