package com.hoanglinhsama.ecommerce.model;

import java.util.Map;

/**
 * Model dinh nghia thong bao gui len FCM firebase
 */
public class NotificationSendData {
    private String to;
    private Map<String, String> notification;

    public NotificationSendData(String to, Map<String, String> notification) {
        this.to = to;
        this.notification = notification;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Map<String, String> getNotification() {
        return this.notification;
    }

    public void setNotification(Map<String, String> notification) {
        this.notification = notification;
    }
}
