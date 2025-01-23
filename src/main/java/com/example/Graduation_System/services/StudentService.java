package com.example.Graduation_System.services;

import com.example.Graduation_System.data.Student;

import java.time.LocalDate;
import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();

    Student getStudentById(Long id);

    Student saveStudent(Student student);

    void deleteStudent(Long id);

    List<Student> getGraduatedStudentsInPeriod(LocalDate startDate, LocalDate endDate);

    Student findByFacultyNumber(String facultyNumber);
}
