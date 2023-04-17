package com.hoanglinhsama.ecommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("totalPrice")
    @Expose
    private long totalPrice;
    @SerializedName("arrayProduct")
    @Expose
    private List<ProductOrder> listProductOrder;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("address")
    @Expose
    private String address;

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ProductOrder> getListProductOrder() {
        return this.listProductOrder;
    }

    public void setListProductOrder(List<ProductOrder> listProductOrder) {
        this.listProductOrder = listProductOrder;
    }

}