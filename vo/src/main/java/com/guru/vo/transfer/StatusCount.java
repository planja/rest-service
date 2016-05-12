package com.guru.vo.transfer;

/**
 * Created by Никита on 07.05.2016.
 */
public class StatusCount {

    private Long queryId;
    private int currentStatus;
    private int maxStatus;

    public StatusCount(Long queryId, int currentStatus, int maxStatus) {
        this.queryId = queryId;
        this.currentStatus = currentStatus;
        this.maxStatus = maxStatus;
    }

    public Long getQueryId() {
        return queryId;
    }

    public void setQueryId(Long queryId) {
        this.queryId = queryId;
    }

    public int getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(int currentStatus) {
        this.currentStatus = currentStatus;
    }

    public int getMaxStatus() {
        return maxStatus;
    }

    public void setMaxStatus(int maxStatus) {
        this.maxStatus = maxStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatusCount that = (StatusCount) o;

        if (currentStatus != that.currentStatus) return false;
        if (maxStatus != that.maxStatus) return false;
        return !(queryId != null ? !queryId.equals(that.queryId) : that.queryId != null);

    }

    @Override
    public int hashCode() {
        int result = queryId != null ? queryId.hashCode() : 0;
        result = 31 * result + currentStatus;
        result = 31 * result + maxStatus;
        return result;
    }
}
