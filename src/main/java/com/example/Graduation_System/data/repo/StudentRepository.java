package com.example.Graduation_System.data.repo;

import com.example.Graduation_System.data.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s WHERE s.graduated BETWEEN :startDate AND :endDate")
    List<Student> findGraduatedStudentsInPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    Student findByFacultyNumber(String facultyNumber);
}
