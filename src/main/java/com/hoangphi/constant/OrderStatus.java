package com.hoangphi.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderStatus {
    PLACED("Placed"),
    SHIPPING("Shipping"),
    DELIVERED("Delivered"),
    CANCELLED_BY_CUSTOMER("Cancelled By Customer"),
    CANCELLED_BY_ADMIN("Cancelled By Admin"),
    WAITING("Waiting");

    private final String state;

    public String getValue() {
        return state;
    }
}
