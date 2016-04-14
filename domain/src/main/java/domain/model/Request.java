package domain.model;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Никита on 12.04.2016.
 */


@Entity
@Table(name = "requests")
public class Request {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alias", unique = true)
    private String alias;

    @Column(name = "type")
    private String type;

    @Column(name = "from")
    private String from;

    @Column(name = "to")
    private String to;

    @Column(name = "include_from_nearby")
    private Boolean includeFromNearby;

    @Column(name = "include_to_nearby")
    private Boolean includeToNearby;

    @Column(name = "departure")
    private Date departure;

    @Column(name = "flexible_departure")
    private boolean flexibleDeparture;

    @Column(name = "arrival")
    private Date arrival;

    @Column(name = "flexible_arrival")
    private Boolean flexibleArrival;

    @Column(name = "passengers")
    private Integer passengers;


    @Column(name = "status")
    private Integer status;

    @Column(name = "error")
    private Boolean error;


    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public Request() {
    }

    public Request(Long id, String alias, String type, String from, String to, Boolean includeFromNearby, Boolean includeToNearby, Date departure, boolean flexibleDeparture, Date arrival, Boolean flexibleArrival, Integer passengers, Integer status, Boolean error, Date createdAt, Date updatedAt) {
        this.id = id;
        this.alias = alias;
        this.type = type;
        this.from = from;
        this.to = to;
        this.includeFromNearby = includeFromNearby;
        this.includeToNearby = includeToNearby;
        this.departure = departure;
        this.flexibleDeparture = flexibleDeparture;
        this.arrival = arrival;
        this.flexibleArrival = flexibleArrival;
        this.passengers = passengers;
        this.status = status;
        this.error = error;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Boolean getIncludeFromNearby() {
        return includeFromNearby;
    }

    public void setIncludeFromNearby(Boolean includeFromNearby) {
        this.includeFromNearby = includeFromNearby;
    }

    public Boolean getIncludeToNearby() {
        return includeToNearby;
    }

    public void setIncludeToNearby(Boolean includeToNearby) {
        this.includeToNearby = includeToNearby;
    }

    public Date getDeparture() {
        return departure;
    }

    public void setDeparture(Date departure) {
        this.departure = departure;
    }

    public boolean isFlexibleDeparture() {
        return flexibleDeparture;
    }

    public void setFlexibleDeparture(boolean flexibleDeparture) {
        this.flexibleDeparture = flexibleDeparture;
    }

    public Date getArrival() {
        return arrival;
    }

    public void setArrival(Date arrival) {
        this.arrival = arrival;
    }

    public Boolean getFlexibleArrival() {
        return flexibleArrival;
    }

    public void setFlexibleArrival(Boolean flexibleArrival) {
        this.flexibleArrival = flexibleArrival;
    }

    public Integer getPassengers() {
        return passengers;
    }

    public void setPassengers(Integer passengers) {
        this.passengers = passengers;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        if (flexibleDeparture != request.flexibleDeparture) return false;
        if (id != null ? !id.equals(request.id) : request.id != null) return false;
        if (alias != null ? !alias.equals(request.alias) : request.alias != null) return false;
        if (type != null ? !type.equals(request.type) : request.type != null) return false;
        if (from != null ? !from.equals(request.from) : request.from != null) return false;
        if (to != null ? !to.equals(request.to) : request.to != null) return false;
        if (includeFromNearby != null ? !includeFromNearby.equals(request.includeFromNearby) : request.includeFromNearby != null)
            return false;
        if (includeToNearby != null ? !includeToNearby.equals(request.includeToNearby) : request.includeToNearby != null)
            return false;
        if (departure != null ? !departure.equals(request.departure) : request.departure != null) return false;
        if (arrival != null ? !arrival.equals(request.arrival) : request.arrival != null) return false;
        if (flexibleArrival != null ? !flexibleArrival.equals(request.flexibleArrival) : request.flexibleArrival != null)
            return false;
        if (passengers != null ? !passengers.equals(request.passengers) : request.passengers != null) return false;
        if (status != null ? !status.equals(request.status) : request.status != null) return false;
        if (error != null ? !error.equals(request.error) : request.error != null) return false;
        if (createdAt != null ? !createdAt.equals(request.createdAt) : request.createdAt != null) return false;
        return !(updatedAt != null ? !updatedAt.equals(request.updatedAt) : request.updatedAt != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (alias != null ? alias.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (includeFromNearby != null ? includeFromNearby.hashCode() : 0);
        result = 31 * result + (includeToNearby != null ? includeToNearby.hashCode() : 0);
        result = 31 * result + (departure != null ? departure.hashCode() : 0);
        result = 31 * result + (flexibleDeparture ? 1 : 0);
        result = 31 * result + (arrival != null ? arrival.hashCode() : 0);
        result = 31 * result + (flexibleArrival != null ? flexibleArrival.hashCode() : 0);
        result = 31 * result + (passengers != null ? passengers.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", alias='" + alias + '\'' +
                ", type='" + type + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", includeFromNearby=" + includeFromNearby +
                ", includeToNearby=" + includeToNearby +
                ", departure=" + departure +
                ", flexibleDeparture=" + flexibleDeparture +
                ", arrival=" + arrival +
                ", flexibleArrival=" + flexibleArrival +
                ", passengers=" + passengers +
                ", status=" + status +
                ", error=" + error +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
