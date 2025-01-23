package com.example.Graduation_System.implTests;

import com.example.Graduation_System.data.DiplomaAssignment;
import com.example.Graduation_System.data.enums.DiplomaStatus;
import com.example.Graduation_System.data.repo.DiplomaAssignmentRepository;
import com.example.Graduation_System.services.impl.DiplomaAssignmentServiceImpl;
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

class DiplomaAssignmentServiceImplTest {

    @Mock
    private DiplomaAssignmentRepository diplomaAssignmentRepository;

    @InjectMocks
    private DiplomaAssignmentServiceImpl diplomaAssignmentService;

    private DiplomaAssignment diplomaAssignment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        diplomaAssignment = new DiplomaAssignment();
        diplomaAssignment.setId(1L);
        diplomaAssignment.setStatus(DiplomaStatus.PENDING);
    }

    @Test
    void testGetAllAssignments() {

        List<DiplomaAssignment> assignments = Arrays.asList(diplomaAssignment);
        when(diplomaAssignmentRepository.findAll()).thenReturn(assignments);

        List<DiplomaAssignment> result = diplomaAssignmentService.getAllAssignments();

        assertNotNull(result);
        assertEquals(assignments.size(), result.size());
        verify(diplomaAssignmentRepository, times(1)).findAll();
    }

    @Test
    void testGetAssignmentById() {

        when(diplomaAssignmentRepository.findById(1L)).thenReturn(Optional.of(diplomaAssignment));

        DiplomaAssignment result = diplomaAssignmentService.getAssignmentById(1L);

        assertNotNull(result);
        assertEquals(diplomaAssignment.getId(), result.getId());
        verify(diplomaAssignmentRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveAssignment() {

        when(diplomaAssignmentRepository.save(diplomaAssignment)).thenReturn(diplomaAssignment);

        DiplomaAssignment result = diplomaAssignmentService.saveAssignment(diplomaAssignment);

        assertNotNull(result);
        assertEquals(diplomaAssignment.getId(), result.getId());
        verify(diplomaAssignmentRepository, times(1)).save(diplomaAssignment);
    }

    @Test
    void testDeleteAssignment() {

        diplomaAssignmentService.deleteAssignment(1L);

        verify(diplomaAssignmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testApproveAssignment() {

        when(diplomaAssignmentRepository.findById(1L)).thenReturn(Optional.of(diplomaAssignment));
        when(diplomaAssignmentRepository.save(diplomaAssignment)).thenReturn(diplomaAssignment);

        diplomaAssignmentService.approveAssignment(1L);

        assertEquals(DiplomaStatus.APPROVED, diplomaAssignment.getStatus());
        verify(diplomaAssignmentRepository, times(1)).save(diplomaAssignment);
    }

    @Test
    void testDisapproveAssignment() {

        when(diplomaAssignmentRepository.findById(1L)).thenReturn(Optional.of(diplomaAssignment));
        when(diplomaAssignmentRepository.save(diplomaAssignment)).thenReturn(diplomaAssignment);

        diplomaAssignmentService.disapproveAssignment(1L);

        assertEquals(DiplomaStatus.REJECTED, diplomaAssignment.getStatus());
        verify(diplomaAssignmentRepository, times(1)).save(diplomaAssignment);
    }

    @Test
    void testGetApprovedAssignmentsBySupervisor() {

        List<DiplomaAssignment> approvedAssignments = Arrays.asList(diplomaAssignment);
        when(diplomaAssignmentRepository.findApprovedAssignmentsBySupervisor(1L)).thenReturn(approvedAssignments);

        List<DiplomaAssignment> result = diplomaAssignmentService.getApprovedAssignmentsBySupervisor(1L);

        assertNotNull(result);
        assertEquals(approvedAssignments.size(), result.size());
        verify(diplomaAssignmentRepository, times(1)).findApprovedAssignmentsBySupervisor(1L);
    }

    @Test
    void testCountNegativeReviews() {

        when(diplomaAssignmentRepository.countNegativeReviews()).thenReturn(5L);

        Long result = diplomaAssignmentService.countNegativeReviews();

        assertEquals(5L, result);
        verify(diplomaAssignmentRepository, times(1)).countNegativeReviews();
    }

    @Test
    void testGetAssignmentByIdNotFound() {
        when(diplomaAssignmentRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> diplomaAssignmentService.getAssignmentById(1L));
        assertEquals("Diploma Assignment not found", exception.getMessage());
    }
}
