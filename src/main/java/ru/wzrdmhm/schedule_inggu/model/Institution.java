/*package ru.wzrdmhm.schedule_inggu.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "institutions")
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name; // "Ингушский государственный университет"

    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code; // "inggu", "college"

    @Column(name = "description", length = 500)
    private String description; // Описание для пользователя

    // 🎯 Связь: одно учреждение → много факультетов
    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Faculty> faculties = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /*
    @OneToMany(mappedBy = "institution") - связь "один ко многим", где mappedBy указывает на поле в Faculty

cascade = CascadeType.ALL - при удалении учреждения удалятся все его факультеты

fetch = FetchType.LAZY - факультеты загружаются только когда к ним обращаются (оптимизация)

addFaculty() - хелпер-метод для установки двусторонней связи


    // 🔄 Конструкторы
    public Institution() {}

    public Institution(String name, String code, String description) {
        this.name = name;
        this.code = code;
        this.description = description;
    }

    // 🎯 Метод для удобного добавления факультета
    public void addFaculty(Faculty faculty) {
        faculties.add(faculty);
        faculty.setInstitution(this); // Устанавливаем обратную связь
    }

    // 📋 Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Faculty> getFaculties() { return faculties; }
    public void setFaculties(List<Faculty> faculties) { this.faculties = faculties; }

    public LocalDateTime getCreatedAt() { return createdAt; }

}


*/