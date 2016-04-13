package domain.temp_model;

import javax.persistence.*;

@Entity
@Table(name = "zones")
public class Zone {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "code")
    private String code;

    @Column(name = "mileage_region")
    private String mileage_region;

    @Column(name = "region")
    private String region;

    @Column(name = "aa_region")
    private String aa_region;

    @Column(name = "sq_region")
    private String sq_region;

    @Column(name = "nh_region")
    private String nh_region;

    @Column(name = "lh_region")
    private String lh_region;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getAa_region() {
        return aa_region;
    }

    public void setAa_region(String aa_region) {
        this.aa_region = aa_region;
    }

    public String getSq_region() {
        return sq_region;
    }

    public void setSq_region(String sq_region) {
        this.sq_region = sq_region;
    }

    public String getNh_region() {
        return nh_region;
    }

    public void setNh_region(String nh_region) {
        this.nh_region = nh_region;
    }

    public String getLh_region() {
        return lh_region;
    }

    public void setLh_region(String lh_region) {
        this.lh_region = lh_region;
    }
}
