package ru.wzrdmhm.schedule_inggu.model;

public class User {
    private Long telegramId;
    private String firstName;
    private String groupName;

    public User(Long telegramId, String firstName, String groupName) {
        this.telegramId = telegramId;
        this.firstName = firstName;
        this.groupName = groupName;
    }

    public User(Long telegramId) {
        this.telegramId = telegramId;
    }

    public User(String firstName) {
        this.firstName = firstName;
    }

    public User() {

    }

    public String getFirstName() {
        return firstName;
    }

    public long getTelegramId() {
        return telegramId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setTelegramId(Long telegramId) {
        this.telegramId = telegramId;
    }
}
