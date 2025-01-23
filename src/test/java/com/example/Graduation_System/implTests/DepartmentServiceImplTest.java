package com.example.Graduation_System.implTests;

import com.example.Graduation_System.data.Department;
import com.example.Graduation_System.data.repo.DepartmentRepository;
import com.example.Graduation_System.services.DepartmentService;
import com.example.Graduation_System.services.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateDepartment() {

        Department department = new Department();
        department.setName("Computer Science");
        when(departmentRepository.save(department)).thenReturn(department);

        Department result = departmentService.createDepartment(department);

        assertNotNull(result);
        assertEquals("Computer Science", result.getName());
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    void testGetAllDepartments() {

        List<Department> departments = Arrays.asList(new Department(), new Department());
        when(departmentRepository.findAll()).thenReturn(departments);

        List<Department> result = departmentService.getAllDepartments();

        assertEquals(departments.size(), result.size());
        verify(departmentRepository, times(1)).findAll();
    }
}
