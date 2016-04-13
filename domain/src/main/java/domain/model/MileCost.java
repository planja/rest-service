package domain.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "mile_costs")
public class MileCost {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "parser")
    private String parser;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatesAt;

    public MileCost() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParser() {
        return parser;
    }

    public void setParser(String parser) {
        this.parser = parser;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
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

        MileCost mileCost = (MileCost) o;

        if (cost != null ? !cost.equals(mileCost.cost) : mileCost.cost != null) return false;
        if (createdAt != null ? !createdAt.equals(mileCost.createdAt) : mileCost.createdAt != null) return false;
        if (id != null ? !id.equals(mileCost.id) : mileCost.id != null) return false;
        if (parser != null ? !parser.equals(mileCost.parser) : mileCost.parser != null) return false;
        if (updatesAt != null ? !updatesAt.equals(mileCost.updatesAt) : mileCost.updatesAt != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (parser != null ? parser.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatesAt != null ? updatesAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MileCost{" +
                "id=" + id +
                ", parser='" + parser + '\'' +
                ", cost=" + cost +
                ", createdAt=" + createdAt +
                ", updatesAt=" + updatesAt +
                '}';
    }
}
