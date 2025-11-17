package ru.wzrdmhm.schedule_inggu.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "education_levels")
public class EducationLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nameLevel", nullable = false, unique = true, length = 100)
    private String nameLevel; // "Бакалавриат", "Магистратура"

    @Column(name = "specialization", nullable = false, unique = true, length = 50)
    private String specialization; // "бакалавр", "магистр" "специалитет" - для внутреннего использования

    @Column(name = "courceYears", nullable = false)
    private Integer courceYears; // 4 для бакалавриата, 2 для магистратуры

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // 🔄 Конструкторы
    public EducationLevel() {
    }

    public EducationLevel(String nameLevel, String specialization, Integer courceYears) {
        this.nameLevel = nameLevel;
        this.specialization = specialization;
        this.courceYears = courceYears;
    }

    // 📋 Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameLevel() {
        return nameLevel;
    }

    public void setNameLevel(String nameLevel) {
        this.nameLevel = nameLevel;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Integer getCourceYears() {
        return courceYears;
    }

    public void setCourceYears(Integer courceYears) {
        this.courceYears = courceYears;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

