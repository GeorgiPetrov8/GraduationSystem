package com.example.Graduation_System.services;

import com.example.Graduation_System.data.Teacher;

import java.util.List;

public interface TeacherService {
    List<Teacher> getAllTeachers();

    Teacher getTeacherById(Long id);

    Teacher saveTeacher(Teacher teacher);

    void deleteTeacher(Long id);

    Teacher getTeacherByUserUsername(String username);
}
