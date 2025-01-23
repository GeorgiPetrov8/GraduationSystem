package com.example.Graduation_System.data.repo;

import com.example.Graduation_System.data.DiplomaThesis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiplomaThesisRepository extends JpaRepository<DiplomaThesis, Long> {

    @Query("SELECT dt FROM DiplomaThesis dt WHERE dt.title LIKE %:keyword%")
    List<DiplomaThesis> findThesesByTitleContaining(@Param("keyword") String keyword);

    DiplomaThesis findByFacultyNumber(String facultyNumber);
}
