package com.hoanglinhsama.ecommerce.eventbus;

import com.hoanglinhsama.ecommerce.model.Order;

/**
 * Gui du lieu cua don hang tu OrderHistoryAdapter qua OrderManageActivity de xu ly
 */
public class UpdateStatusOrderEvent {
    Order order;

    public UpdateStatusOrderEvent(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
