package com.guru.vo.view;

public class IMTInfo {

    public static final String ECONOMY = "E";
    public static final String BUSINESS = "B";
    public static final String FIRST = "F";
    public static final String PREMIUM_ECONOMY = "PE";

    public static final String SAVER = "SAVER";
    public static final String STANDART = "STANDART";
    public static final String FULL = "FULL";
    public static final String SAVER_STANDART = "SAVER STANDART";
    public static final String STANDART_FULL = "STANDART FULL";

    public static final String NA = "NA";
    public static final String AVAILABLE = "AVAILABLE";
    public static final String WAITLIST = "WAITLIST";

    private String name;
    private String status;
    private String mileage;
    private String tax;
    private String currency;
    private Integer id;

    public IMTInfo() {
    }

    public IMTInfo(String name, String status, String mileage, String tax, String currency, Integer id) {
        this.name = name;
        this.status = status;
        this.mileage = mileage;
        this.tax = tax;
        this.currency = currency;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IMTInfo imtInfo = (IMTInfo) o;

        if (currency != null ? !currency.equals(imtInfo.currency) : imtInfo.currency != null) return false;
        if (id != null ? !id.equals(imtInfo.id) : imtInfo.id != null) return false;
        if (mileage != null ? !mileage.equals(imtInfo.mileage) : imtInfo.mileage != null) return false;
        if (name != null ? !name.equals(imtInfo.name) : imtInfo.name != null) return false;
        if (status != null ? !status.equals(imtInfo.status) : imtInfo.status != null) return false;
        if (tax != null ? !tax.equals(imtInfo.tax) : imtInfo.tax != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (mileage != null ? mileage.hashCode() : 0);
        result = 31 * result + (tax != null ? tax.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IMTInfo{" +
                "name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", mileage='" + mileage + '\'' +
                ", tax='" + tax + '\'' +
                ", currency='" + currency + '\'' +
                ", id=" + id +
                '}';
    }
}
