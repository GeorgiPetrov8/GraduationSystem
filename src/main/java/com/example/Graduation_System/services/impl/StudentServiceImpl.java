package com.example.Graduation_System.services.impl;

import com.example.Graduation_System.data.Student;
import com.example.Graduation_System.data.repo.StudentRepository;
import com.example.Graduation_System.exceptionHandler.ResourceNotFoundException;
import com.example.Graduation_System.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review with ID " + id + " not found."));
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Review with ID " + id + " does not exist.");
        }
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> getGraduatedStudentsInPeriod(LocalDate startDate, LocalDate endDate) {
        return studentRepository.findGraduatedStudentsInPeriod(startDate, endDate);
    }

    @Override
    public Student findByFacultyNumber(String facultyNumber) {
        return studentRepository.findByFacultyNumber(facultyNumber);
    }
}
