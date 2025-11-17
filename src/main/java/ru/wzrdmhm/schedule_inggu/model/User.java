package ru.wzrdmhm.schedule_inggu.model;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    private Long telegramId;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(length = 100)
    private String username;

    @Column(name = "group_name", length = 50)
    private String groupName;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 30)
    private UserState state = UserState.START; // Для multi-step выбора

    private String tempInstitution;
    private String tempFaculty;
    private String tempEducationLevel;
    private Long tempStudyProgramId;
    private Integer tempCourse;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public User() {}

    public User(Long telegramId, String firstName) {
        if (telegramId == null) throw new IllegalArgumentException("telegramId cannot be null");
        this.telegramId = telegramId;
        this.firstName = firstName != null ? firstName : "User";
        this.state = UserState.START;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public String getTempFaculty() {
        return tempFaculty;
    }

    public void setTempFaculty(String tempFaculty) {
        this.tempFaculty = tempFaculty;
    }

    public String getTempInstitution() {
        return tempInstitution;
    }

    public void setTempInstitution(String tempInstitution) {
        this.tempInstitution = tempInstitution;
    }

    public Integer getTempCourse() {
        return tempCourse;
    }

    public void setTempCourse(Integer tempCourse) {
        this.tempCourse = tempCourse;
    }

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
