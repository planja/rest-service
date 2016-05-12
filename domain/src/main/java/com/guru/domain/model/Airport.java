package com.guru.domain.model;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Anton on 05.05.2016.
 */
@Entity
@Table(name = "airports")
public class Airport {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "airport")
    private String airport;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "code_iata")
    private String codeIata;

    @Column(name = "code_ikao")
    private String codeIkao;

    @Column(name = "lat")
    private BigDecimal lat;

    @Column(name = "lon")
    private BigDecimal lon;

    public Airport() {
    }

    public Airport(String airport, String city, String country, String codeIata, String codeIkao, BigDecimal lat, BigDecimal lon) {

        this.airport = airport;
        this.city = city;
        this.country = country;
        this.codeIata = codeIata;
        this.codeIkao = codeIkao;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "Airport{" +
                "id=" + id +
                ", airport='" + airport + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", codeIata='" + codeIata + '\'' +
                ", codeIkao='" + codeIkao + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Airport airport1 = (Airport) o;

        if (!id.equals(airport1.id)) return false;
        if (!airport.equals(airport1.airport)) return false;
        if (!city.equals(airport1.city)) return false;
        if (!country.equals(airport1.country)) return false;
        if (!lat.equals(airport1.lat)) return false;
        return lon.equals(airport1.lon);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + airport.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + country.hashCode();
        result = 31 * result + lat.hashCode();
        result = 31 * result + lon.hashCode();
        return result;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCodeIata() {
        return codeIata;
    }

    public void setCodeIata(String codeIata) {
        this.codeIata = codeIata;
    }

    public String getCodeIkao() {
        return codeIkao;
    }

    public void setCodeIkao(String codeIkao) {
        this.codeIkao = codeIkao;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }


}
