package com.guru.vo.view;

import java.util.List;


public class IMTDataObject {

    private List<IMTAward> awardList;
    private IMTError error;

    public IMTDataObject() {
    }

    public IMTDataObject(List<IMTAward> awardList) {
        this.awardList = awardList;
    }

    public IMTError getError() {
        return error;
    }

    public void setError(IMTError error) {
        this.error = error;
    }

    public List<IMTAward> getAwardList() {
        return awardList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IMTDataObject that = (IMTDataObject) o;

        if (awardList != null ? !awardList.equals(that.awardList) : that.awardList != null) return false;
        if (error != null ? !error.equals(that.error) : that.error != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = awardList != null ? awardList.hashCode() : 0;
        result = 31 * result + (error != null ? error.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IMTDataObject{" +
                "awardList=" + awardList +
                ", error=" + error +
                '}';
    }
}
