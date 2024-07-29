package com.hoangphi.constant;

public enum RespMessage {
    SUCCESS("Successfully"),
    FAILURE("Failures!"),
    INVALID("is invalid!"),
    NOT_FOUND("Can't found data!"),

    INTERNAL_SERVER_ERROR("Internal Server Error!");

    private final String message;
    RespMessage(String message) { this.message = message; }

    public String getValue() { return message; }

    public static final String NOT_EMPTY = "can't be empty!";

    public static final String EXISTS = "already exists!";

}
