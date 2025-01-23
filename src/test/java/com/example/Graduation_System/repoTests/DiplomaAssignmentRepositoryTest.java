package com.example.Graduation_System.repoTests;

import com.example.Graduation_System.data.DiplomaAssignment;
import com.example.Graduation_System.data.Student;
import com.example.Graduation_System.data.Teacher;
import com.example.Graduation_System.data.User;
import com.example.Graduation_System.data.enums.DiplomaStatus;
import com.example.Graduation_System.data.enums.TeacherPosition;
import com.example.Graduation_System.data.enums.UserRole;
import com.example.Graduation_System.data.repo.DiplomaAssignmentRepository;
import com.example.Graduation_System.data.repo.StudentRepository;
import com.example.Graduation_System.data.repo.TeacherRepository;
import com.example.Graduation_System.data.repo.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
public class DiplomaAssignmentRepositoryTest {

    @Autowired
    private DiplomaAssignmentRepository assignmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    public void testFindByStatus() {
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

        List<DiplomaAssignment> results = assignmentRepository.findByStatus(DiplomaStatus.APPROVED);
        assertEquals(1, results.size());
        assertEquals(DiplomaStatus.APPROVED, results.getFirst().getStatus());
    }

    @Test
    public void testCountNegativeReviews() {
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
        assignment.setStatus(DiplomaStatus.REJECTED);
        assignment.setStudent(student);
        assignment.setSupervisor(teacher);
        assignment.setSupervisorName(teacher.getName());
        assignmentRepository.save(assignment);

        Long count = assignmentRepository.countNegativeReviews();
        assertEquals(1, count);
    }
}
