package ru.wzrdmhm.schedule_inggu.model;

public class ScheduleResponse {
    private String response;
    private boolean success;

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
