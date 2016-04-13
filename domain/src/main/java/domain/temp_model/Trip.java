package domain.temp_model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @Column(name = "depart_code")
    private String depart_code;

    @Column(name = "arrive_code")
    private String arrive_code;

    @Column(name = "depart_place")
    private String depart_place;

    @Column(name = "arrive_place")
    private String arrive_place;

    @Column(name = "trip_date")
    private Date trip_date;
    //Добавить поля

    @Column(name = "trip_duration")
    private String trip_duration;

    @Column(name = "cost")
    private double cost;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public String getDepart_code() {
        return depart_code;
    }

    public void setDepart_code(String depart_code) {
        this.depart_code = depart_code;
    }

    public String getArrive_code() {
        return arrive_code;
    }

    public void setArrive_code(String arrive_code) {
        this.arrive_code = arrive_code;
    }

    public String getDepart_place() {
        return depart_place;
    }

    public void setDepart_place(String depart_place) {
        this.depart_place = depart_place;
    }

    public String getArrive_place() {
        return arrive_place;
    }

    public void setArrive_place(String arrive_place) {
        this.arrive_place = arrive_place;
    }

    public Date getTrip_date() {
        return trip_date;
    }

    public void setTrip_date(Date trip_date) {
        this.trip_date = trip_date;
    }

    public String getTrip_duration() {
        return trip_duration;
    }

    public void setTrip_duration(String trip_duration) {
        this.trip_duration = trip_duration;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
