package com.guru.vo.view;

import java.util.List;

public class IMTAward {

    private boolean split;
    private List<IMTInfo> classList;
    private List<IMTFlight> flightList;
    private String totalDuration;
    private List<ExtraData> extraData;

    public IMTAward() {
    }

    public IMTAward(boolean split, List<IMTInfo> classList, List<IMTFlight> flightList, String totalDuration, List<ExtraData> extraData) {
        this.split = split;
        this.classList = classList;
        this.flightList = flightList;
        this.totalDuration = totalDuration;
        this.extraData = extraData;
    }

    public boolean isSplit() {
        return split;
    }

    public void setSplit(boolean split) {
        this.split = split;
    }

    public List<IMTInfo> getClassList() {
        return classList;
    }

    public List<IMTFlight> getFlightList() {
        return flightList;
    }

    public String getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(String totalDuration) {
        this.totalDuration = totalDuration;
    }

    public List<ExtraData> getExtraData() {
        return extraData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IMTAward imtAward = (IMTAward) o;

        if (split != imtAward.split) return false;
        if (classList != null ? !classList.equals(imtAward.classList) : imtAward.classList != null) return false;
        if (extraData != null ? !extraData.equals(imtAward.extraData) : imtAward.extraData != null) return false;
        if (flightList != null ? !flightList.equals(imtAward.flightList) : imtAward.flightList != null) return false;
        if (totalDuration != null ? !totalDuration.equals(imtAward.totalDuration) : imtAward.totalDuration != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (split ? 1 : 0);
        result = 31 * result + (classList != null ? classList.hashCode() : 0);
        result = 31 * result + (flightList != null ? flightList.hashCode() : 0);
        result = 31 * result + (totalDuration != null ? totalDuration.hashCode() : 0);
        result = 31 * result + (extraData != null ? extraData.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IMTAward{" +
                "split=" + split +
                ", classList=" + classList +
                ", flightList=" + flightList +
                ", totalDuration='" + totalDuration + '\'' +
                ", extraData=" + extraData +
                '}';
    }
}
