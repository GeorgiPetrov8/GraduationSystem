package com.example.Graduation_System.implTests;

import com.example.Graduation_System.data.DiplomaThesis;
import com.example.Graduation_System.data.repo.DiplomaThesisRepository;
import com.example.Graduation_System.exceptionHandler.ResourceNotFoundException;
import com.example.Graduation_System.exceptionHandler.ValidationException;
import com.example.Graduation_System.services.impl.DiplomaThesisServiceImpl;
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

class DiplomaThesisServiceImplTest {

    @Mock
    private DiplomaThesisRepository diplomaThesisRepository;

    @InjectMocks
    private DiplomaThesisServiceImpl diplomaThesisService;

    private DiplomaThesis diplomaThesis;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        diplomaThesis = new DiplomaThesis();
        diplomaThesis.setId(1L);
        diplomaThesis.setTitle("Thesis Title");
    }

    @Test
    void testGetAllTheses() {

        List<DiplomaThesis> theses = Arrays.asList(diplomaThesis);
        when(diplomaThesisRepository.findAll()).thenReturn(theses);

        List<DiplomaThesis> result = diplomaThesisService.getAllTheses();

        assertNotNull(result);
        assertEquals(theses.size(), result.size());
        verify(diplomaThesisRepository, times(1)).findAll();
    }

    @Test
    void testGetThesisById() {

        when(diplomaThesisRepository.findById(1L)).thenReturn(Optional.of(diplomaThesis));

        DiplomaThesis result = diplomaThesisService.getThesisById(1L);

        assertNotNull(result);
        assertEquals(diplomaThesis.getId(), result.getId());
        verify(diplomaThesisRepository, times(1)).findById(1L);
    }

    @Test
    void testGetThesisByIdNotFound() {

        when(diplomaThesisRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> diplomaThesisService.getThesisById(1L));
        assertEquals("Diploma Thesis with ID 1 not found.", exception.getMessage());
    }

    @Test
    void testSaveThesis() {

        when(diplomaThesisRepository.save(diplomaThesis)).thenReturn(diplomaThesis);

        DiplomaThesis result = diplomaThesisService.saveThesis(diplomaThesis);

        assertNotNull(result);
        assertEquals(diplomaThesis.getId(), result.getId());
        verify(diplomaThesisRepository, times(1)).save(diplomaThesis);
    }

    @Test
    void testSaveThesisWithEmptyTitle() {

        diplomaThesis.setTitle(""); // Empty title
        ValidationException exception = assertThrows(ValidationException.class, () -> diplomaThesisService.saveThesis(diplomaThesis));

        assertEquals("Diploma Thesis title cannot be null or empty.", exception.getMessage());
    }

    @Test
    void testDeleteThesis() {

        when(diplomaThesisRepository.existsById(1L)).thenReturn(true);

        diplomaThesisService.deleteThesis(1L);

        verify(diplomaThesisRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteThesisNotFound() {

        when(diplomaThesisRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> diplomaThesisService.deleteThesis(1L));
        assertEquals("Cannot delete. Diploma Thesis with ID 1 does not exist.", exception.getMessage());
    }

    @Test
    void testSearchByTitle() {

        List<DiplomaThesis> theses = Arrays.asList(diplomaThesis);
        when(diplomaThesisRepository.findThesesByTitleContaining("Thesis")).thenReturn(theses);

        List<DiplomaThesis> result = diplomaThesisService.searchByTitle("Thesis");

        assertNotNull(result);
        assertEquals(theses.size(), result.size());
        verify(diplomaThesisRepository, times(1)).findThesesByTitleContaining("Thesis");
    }

    @Test
    void testSearchByTitleWithEmptyKeyword() {

        ValidationException exception = assertThrows(ValidationException.class, () -> diplomaThesisService.searchByTitle(""));
        assertEquals("Search keyword cannot be null or empty.", exception.getMessage());
    }
}
