package com.explore.common;

/**
 * @author PinTeh
 * @date 2020/4/14
 */
public enum ResponseCode {

    /**
     * Success
     */
    SUCCESS(0, "SUCCESS"),
    /**
     * Error
     */
    ERROR(1, "ERROR");

    private int code;
    private String description;

    ResponseCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }
}
