package com.example.Graduation_System.data.repo;

import com.example.Graduation_System.data.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Teacher findByUserUsername(String username);
}
