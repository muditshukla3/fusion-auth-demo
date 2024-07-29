package com.ms.service_app.entity;

public class PanicResponse {
    public PanicResponse(String message) {
        this.message = message;
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
