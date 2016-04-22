package com.guru.domain.model;

import javax.persistence.*;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Никита on 12.04.2016.
 */

@Entity
@Table(name = "trips_costs")
public class TripCost {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(name = "miles")
    private Integer miles;

    @Column(name = "tax")
    private BigDecimal tax;

    @Column(name = "parser_cost")
    private BigDecimal parserCost;

    @Column(name = "aa_cost")
    private BigDecimal aaCost;

    @Column(name = "sq_cost")
    private BigDecimal sqCost;

    @Column(name = "nh_cost")
    private BigDecimal nhCost;

    @Column(name = "ey_cost")
    private BigDecimal eyCost;

    @Column(name = "lh_cost")
    private BigDecimal lhCost;

    @Column(name = "cx_cost")
    private BigDecimal cxCost;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public TripCost() {
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

    public BigDecimal getParserCost() {
        return parserCost;
    }

    public void setParserCost(BigDecimal parserCost) {
        this.parserCost = parserCost;
    }

    public BigDecimal getAaCost() {
        return aaCost;
    }

    public void setAaCost(BigDecimal aaCost) {
        this.aaCost = aaCost;
    }

    public BigDecimal getSqCost() {
        return sqCost;
    }

    public void setSqCost(BigDecimal sqCost) {
        this.sqCost = sqCost;
    }

    public BigDecimal getNhCost() {
        return nhCost;
    }

    public void setNhCost(BigDecimal nhCost) {
        this.nhCost = nhCost;
    }

    public BigDecimal getEyCost() {
        return eyCost;
    }

    public void setEyCost(BigDecimal eyCost) {
        this.eyCost = eyCost;
    }

    public BigDecimal getLhCost() {
        return lhCost;
    }

    public void setLhCost(BigDecimal lhCost) {
        this.lhCost = lhCost;
    }

    public BigDecimal getCxCost() {
        return cxCost;
    }

    public void setCxCost(BigDecimal cxCost) {
        this.cxCost = cxCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TripCost tripCost = (TripCost) o;

        if (id != null ? !id.equals(tripCost.id) : tripCost.id != null) return false;
        if (miles != null ? !miles.equals(tripCost.miles) : tripCost.miles != null) return false;
        if (tax != null ? !tax.equals(tripCost.tax) : tripCost.tax != null) return false;
        if (parserCost != null ? !parserCost.equals(tripCost.parserCost) : tripCost.parserCost != null) return false;
        if (aaCost != null ? !aaCost.equals(tripCost.aaCost) : tripCost.aaCost != null) return false;
        if (sqCost != null ? !sqCost.equals(tripCost.sqCost) : tripCost.sqCost != null) return false;
        if (nhCost != null ? !nhCost.equals(tripCost.nhCost) : tripCost.nhCost != null) return false;
        if (eyCost != null ? !eyCost.equals(tripCost.eyCost) : tripCost.eyCost != null) return false;
        if (lhCost != null ? !lhCost.equals(tripCost.lhCost) : tripCost.lhCost != null) return false;
        if (cxCost != null ? !cxCost.equals(tripCost.cxCost) : tripCost.cxCost != null) return false;
        if (createdAt != null ? !createdAt.equals(tripCost.createdAt) : tripCost.createdAt != null) return false;
        return !(updatedAt != null ? !updatedAt.equals(tripCost.updatedAt) : tripCost.updatedAt != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (miles != null ? miles.hashCode() : 0);
        result = 31 * result + (tax != null ? tax.hashCode() : 0);
        result = 31 * result + (parserCost != null ? parserCost.hashCode() : 0);
        result = 31 * result + (aaCost != null ? aaCost.hashCode() : 0);
        result = 31 * result + (sqCost != null ? sqCost.hashCode() : 0);
        result = 31 * result + (nhCost != null ? nhCost.hashCode() : 0);
        result = 31 * result + (eyCost != null ? eyCost.hashCode() : 0);
        result = 31 * result + (lhCost != null ? lhCost.hashCode() : 0);
        result = 31 * result + (cxCost != null ? cxCost.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TripCost{" +
                "id=" + id +
                ", miles=" + miles +
                ", tax=" + tax +
                ", parserCost=" + parserCost +
                ", aaCost=" + aaCost +
                ", sqCost=" + sqCost +
                ", nhCost=" + nhCost +
                ", eyCost=" + eyCost +
                ", lhCost=" + lhCost +
                ", cxCost=" + cxCost +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
