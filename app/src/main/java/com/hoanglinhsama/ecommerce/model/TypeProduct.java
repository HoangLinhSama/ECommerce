package com.hoanglinhsama.ecommerce.model;

public class TypeProduct {
    private String nameTypeProduct;
    private int pictureTypeProduct;

    public TypeProduct(String nameTypeProduct, int pictureTypeProduct) {
        this.nameTypeProduct = nameTypeProduct;
        this.pictureTypeProduct = pictureTypeProduct;
    }

    public String getNameTypeProduct() {
        return nameTypeProduct;
    }

    public void setNameTypeProduct(String nameTypeProduct) {
        this.nameTypeProduct = nameTypeProduct;
    }

    public int getPictureTypeProduct() {
        return pictureTypeProduct;
    }

    public void setPictureTypeProduct(int pictureTypeProduct) {
        this.pictureTypeProduct = pictureTypeProduct;
    }
}
