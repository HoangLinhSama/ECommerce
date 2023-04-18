package com.hoanglinhsama.ecommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Thong ke doanh thu theo tung ngay trong thang
 */
public class Statistical {

    @SerializedName("dayOrder")
    @Expose
    private int dayOrder;
    @SerializedName("income")
    @Expose
    private long income;

    public int getDayOrder() {
        return this.dayOrder;
    }

    public void setDayOrder(int dayOrder) {
        this.dayOrder = dayOrder;
    }

    public long getIncome() {
        return this.income;
    }

    public void setIncome(long income) {
        this.income = income;
    }

}