package domain.temp_model;

import javax.persistence.*;

@Entity
@Table(name = "miles")
public class Mile {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "from")
    private String from;

    @Column(name = "to")
    private String to;

    @Column(name = "air_company")
    private String air_company;

    @Column(name = "class")
    private String clas;

    @Column(name = "cost")
    private double cost;

    @Column(name = "tax")
    private double tax;

    @Column(name = "tax_out")
    private double tax_out;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getAir_company() {
        return air_company;
    }

    public void setAir_company(String air_company) {
        this.air_company = air_company;
    }

    public String getClas() {
        return clas;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTax_out() {
        return tax_out;
    }

    public void setTax_out(double tax_out) {
        this.tax_out = tax_out;
    }
}
