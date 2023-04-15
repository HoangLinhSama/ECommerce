package com.hoanglinhsama.ecommerce.eventbus;

import com.hoanglinhsama.ecommerce.model.Product;

/**
 * Gui su kien xoa, sua san pham tu ManagerProductAdapter den ProductManageActivity
 */
public class DeleteModifyProductEvent {
    Product product; // Truyen theo thong tin cua san pham duoc longclick de phuc vu cho viec sua, xoa

    public DeleteModifyProductEvent(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
