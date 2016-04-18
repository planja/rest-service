package com.guru.domain.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "queries")
public class Query implements Serializable {

    private static final long serialVersionUID = 42L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alias")
    private String alias;

    @Column(name = "type")
    private String type;

    @Column(name = "from")
    private String from;

    @Column(name = "to")
    private String to;

    @Column(name = "include_from_nearby")
    private Integer includeFromNearby;

    @Column(name = "include_to_nearby")
    private Integer includeToNearby;

    @Column(name = "departure")
    private Date departure;

    @Column(name = "flexible_departure")
    private Integer flexibleDeparture;

    @Column(name = "arrival")
    private Date arrival;

    @Column(name = "flexible_arrival")
    private Integer flexibleArrival;

    @Column(name = "passengers")
    private Integer passengers;

    @Column(name = "classes")
    private String classes;

    @Column(name = "parsers")
    private String parsers;

    @Column(name = "status")
    private Integer status;

    @Column(name = "error")
    private Integer error;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @OneToMany(mappedBy = "query", fetch = FetchType.EAGER)
    private Set<ParserAnswer> parserAnswers = new HashSet<>();

    @OneToMany(mappedBy = "query", fetch = FetchType.EAGER)
    private Set<ParserError> parserErrors = new HashSet<>();

    public Query() {
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

    public Integer getIncludeFromNearby() {
        return includeFromNearby;
    }

    public void setIncludeFromNearby(Integer includeFromNearby) {
        this.includeFromNearby = includeFromNearby;
    }

    public Integer getIncludeToNearby() {
        return includeToNearby;
    }

    public void setIncludeToNearby(Integer includeToNearby) {
        this.includeToNearby = includeToNearby;
    }

    public Date getDeparture() {
        return departure;
    }

    public void setDeparture(Date departure) {
        this.departure = departure;
    }

    public Integer getFlexibleDeparture() {
        return flexibleDeparture;
    }

    public void setFlexibleDeparture(Integer flexibleDeparture) {
        this.flexibleDeparture = flexibleDeparture;
    }

    public Date getArrival() {
        return arrival;
    }

    public void setArrival(Date arrival) {
        this.arrival = arrival;
    }

    public Integer getFlexibleArrival() {
        return flexibleArrival;
    }

    public void setFlexibleArrival(Integer flexibleArrival) {
        this.flexibleArrival = flexibleArrival;
    }

    public Integer getPassengers() {
        return passengers;
    }

    public void setPassengers(Integer passengers) {
        this.passengers = passengers;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getParsers() {
        return parsers;
    }

    public void setParsers(String parsers) {
        this.parsers = parsers;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
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

    public Set<ParserAnswer> getParserAnswers() {
        return parserAnswers;
    }

    public void setParserAnswers(Set<ParserAnswer> parserAnswers) {
        this.parserAnswers = parserAnswers;
    }

    public Set<ParserError> getParserErrors() {
        return parserErrors;
    }

    public void setParserErrors(Set<ParserError> parserErrors) {
        this.parserErrors = parserErrors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Query query = (Query) o;

        if (alias != null ? !alias.equals(query.alias) : query.alias != null) return false;
        if (arrival != null ? !arrival.equals(query.arrival) : query.arrival != null) return false;
        if (classes != null ? !classes.equals(query.classes) : query.classes != null) return false;
        if (createdAt != null ? !createdAt.equals(query.createdAt) : query.createdAt != null) return false;
        if (departure != null ? !departure.equals(query.departure) : query.departure != null) return false;
        if (error != null ? !error.equals(query.error) : query.error != null) return false;
        if (flexibleArrival != null ? !flexibleArrival.equals(query.flexibleArrival) : query.flexibleArrival != null)
            return false;
        if (flexibleDeparture != null ? !flexibleDeparture.equals(query.flexibleDeparture) : query.flexibleDeparture != null)
            return false;
        if (from != null ? !from.equals(query.from) : query.from != null) return false;
        if (id != null ? !id.equals(query.id) : query.id != null) return false;
        if (includeFromNearby != null ? !includeFromNearby.equals(query.includeFromNearby) : query.includeFromNearby != null)
            return false;
        if (includeToNearby != null ? !includeToNearby.equals(query.includeToNearby) : query.includeToNearby != null)
            return false;
        if (parsers != null ? !parsers.equals(query.parsers) : query.parsers != null) return false;
        if (passengers != null ? !passengers.equals(query.passengers) : query.passengers != null) return false;
        if (status != null ? !status.equals(query.status) : query.status != null) return false;
        if (to != null ? !to.equals(query.to) : query.to != null) return false;
        if (type != null ? !type.equals(query.type) : query.type != null) return false;
        if (updatedAt != null ? !updatedAt.equals(query.updatedAt) : query.updatedAt != null) return false;

        return true;
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
        result = 31 * result + (flexibleDeparture != null ? flexibleDeparture.hashCode() : 0);
        result = 31 * result + (arrival != null ? arrival.hashCode() : 0);
        result = 31 * result + (flexibleArrival != null ? flexibleArrival.hashCode() : 0);
        result = 31 * result + (passengers != null ? passengers.hashCode() : 0);
        result = 31 * result + (classes != null ? classes.hashCode() : 0);
        result = 31 * result + (parsers != null ? parsers.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Query{" +
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
                ", classes='" + classes + '\'' +
                ", parsers='" + parsers + '\'' +
                ", status=" + status +
                ", error=" + error +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
