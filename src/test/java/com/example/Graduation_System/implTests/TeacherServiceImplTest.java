package com.example.Graduation_System.implTests;

import com.example.Graduation_System.data.Department;
import com.example.Graduation_System.data.Teacher;
import com.example.Graduation_System.data.repo.TeacherRepository;
import com.example.Graduation_System.exceptionHandler.ResourceNotFoundException;
import com.example.Graduation_System.services.TeacherService;
import com.example.Graduation_System.services.impl.TeacherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherServiceImplTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    private Teacher teacher;

    private Department department;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        department = new Department();
        department.setName("CS");

        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Dr. Smith");
        teacher.setDepartment(department);
    }

    @Test
    void testGetAllTeachers() {

        List<Teacher> teachers = Arrays.asList(teacher);
        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> result = teacherService.getAllTeachers();

        assertNotNull(result);
        assertEquals(teachers.size(), result.size());
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void testGetTeacherById() {

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        Teacher result = teacherService.getTeacherById(1L);

        assertNotNull(result);
        assertEquals(teacher.getId(), result.getId());
        verify(teacherRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTeacherByIdNotFound() {

        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> teacherService.getTeacherById(1L));
        assertEquals("Review with ID 1 not found.", exception.getMessage());
    }

    @Test
    void testSaveTeacher() {

        when(teacherRepository.save(teacher)).thenReturn(teacher);

        Teacher result = teacherService.saveTeacher(teacher);

        assertNotNull(result);
        assertEquals(teacher.getId(), result.getId());
        verify(teacherRepository, times(1)).save(teacher);
    }

    @Test
    void testDeleteTeacher() {

        when(teacherRepository.existsById(1L)).thenReturn(true);

        teacherService.deleteTeacher(1L);

        verify(teacherRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTeacherNotFound() {

        when(teacherRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> teacherService.deleteTeacher(1L));
        assertEquals("Cannot delete. Review with ID 1 does not exist.", exception.getMessage());
    }
}
