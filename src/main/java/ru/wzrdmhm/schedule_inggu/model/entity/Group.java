package ru.wzrdmhm.schedule_inggu.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Column(nullable = false)
    private String fullName; // "Химико-Биологический"

    // Пока просто заглушки, потом добавим связи:
    // private Institution institution;
    // private Faculty faculty;
    // и т.д.
}
