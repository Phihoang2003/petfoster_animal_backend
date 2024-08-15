package com.hoangphi.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AdoptStatus {

    WAITING("Waiting"),
    REGISTERED("Registered"),
    ADOPTED("Adopted"),
    CANCELLED_BY_CUSTOMER("Cancelled By Customer"),
    CANCELLED_BY_ADMIN("Cancelled By Admin");

    private final String state;

    public String getValue() {
        return state;
    }

}