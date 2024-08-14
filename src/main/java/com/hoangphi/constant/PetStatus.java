package com.hoangphi.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PetStatus {
    AVAILABLE("Available"),
    FOSTERED("Fostered"),
    ADOPTED("Adopted");

    private final String state;

    public String getValue() { return state; }
}
