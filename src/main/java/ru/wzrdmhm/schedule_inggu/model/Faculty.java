/*
package ru.wzrdmhm.schedule_inggu.model;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "faculties")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name; // "Химико-биологический факультет"

    @Column(name = "code", nullable = false, length = 20)
    private String code; // "chem_bio", "philology"

    @Column(name = "description", length = 500)
    private String description;

    // 🎯 Связь: многие факультеты → одному учреждению
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id", nullable = false)
    private Institution institution;

    // 🎯 Связь: один факультет → много программ
    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StudyProgram> studyPrograms = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Faculty() {
    }

    public Faculty(String name, String code, String description, Institution institution) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.institution = institution;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public List<StudyProgram> getStudyPrograms() {
        return studyPrograms;
    }

    public void setStudyPrograms(List<StudyProgram> studyPrograms) {
        this.studyPrograms = studyPrograms;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

 */