package com.hoanglinhsama.ecommerce.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cart {

    @SerializedName("idProduct")
    @Expose
    private int idProduct;
    @SerializedName("nameProduct")
    @Expose
    private String nameProduct;
    @SerializedName("totalPrice")
    @Expose
    private long totalPrice;
    @SerializedName("pictureProduct")
    @Expose
    private String pictureProduct;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("quantityRemain")
    @Expose
    private int quantityRemain;

    public int getQuantityRemain() {
        return this.quantityRemain;
    }

    public void setQuantityRemain(int quantityRemain) {
        this.quantityRemain = quantityRemain;
    }

    public int getIdProduct() {
        return this.idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getNameProduct() {
        return this.nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public long getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPictureProduct() {
        return this.pictureProduct;
    }

    public void setPictureProduct(String pictureProduct) {
        this.pictureProduct = pictureProduct;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}