package com.example.Graduation_System.data;

import com.example.Graduation_System.data.enums.Conclusion;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "Review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String reviewerName;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher reviewer;

    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^[0-9]{8}$")
    private String facultyNumber;

    @OneToOne
    @JoinColumn(name = "thesis_id", referencedColumnName = "id")
    private DiplomaThesis thesis;

    @Temporal(TemporalType.DATE)
    private Date reviewDate;

    @Lob
    @NotNull
    private String text;

    @Enumerated(EnumType.STRING)
    private Conclusion conclusion;
}