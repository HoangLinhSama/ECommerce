package com.hoanglinhsama.ecommerce.model;

import java.util.Date;

/**
 * Dinh nghia tin nhan cua ca nguoi gui va nguoi nhan duoc gui len firebase cloud firestore
 */
public class ChatMessage {
    private String sendId;
    private String receiveId;
    private String contentMessage;
    private String dateTime;
    private Date dateObject;

    public String getSendId() {
        return this.sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getReceiveId() {
        return this.receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public String getContentMessage() {
        return this.contentMessage;
    }

    public void setContentMessage(String contentMessage) {
        this.contentMessage = contentMessage;
    }

    public String getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Date getDateObject() {
        return this.dateObject;
    }

    public void setDateObject(Date dateObject) {
        this.dateObject = dateObject;
    }
}
