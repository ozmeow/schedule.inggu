package ru.wzrdmhm.schedule_inggu.dto;

public class BotResponse {
    private String response;
    private boolean success;

    public BotResponse() {
    }

    public BotResponse(String response, boolean success) {
        this.response = response;
        this.success = success;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
