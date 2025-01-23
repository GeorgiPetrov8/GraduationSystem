package com.example.Graduation_System.data;

import com.example.Graduation_System.data.Student;
import com.example.Graduation_System.data.Teacher;
import com.example.Graduation_System.data.enums.DiplomaStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "DiplomaAssignment")
public class DiplomaAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String goal;

    @NotNull
    private String tasks;

    @NotNull
    private String technologies;

    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^[0-9]{8}$")
    private String facultyNumber;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    @NotNull
    private String supervisorName;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher supervisor;

    @Enumerated(EnumType.STRING)
    private DiplomaStatus status;
}