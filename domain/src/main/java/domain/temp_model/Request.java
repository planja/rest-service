package domain.temp_model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "requests")
public class Request {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "alias", unique = true)
    private String alias;

    @Column(name = "type")
    private String type;

    @Column(name = "from")
    private String from;

    @Column(name = "to")
    private String to;

    @Column(name = "include_from_nearby")
    private boolean include_from_nearby;

    @Column(name = "include_to_nearby")
    private boolean include_to_nearby;

    @Column(name = "departure")
    private Date departure;

    @Column(name = "flexible_departure")
    private boolean flexible_departure;

    @Column(name = "arrival")
    private Date arrival;

    @Column(name = "flexible_arrival")
    private boolean flexible_arrival;

    @Column(name = "passengers")
    private int passengers;

    //Добавить 2 поля

    @Column(name = "status")
    private int status;

    @Column(name = "error")
    private boolean error;


    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "updated_at")
    private Date updated_at;


    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public boolean isInclude_from_nearby() {
        return include_from_nearby;
    }

    public void setInclude_from_nearby(boolean include_from_nearby) {
        this.include_from_nearby = include_from_nearby;
    }

    public boolean isInclude_to_nearby() {
        return include_to_nearby;
    }

    public void setInclude_to_nearby(boolean include_to_nearby) {
        this.include_to_nearby = include_to_nearby;
    }

    public Date getDeparture() {
        return departure;
    }

    public void setDeparture(Date departure) {
        this.departure = departure;
    }

    public boolean isFlexible_departure() {
        return flexible_departure;
    }

    public void setFlexible_departure(boolean flexible_departure) {
        this.flexible_departure = flexible_departure;
    }

    public Date getArrival() {
        return arrival;
    }

    public void setArrival(Date arrival) {
        this.arrival = arrival;
    }

    public boolean isFlexible_arrival() {
        return flexible_arrival;
    }

    public void setFlexible_arrival(boolean flexible_arrival) {
        this.flexible_arrival = flexible_arrival;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
}
