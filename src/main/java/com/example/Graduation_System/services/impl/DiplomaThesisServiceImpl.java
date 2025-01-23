package com.example.Graduation_System.services.impl;

import com.example.Graduation_System.data.DiplomaThesis;
import com.example.Graduation_System.data.repo.DiplomaThesisRepository;
import com.example.Graduation_System.exceptionHandler.ResourceNotFoundException;
import com.example.Graduation_System.exceptionHandler.ValidationException;
import com.example.Graduation_System.services.DiplomaThesisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiplomaThesisServiceImpl implements DiplomaThesisService {

    @Autowired
    private DiplomaThesisRepository diplomaThesisRepository;

    @Override
    public List<DiplomaThesis> getAllTheses() {
        return diplomaThesisRepository.findAll();
    }

    @Override
    public DiplomaThesis getThesisById(Long id) {
        return diplomaThesisRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Diploma Thesis with ID " + id + " not found."));
    }

    @Override
    public DiplomaThesis saveThesis(DiplomaThesis thesis) {
        if (thesis.getTitle() == null || thesis.getTitle().isEmpty()) {
            throw new ValidationException("Diploma Thesis title cannot be null or empty.");
        }
        return diplomaThesisRepository.save(thesis);
    }

    @Override
    public void deleteThesis(Long id) {
        if (!diplomaThesisRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Diploma Thesis with ID " + id + " does not exist.");
        }
        diplomaThesisRepository.deleteById(id);
    }

    @Override
    public List<DiplomaThesis> searchByTitle(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            throw new ValidationException("Search keyword cannot be null or empty.");
        }
        return diplomaThesisRepository.findThesesByTitleContaining(keyword);
    }

    @Override
    public  DiplomaThesis findDiplomaThesisByFacultyNumber(String facultyNumber){
        return diplomaThesisRepository.findByFacultyNumber(facultyNumber);
    }
}
