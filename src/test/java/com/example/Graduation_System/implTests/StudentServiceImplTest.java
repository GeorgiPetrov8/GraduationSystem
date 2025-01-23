package com.example.Graduation_System.implTests;

import com.example.Graduation_System.data.Student;
import com.example.Graduation_System.data.repo.StudentRepository;
import com.example.Graduation_System.exceptionHandler.ResourceNotFoundException;
import com.example.Graduation_System.services.StudentService;
import com.example.Graduation_System.services.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        student = new Student();
        student.setId(1L);
        student.setName("John Doe");
        student.setGraduated(LocalDate.of(2023, 6, 15));
    }

    @Test
    void testGetAllStudents() {

        List<Student> students = Arrays.asList(student);
        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.getAllStudents();

        assertNotNull(result);
        assertEquals(students.size(), result.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetStudentById() {

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1L);

        assertNotNull(result);
        assertEquals(student.getId(), result.getId());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetStudentByIdNotFound() {

        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById(1L));
        assertEquals("Review with ID 1 not found.", exception.getMessage());
    }

    @Test
    void testSaveStudent() {

        when(studentRepository.save(student)).thenReturn(student);

        Student result = studentService.saveStudent(student);

        assertNotNull(result);
        assertEquals(student.getId(), result.getId());
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testDeleteStudent() {

        when(studentRepository.existsById(1L)).thenReturn(true);

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteStudentNotFound() {

        when(studentRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> studentService.deleteStudent(1L));
        assertEquals("Cannot delete. Review with ID 1 does not exist.", exception.getMessage());
    }

    @Test
    void testGetGraduatedStudentsInPeriod() {

        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        List<Student> students = Arrays.asList(student);
        when(studentRepository.findGraduatedStudentsInPeriod(startDate, endDate)).thenReturn(students);

        List<Student> result = studentService.getGraduatedStudentsInPeriod(startDate, endDate);

        assertNotNull(result);
        assertEquals(students.size(), result.size());
        verify(studentRepository, times(1)).findGraduatedStudentsInPeriod(startDate, endDate);
    }
}
