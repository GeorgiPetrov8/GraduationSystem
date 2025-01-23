package com.example.Graduation_System.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "Defense")
public class Defense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate date;

    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^[0-9]{8}$")
    private String facultyNumber;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student students;

    @NotNull
    private String supervisorName;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher supervisor;

    @NotNull
    private String departmentName;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department committee;


    @DecimalMin(value = "2.0", inclusive = true)
    @DecimalMax(value = "6.0", inclusive = true)
    private Double grades;

}