package com.example.Graduation_System.data;

import com.example.Graduation_System.data.enums.DiplomaStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "Department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private Set<Student> students;

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private Set<Teacher> teachers;

}