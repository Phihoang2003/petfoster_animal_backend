package com.hoangphi.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PetStatus {
    HEALTHY("Healthy"),
    SICK("Sick"),
    DECEASED("Deceased");

    private final String state;

    public String getValue() { return state; }
}
