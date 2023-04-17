package com.hoanglinhsama.ecommerce.model;

/**
 * Model dinh nghia thong bao nhan ve tu firebase
 */
public class NotificationReceiveData {
    private long multicastId;
    private int success;
    private int failure;
    private int canonicalIds;

    public NotificationReceiveData() {
    }

    public long getMulticastId() {
        return multicastId;
    }

    public void setMulticastId(long multicastId) {
        this.multicastId = multicastId;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public int getCanonicalIds() {
        return canonicalIds;
    }

    public void setCanonicalIds(int canonicalIds) {
        this.canonicalIds = canonicalIds;
    }
}
