package ru.wzrdmhm.schedule_inggu.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_code")
    private Group group;     // Для какой группы

    @Column(name = "subject", nullable = false, length = 100)
    private String subject;       // Название предмета

    @Column(name = "time_range", nullable = false, length = 50)
    private String time;          // Время пары

    @Column(name = "teacher", length = 100)
    private String teacher;

    @Column(name = "classroom", length = 20)
    private String classroom;

    @Column(name = "day_of_week")
    private Integer dayOfWeek;

    @Column(name = "week_type", length = 10)
    private String weekType;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public String getWeekType() {
        return weekType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setWeekType(String weekType) {
        this.weekType = weekType;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Group getGroupId() {
        return group;
    }

    public void setGroupId(Group groupName) {
        this.group = groupName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", subject, time, classroom);
    }
}
