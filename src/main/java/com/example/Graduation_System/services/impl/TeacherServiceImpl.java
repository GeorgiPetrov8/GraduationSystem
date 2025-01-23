package com.example.Graduation_System.services.impl;

import com.example.Graduation_System.data.Teacher;
import com.example.Graduation_System.data.repo.TeacherRepository;
import com.example.Graduation_System.exceptionHandler.ResourceNotFoundException;
import com.example.Graduation_System.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review with ID " + id + " not found."));
    }

    @Override
    public Teacher saveTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @Override
    public void deleteTeacher(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Review with ID " + id + " does not exist.");
        }
        teacherRepository.deleteById(id);
    }

    @Override
    public Teacher getTeacherByUserUsername(String username) {
        return teacherRepository.findByUserUsername(username);
    }
}
