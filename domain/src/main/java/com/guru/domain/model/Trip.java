package com.guru.domain.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Никита on 12.04.2016.
 */
@Entity
@Table(name = "trips")
public class Trip implements scala.Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

   /* @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "queries_id", nullable = false)
    private Query query;*/

    @Column(name = "query_id")
    private Long queryId;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "stops")
    private String stops;

    @Column(name = "cabins")
    private String cabins;

    @Column(name = "carriers")
    private String carriers;

    @Column(name = "layovers")
    private String layovers;

    @Column(name = "flight_legs")
    private String flightLegs;

    @Column(name = "flight_numbers")
    private String flightNumbers;

    @OneToMany(mappedBy = "trip", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Flight> flights = new ArrayList<>();

    @Transient
    private int direction;

    @Transient
    private List<ClasInfo> clasInfo = new ArrayList<>();

    @Transient
    private String clas;

    @Transient
    private Boolean isComplete;

    @Transient
    private Integer miles;

    @Transient
    private BigDecimal tax;

// для всех парсеров добавить miles tax


    public Trip() {
    }

    public Trip(Long id, String departCode, String arriveCode, String departPlace, String arrivePlace, Date tripDate, String tripDuration, BigDecimal cost) {
        this.id = id;
        this.departCode = departCode;
        this.arriveCode = arriveCode;
        this.departPlace = departPlace;
        this.arrivePlace = arrivePlace;
        this.tripDate = tripDate;
        this.tripDuration = tripDuration;
        this.cost = cost;
    }


    public Trip(Trip trip) {
        this.departCode = trip.getDepartCode();
        this.arriveCode = trip.getArriveCode();
        this.departPlace = trip.getDepartPlace();
        this.arrivePlace = trip.getArrivePlace();
        this.tripDate = trip.getTripDate();
        this.tripDuration = trip.getTripDuration();
        this.cost = trip.getCost();
        List<Flight> flights = trip.getFlights();
        List<Flight> thisFlights = new ArrayList<>();
        for(Flight flight : flights){
            thisFlights.add(new Flight(flight));
        }
        this.flights = thisFlights;
        this.queryId = trip.getQueryId();
        this.stops = trip.getStops();
        this.cabins = trip.getCabins();
        this.carriers = trip.getCarriers();
        this.layovers = trip.getLayovers();
        this.flightLegs = trip.getFlightLegs();
        this.flightNumbers = trip.getFlightNumbers();
        this.direction = trip.getDirection();
        this.miles = trip.getMiles();
        this.tax = trip.getTax();

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
                ", queryId=" + queryId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", stops='" + stops + '\'' +
                ", cabins='" + cabins + '\'' +
                ", carriers='" + carriers + '\'' +
                ", layovers='" + layovers + '\'' +
                ", flightLegs='" + flightLegs + '\'' +
                ", flightNumbers='" + flightNumbers + '\'' +
                ", flights=" + flights +
                ", direction=" + direction +
                ", clasInfo=" + clasInfo +
                ", clas='" + clas + '\'' +
                ", isComplete=" + isComplete +
                ", miles=" + miles +
                ", tax=" + tax +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


    public Long getQueryId() {
        return queryId;
    }

    public void setQueryId(Long queryId) {
        this.queryId = queryId;
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

    public String getStops() {
        return stops;
    }

    public void setStops(String stops) {
        this.stops = stops;
    }

    public String getCabins() {
        return cabins;
    }

    public void setCabins(String cabins) {
        this.cabins = cabins;
    }

    public String getCarriers() {
        return carriers;
    }

    public void setCarriers(String carriers) {
        this.carriers = carriers;
    }

    public String getLayovers() {
        return layovers;
    }

    public void setLayovers(String layovers) {
        this.layovers = layovers;
    }

    public String getFlightLegs() {
        return flightLegs;
    }

    public void setFlightLegs(String flightLegs) {
        this.flightLegs = flightLegs;
    }

    public String getFlightNumbers() {
        return flightNumbers;
    }

    public void setFlightNumbers(String flightNumbers) {
        this.flightNumbers = flightNumbers;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public List<ClasInfo> getClasInfo() {
        return clasInfo;
    }

    public void setClasInfo(List<ClasInfo> clasInfo) {
        this.clasInfo = clasInfo;
    }

    public String getClas() {
        return clas;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }

    public Integer getMiles() {
        return miles;
    }

    public void setMiles(Integer miles) {
        this.miles = miles;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trip trip = (Trip) o;

        if (direction != trip.direction) return false;
        if (id != null ? !id.equals(trip.id) : trip.id != null) return false;
        if (departCode != null ? !departCode.equals(trip.departCode) : trip.departCode != null) return false;
        if (arriveCode != null ? !arriveCode.equals(trip.arriveCode) : trip.arriveCode != null) return false;
        if (departPlace != null ? !departPlace.equals(trip.departPlace) : trip.departPlace != null) return false;
        if (arrivePlace != null ? !arrivePlace.equals(trip.arrivePlace) : trip.arrivePlace != null) return false;
        if (tripDate != null ? !tripDate.equals(trip.tripDate) : trip.tripDate != null) return false;
        if (tripDuration != null ? !tripDuration.equals(trip.tripDuration) : trip.tripDuration != null) return false;
        if (cost != null ? !cost.equals(trip.cost) : trip.cost != null) return false;
        if (queryId != null ? !queryId.equals(trip.queryId) : trip.queryId != null) return false;
        if (createdAt != null ? !createdAt.equals(trip.createdAt) : trip.createdAt != null) return false;
        if (updatedAt != null ? !updatedAt.equals(trip.updatedAt) : trip.updatedAt != null) return false;
        if (stops != null ? !stops.equals(trip.stops) : trip.stops != null) return false;
        if (cabins != null ? !cabins.equals(trip.cabins) : trip.cabins != null) return false;
        if (carriers != null ? !carriers.equals(trip.carriers) : trip.carriers != null) return false;
        if (layovers != null ? !layovers.equals(trip.layovers) : trip.layovers != null) return false;
        if (flightLegs != null ? !flightLegs.equals(trip.flightLegs) : trip.flightLegs != null) return false;
        if (flightNumbers != null ? !flightNumbers.equals(trip.flightNumbers) : trip.flightNumbers != null)
            return false;
        if (flights != null ? !flights.equals(trip.flights) : trip.flights != null) return false;
        if (clasInfo != null ? !clasInfo.equals(trip.clasInfo) : trip.clasInfo != null) return false;
        if (clas != null ? !clas.equals(trip.clas) : trip.clas != null) return false;
        if (isComplete != null ? !isComplete.equals(trip.isComplete) : trip.isComplete != null) return false;
        if (miles != null ? !miles.equals(trip.miles) : trip.miles != null) return false;
        return !(tax != null ? !tax.equals(trip.tax) : trip.tax != null);

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
        result = 31 * result + (queryId != null ? queryId.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        result = 31 * result + (stops != null ? stops.hashCode() : 0);
        result = 31 * result + (cabins != null ? cabins.hashCode() : 0);
        result = 31 * result + (carriers != null ? carriers.hashCode() : 0);
        result = 31 * result + (layovers != null ? layovers.hashCode() : 0);
        result = 31 * result + (flightLegs != null ? flightLegs.hashCode() : 0);
        result = 31 * result + (flightNumbers != null ? flightNumbers.hashCode() : 0);
        result = 31 * result + (flights != null ? flights.hashCode() : 0);
        result = 31 * result + direction;
        result = 31 * result + (clasInfo != null ? clasInfo.hashCode() : 0);
        result = 31 * result + (clas != null ? clas.hashCode() : 0);
        result = 31 * result + (isComplete != null ? isComplete.hashCode() : 0);
        result = 31 * result + (miles != null ? miles.hashCode() : 0);
        result = 31 * result + (tax != null ? tax.hashCode() : 0);
        return result;
    }

    public Boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Boolean isComplete) {
        this.isComplete = isComplete;
    }

}
