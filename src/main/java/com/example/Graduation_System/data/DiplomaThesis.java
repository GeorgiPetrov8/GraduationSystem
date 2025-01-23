package com.example.Graduation_System.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "DiplomaThesis")
public class DiplomaThesis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @Lob
    @NotNull
    private String text;

    @Temporal(TemporalType.DATE)
    private Date submissionDate;

    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^[0-9]{8}$")
    private String facultyNumber;

    @OneToOne
    @JoinColumn(name = "assignment_id", referencedColumnName = "id")
    private DiplomaAssignment assignment;
}
