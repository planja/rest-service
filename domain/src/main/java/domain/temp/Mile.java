package domain.temp;

import javax.persistence.*;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Никита on 12.04.2016.
 */

@Entity
@Table(name = "miles")
public class Mile {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from")
    private String from;

    @Column(name = "to")
    private String to;

    @Column(name = "air_company")
    private String airCompany;

    @Column(name = "class")
    private String clas;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "tax")
    private BigDecimal tax;

    @Column(name = "tax_out")
    private BigDecimal taxOut;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatesAt;

    public Mile() {
    }

    public Mile(Long id,String from, String to, String airCompany, String clas, BigDecimal cost, BigDecimal tax, BigDecimal taxOut, Date createdAt, Date updatesAt) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.airCompany = airCompany;
        this.clas = clas;
        this.cost = cost;
        this.tax = tax;
        this.taxOut = taxOut;
        this.createdAt = createdAt;
        this.updatesAt = updatesAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getAirCompany() {
        return airCompany;
    }

    public void setAirCompany(String airCompany) {
        this.airCompany = airCompany;
    }

    public String getClas() {
        return clas;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTaxOut() {
        return taxOut;
    }

    public void setTaxOut(BigDecimal taxOut) {
        this.taxOut = taxOut;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatesAt() {
        return updatesAt;
    }

    public void setUpdatesAt(Date updatesAt) {
        this.updatesAt = updatesAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mile mile = (Mile) o;

        if (id != null ? !id.equals(mile.id) : mile.id != null) return false;
        if (from != null ? !from.equals(mile.from) : mile.from != null) return false;
        if (to != null ? !to.equals(mile.to) : mile.to != null) return false;
        if (airCompany != null ? !airCompany.equals(mile.airCompany) : mile.airCompany != null) return false;
        if (clas != null ? !clas.equals(mile.clas) : mile.clas != null) return false;
        if (cost != null ? !cost.equals(mile.cost) : mile.cost != null) return false;
        if (tax != null ? !tax.equals(mile.tax) : mile.tax != null) return false;
        if (taxOut != null ? !taxOut.equals(mile.taxOut) : mile.taxOut != null) return false;
        if (createdAt != null ? !createdAt.equals(mile.createdAt) : mile.createdAt != null) return false;
        return !(updatesAt != null ? !updatesAt.equals(mile.updatesAt) : mile.updatesAt != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (airCompany != null ? airCompany.hashCode() : 0);
        result = 31 * result + (clas != null ? clas.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (tax != null ? tax.hashCode() : 0);
        result = 31 * result + (taxOut != null ? taxOut.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatesAt != null ? updatesAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Mile{" +
                "id=" + id +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", airCompany='" + airCompany + '\'' +
                ", clas='" + clas + '\'' +
                ", cost=" + cost +
                ", tax=" + tax +
                ", taxOut=" + taxOut +
                ", createdAt=" + createdAt +
                ", updatesAt=" + updatesAt +
                '}';
    }
}
