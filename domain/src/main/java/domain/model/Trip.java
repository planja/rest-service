package domain.model;

import javax.persistence.*;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "queries_id", nullable = false)
    private Query query;

    @Column(name = "request_id")
    private Long requestId;

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

    @OneToMany(mappedBy = "query",fetch = FetchType.EAGER)
    private Set<Query> queries = new HashSet<>();

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

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
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

    public Set<Query> getQueries() {
        return queries;
    }

    public void setQueries(Set<Query> queries) {
        this.queries = queries;
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
        if (cost != null ? !cost.equals(trip.cost) : trip.cost != null) return false;
        if (requestId != null ? !requestId.equals(trip.requestId) : trip.requestId != null) return false;
        if (createdAt != null ? !createdAt.equals(trip.createdAt) : trip.createdAt != null) return false;
        if (updatedAt != null ? !updatedAt.equals(trip.updatedAt) : trip.updatedAt != null) return false;
        if (stops != null ? !stops.equals(trip.stops) : trip.stops != null) return false;
        if (cabins != null ? !cabins.equals(trip.cabins) : trip.cabins != null) return false;
        if (carriers != null ? !carriers.equals(trip.carriers) : trip.carriers != null) return false;
        if (layovers != null ? !layovers.equals(trip.layovers) : trip.layovers != null) return false;
        if (flightLegs != null ? !flightLegs.equals(trip.flightLegs) : trip.flightLegs != null) return false;
        return !(flightNumbers != null ? !flightNumbers.equals(trip.flightNumbers) : trip.flightNumbers != null);

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
        result = 31 * result + (requestId != null ? requestId.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        result = 31 * result + (stops != null ? stops.hashCode() : 0);
        result = 31 * result + (cabins != null ? cabins.hashCode() : 0);
        result = 31 * result + (carriers != null ? carriers.hashCode() : 0);
        result = 31 * result + (layovers != null ? layovers.hashCode() : 0);
        result = 31 * result + (flightLegs != null ? flightLegs.hashCode() : 0);
        result = 31 * result + (flightNumbers != null ? flightNumbers.hashCode() : 0);
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
                ", requestId=" + requestId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", stops='" + stops + '\'' +
                ", cabins='" + cabins + '\'' +
                ", carriers='" + carriers + '\'' +
                ", layovers='" + layovers + '\'' +
                ", flightLegs='" + flightLegs + '\'' +
                ", flightNumbers='" + flightNumbers + '\'' +
                '}';
    }
}
