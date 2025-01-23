package com.example.Graduation_System.data.repo;

import com.example.Graduation_System.data.Defense;
import com.example.Graduation_System.data.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DefenseRepository extends JpaRepository<Defense, Long> {

    @Query("SELECT da FROM Defense da WHERE da.facultyNumber = :facultyNumber")
    List<Defense> findDefensesByFacultyNumber(@Param("facultyNumber") String facultyNumber);

    @Query("SELECT d FROM Defense d WHERE d.grades BETWEEN :minGrade AND :maxGrade")
    List<Defense> findDefensesWithGradeRange(@Param("minGrade") Double minGrade, @Param("maxGrade") Double maxGrade);

    @Query("SELECT COUNT(d) * 1.0 / COUNT(DISTINCT d.id) FROM Defense d WHERE d.date BETWEEN :startDate AND :endDate")
    Double findAverageStudentsPerDefenseInPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(d) FROM Defense d WHERE d.grades >= 3.0 AND d.supervisor.id = :teacherId")
    Long countSuccessfulDefensesByTeacher(@Param("teacherId") Long teacherId);

}
