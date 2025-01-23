package com.example.Graduation_System.services;

import com.example.Graduation_System.data.DiplomaThesis;

import java.util.List;

public interface DiplomaThesisService {
    List<DiplomaThesis> getAllTheses();

    DiplomaThesis getThesisById(Long id);

    DiplomaThesis saveThesis(DiplomaThesis thesis);

    void deleteThesis(Long id);

    List<DiplomaThesis> searchByTitle(String keyword);

    DiplomaThesis findDiplomaThesisByFacultyNumber(String facultyNumber);
}
