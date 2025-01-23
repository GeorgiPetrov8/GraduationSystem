package com.example.Graduation_System.services;

import com.example.Graduation_System.data.Department;

import java.util.List;

public interface DepartmentService {
    Department createDepartment(Department department);

    List<Department> getAllDepartments();

    Department findDepartmentByName(String name);

    void deleteDepartment(Long id);
}
