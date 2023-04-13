package com.hoanglinhsama.ecommerce.model;

public class AdminFeature {
    private String name;
    private int picture;

    public AdminFeature(String name, int picture) {
        this.name = name;
        this.picture = picture;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPicture() {
        return this.picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }
}
