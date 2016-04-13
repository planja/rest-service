package domain.temp_model;

import javax.persistence.*;

@Entity
@Table(name = "trips_costs")
public class TripCost {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(name = "miles")
    private int miles;

    @Column(name = "tax")
    private double tax;

    @Column(name = "parser_cost")
    private double parser_cost;

    @Column(name = "aa_cost")
    private double aa_cost;

    @Column(name = "sq_cost")
    private double sq_cost;

    @Column(name = "nh_cost")
    private double nh_cost;

    @Column(name = "ey_cost")
    private double ey_cost;

    @Column(name = "lh_cost")
    private double lh_cost;

    @Column(name = "cx_cost")
    private double cx_cost;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public int getMiles() {
        return miles;
    }

    public void setMiles(int miles) {
        this.miles = miles;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getParser_cost() {
        return parser_cost;
    }

    public void setParser_cost(double parser_cost) {
        this.parser_cost = parser_cost;
    }

    public double getAa_cost() {
        return aa_cost;
    }

    public void setAa_cost(double aa_cost) {
        this.aa_cost = aa_cost;
    }

    public double getSq_cost() {
        return sq_cost;
    }

    public void setSq_cost(double sq_cost) {
        this.sq_cost = sq_cost;
    }

    public double getNh_cost() {
        return nh_cost;
    }

    public void setNh_cost(double nh_cost) {
        this.nh_cost = nh_cost;
    }

    public double getEy_cost() {
        return ey_cost;
    }

    public void setEy_cost(double ey_cost) {
        this.ey_cost = ey_cost;
    }

    public double getLh_cost() {
        return lh_cost;
    }

    public void setLh_cost(double lh_cost) {
        this.lh_cost = lh_cost;
    }

    public double getCx_cost() {
        return cx_cost;
    }

    public void setCx_cost(double cx_cost) {
        this.cx_cost = cx_cost;
    }
}
