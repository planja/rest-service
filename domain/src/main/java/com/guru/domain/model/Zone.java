package com.guru.domain.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Никита on 12.04.2016.
 */

@Entity
@Table(name = "zones")
public class Zone {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "mileage_region")
    private String mileage_region;

    @Column(name = "region")
    private String region;

    @Column(name = "aa_region")
    private String aaRegion;

    @Column(name = "sq_region")
    private String sqRegion;

    @Column(name = "nh_region")
    private String nhRegion;

    @Column(name = "lh_region")
    private String lhRegion;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;


    public Zone() {
    }

    public Zone(Long id, String code, String mileage_region, String region, String aaRegion, String sqRegion, String nhRegion, String lhRegion) {
        this.id = id;
        this.code = code;
        this.mileage_region = mileage_region;
        this.region = region;
        this.aaRegion = aaRegion;
        this.sqRegion = sqRegion;
        this.nhRegion = nhRegion;
        this.lhRegion = lhRegion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMileage_region() {
        return mileage_region;
    }

    public void setMileage_region(String mileage_region) {
        this.mileage_region = mileage_region;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAaRegion() {
        return aaRegion;
    }

    public void setAaRegion(String aaRegion) {
        this.aaRegion = aaRegion;
    }

    public String getSqRegion() {
        return sqRegion;
    }

    public void setSqRegion(String sqRegion) {
        this.sqRegion = sqRegion;
    }

    public String getNhRegion() {
        return nhRegion;
    }

    public void setNhRegion(String nhRegion) {
        this.nhRegion = nhRegion;
    }

    public String getLhRegion() {
        return lhRegion;
    }

    public void setLhRegion(String lhRegion) {
        this.lhRegion = lhRegion;
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

        Zone zone = (Zone) o;

        if (id != null ? !id.equals(zone.id) : zone.id != null) return false;
        if (code != null ? !code.equals(zone.code) : zone.code != null) return false;
        if (mileage_region != null ? !mileage_region.equals(zone.mileage_region) : zone.mileage_region != null)
            return false;
        if (region != null ? !region.equals(zone.region) : zone.region != null) return false;
        if (aaRegion != null ? !aaRegion.equals(zone.aaRegion) : zone.aaRegion != null) return false;
        if (sqRegion != null ? !sqRegion.equals(zone.sqRegion) : zone.sqRegion != null) return false;
        if (nhRegion != null ? !nhRegion.equals(zone.nhRegion) : zone.nhRegion != null) return false;
        if (lhRegion != null ? !lhRegion.equals(zone.lhRegion) : zone.lhRegion != null) return false;
        if (createdAt != null ? !createdAt.equals(zone.createdAt) : zone.createdAt != null) return false;
        return !(updatedAt != null ? !updatedAt.equals(zone.updatedAt) : zone.updatedAt != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (mileage_region != null ? mileage_region.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (aaRegion != null ? aaRegion.hashCode() : 0);
        result = 31 * result + (sqRegion != null ? sqRegion.hashCode() : 0);
        result = 31 * result + (nhRegion != null ? nhRegion.hashCode() : 0);
        result = 31 * result + (lhRegion != null ? lhRegion.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Zone{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", mileage_region='" + mileage_region + '\'' +
                ", region='" + region + '\'' +
                ", aaRegion='" + aaRegion + '\'' +
                ", sqRegion='" + sqRegion + '\'' +
                ", nhRegion='" + nhRegion + '\'' +
                ", lhRegion='" + lhRegion + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
