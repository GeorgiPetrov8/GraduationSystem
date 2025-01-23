package com.example.Graduation_System.repoTests;

import com.example.Graduation_System.data.*;
import com.example.Graduation_System.data.enums.DiplomaStatus;
import com.example.Graduation_System.data.enums.TeacherPosition;
import com.example.Graduation_System.data.enums.UserRole;
import com.example.Graduation_System.data.repo.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class DiplomaThesisRepositoryTest {

    @Autowired
    private DiplomaThesisRepository thesisRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DiplomaAssignmentRepository assignmentRepository;

    @Test
    public void testFindThesesByTitleContaining() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setRoles(UserRole.STUDENT);
        userRepository.save(user);

        User user1 = new User();
        user1.setUsername("testuser1");
        user1.setPassword("password123");
        user1.setRoles(UserRole.TEACHER);
        userRepository.save(user1);

        Student student = new Student();
        student.setName("teststudent");
        student.setFacultyNumber("12345678");
        student.setUser(user);
        student.setGraduated((LocalDate.of(2023, 1, 15)));
        studentRepository.save(student);

        Teacher teacher = new Teacher();
        teacher.setName("testteacher");
        teacher.setPosition(TeacherPosition.PROFESSOR);
        teacher.setUser(user1);
        teacherRepository.save(teacher);

        DiplomaAssignment assignment = new DiplomaAssignment();
        assignment.setTitle("test");
        assignment.setGoal("test");
        assignment.setTasks("test");
        assignment.setTechnologies("test");
        assignment.setFacultyNumber(student.getFacultyNumber());
        assignment.setStatus(DiplomaStatus.APPROVED);
        assignment.setStudent(student);
        assignment.setSupervisor(teacher);
        assignment.setSupervisorName(teacher.getName());
        assignmentRepository.save(assignment);

        DiplomaThesis thesis = new DiplomaThesis();
        thesis.setTitle("Advanced Algorithms");
        thesis.setText("test");
        thesis.setAssignment(assignment);
        thesis.setFacultyNumber(student.getFacultyNumber());
        thesisRepository.save(thesis);

        List<DiplomaThesis> results = thesisRepository.findThesesByTitleContaining("Algorithms");
        assertEquals(1, results.size());
        assertEquals("Advanced Algorithms", results.get(0).getTitle());
    }

    @Test
    public void testFindAllByFacultyNumber() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setRoles(UserRole.STUDENT);
        userRepository.save(user);

        User user1 = new User();
        user1.setUsername("testuser1");
        user1.setPassword("password123");
        user1.setRoles(UserRole.TEACHER);
        userRepository.save(user1);

        Student student = new Student();
        student.setName("teststudent");
        student.setFacultyNumber("12345678");
        student.setUser(user);
        student.setGraduated((LocalDate.of(2023, 1, 15)));
        studentRepository.save(student);

        Teacher teacher = new Teacher();
        teacher.setName("testteacher");
        teacher.setPosition(TeacherPosition.PROFESSOR);
        teacher.setUser(user1);
        teacherRepository.save(teacher);

        DiplomaAssignment assignment = new DiplomaAssignment();
        assignment.setTitle("test");
        assignment.setGoal("test");
        assignment.setTasks("test");
        assignment.setTechnologies("test");
        assignment.setFacultyNumber(student.getFacultyNumber());
        assignment.setStatus(DiplomaStatus.APPROVED);
        assignment.setStudent(student);
        assignment.setSupervisor(teacher);
        assignment.setSupervisorName(teacher.getName());
        assignmentRepository.save(assignment);

        DiplomaThesis thesis = new DiplomaThesis();
        thesis.setTitle("Advanced Algorithms");
        thesis.setText("test");
        thesis.setAssignment(assignment);
        thesis.setFacultyNumber(student.getFacultyNumber());
        thesisRepository.save(thesis);

        List<DiplomaAssignment> assignments = assignmentRepository.findAllByFacultyNumber(student.getFacultyNumber());

        assertNotNull(assignments);
        assertEquals(1, assignments.size());
        assertEquals(student.getFacultyNumber(), assignments.get(0).getFacultyNumber());
        assertTrue(assignment.getTitle().startsWith("test"));
        assertNotNull(assignment.getGoal());
        assertNotNull(assignment.getTasks());
        assertNotNull(assignment.getTechnologies());
        assertNotNull(assignment.getSupervisorName());

    }
}

