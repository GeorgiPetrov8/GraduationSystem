package com.example.Graduation_System.repoTests;

import com.example.Graduation_System.GraduationSystemApplication;
import com.example.Graduation_System.data.*;
import com.example.Graduation_System.data.enums.DiplomaStatus;
import com.example.Graduation_System.data.enums.TeacherPosition;
import com.example.Graduation_System.data.enums.UserRole;
import com.example.Graduation_System.data.repo.DefenseRepository;
import com.example.Graduation_System.data.repo.StudentRepository;
import com.example.Graduation_System.data.repo.TeacherRepository;
import com.example.Graduation_System.data.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class DefenseRepositoryTest{

    @Autowired
    private DefenseRepository defenseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void testFindDefensesWithGradeRange() {
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

        User user2 = new User();
        user2.setUsername("testuser2");
        user2.setPassword("password123");
        user2.setRoles(UserRole.STUDENT);
        userRepository.save(user2);

        Student student = new Student();
        student.setName("teststudent");
        student.setFacultyNumber("12345678");
        student.setUser(user);
        student.setGraduated((LocalDate.of(2023, 1, 15)));
        studentRepository.save(student);

        Student student1 = new Student();
        student1.setName("teststudent");
        student1.setFacultyNumber("12345679");
        student1.setUser(user2);
        student1.setGraduated((LocalDate.of(2023, 1, 15)));
        studentRepository.save(student1);

        Teacher teacher = new Teacher();
        teacher.setName("testteacher");
        teacher.setPosition(TeacherPosition.PROFESSOR);
        teacher.setUser(user1);
        teacherRepository.save(teacher);

        Department department = new Department();
        department.setName("testdepartment");

        Defense defense1 = new Defense();
        defense1.setGrades(3.5);
        defense1.setDate(LocalDate.now());
        defense1.setStudents(student);
        defense1.setSupervisor(teacher);
        defense1.setFacultyNumber(student.getFacultyNumber());
        defense1.setSupervisorName(teacher.getName());
        defense1.setDepartmentName(department.getName());
        defenseRepository.save(defense1);

        Defense defense2 = new Defense();
        defense2.setGrades(4.5);
        defense2.setDate(LocalDate.now());
        defense2.setStudents(student1);
        defense2.setSupervisor(teacher);
        defense2.setFacultyNumber(student1.getFacultyNumber());
        defense2.setSupervisorName(teacher.getName());
        defense2.setDepartmentName(department.getName());
        defenseRepository.save(defense2);

        List<Defense> results = defenseRepository.findDefensesWithGradeRange(3.0, 4.0);

        assertEquals(1, results.size());
        assertEquals(3.5, results.get(0).getGrades(), 0.01);
        }

    @Test
    public void testCountSuccessfulDefensesByTeacher() {

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

        User user2 = new User();
        user2.setUsername("testuser2");
        user2.setPassword("password123");
        user2.setRoles(UserRole.STUDENT);
        userRepository.save(user2);

        Student student = new Student();
        student.setName("teststudent");
        student.setFacultyNumber("12345678");
        student.setUser(user);
        student.setGraduated((LocalDate.of(2023, 1, 15)));
        studentRepository.save(student);

        Student student1 = new Student();
        student1.setName("teststudent");
        student1.setFacultyNumber("12345679");
        student1.setUser(user2);
        student1.setGraduated((LocalDate.of(2023, 1, 15)));
        studentRepository.save(student1);

        Teacher teacher = new Teacher();
        teacher.setName("testteacher");
        teacher.setPosition(TeacherPosition.PROFESSOR);
        teacher.setUser(user1);
        teacherRepository.save(teacher);

        Department department = new Department();
        department.setName("testdepartment");

        Defense defense1 = new Defense();
        defense1.setGrades(4.0);
        defense1.setDate(LocalDate.now());
        defense1.setStudents(student);
        defense1.setSupervisor(teacher);
        defense1.setFacultyNumber(student.getFacultyNumber());
        defense1.setSupervisorName(student.getName());
        defense1.setDepartmentName(department.getName());
        defenseRepository.save(defense1);

        Defense defense2 = new Defense();
        defense2.setGrades(2.5);
        defense2.setDate(LocalDate.now());
        defense2.setStudents(student);
        defense2.setSupervisor(teacher);
        defense2.setFacultyNumber(student1.getFacultyNumber());
        defense2.setSupervisorName(student1.getName());
        defense2.setDepartmentName(department.getName());
        defenseRepository.save(defense2);

        Long count = defenseRepository.countSuccessfulDefensesByTeacher(teacher.getId());

        assertEquals(1, count);
    }

    @Test
    public void testFindAverageStudentsPerDefenseInPeriod() {

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

        User user2 = new User();
        user2.setUsername("testuser2");
        user2.setPassword("password123");
        user2.setRoles(UserRole.STUDENT);
        userRepository.save(user2);

        Student student = new Student();
        student.setName("teststudent");
        student.setFacultyNumber("12345678");
        student.setUser(user);
        student.setGraduated((LocalDate.of(2023, 1, 15)));
        studentRepository.save(student);

        Student student1 = new Student();
        student1.setName("teststudent");
        student1.setFacultyNumber("12345679");
        student1.setUser(user2);
        student1.setGraduated((LocalDate.of(2023, 1, 15)));
        studentRepository.save(student1);

        Teacher teacher = new Teacher();
        teacher.setName("testteacher");
        teacher.setPosition(TeacherPosition.PROFESSOR);
        teacher.setUser(user1);
        teacherRepository.save(teacher);

        Department department = new Department();
        department.setName("testdepartment");

        Defense defense1 = new Defense();
        defense1.setDate(LocalDate.of(2025, 1, 1));
        defense1.setFacultyNumber("12345678");
        defense1.setSupervisor(teacher);
        defense1.setStudents(student);
        defense1.setSupervisorName(teacher.getName());
        defense1.setDepartmentName(department.getName());
        defenseRepository.save(defense1);

        Defense defense2 = new Defense();
        defense2.setDate(LocalDate.of(2025, 1, 2));
        defense2.setFacultyNumber("12345679");
        defense2.setStudents(student1);
        defense2.setSupervisor(teacher);
        defense2.setSupervisorName(teacher.getName());
        defense2.setDepartmentName(department.getName());
        defenseRepository.save(defense2);

        Defense defense3 = new Defense();
        defense3.setDate(LocalDate.of(2025, 1, 1));
        defense3.setFacultyNumber("12345674");
        defense3.setStudents(student);
        defense3.setSupervisor(teacher);
        defense3.setSupervisorName(teacher.getName());
        defense3.setDepartmentName(department.getName());
        defenseRepository.save(defense3);

        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 31);

        Double average = defenseRepository.findAverageStudentsPerDefenseInPeriod(startDate, endDate);

        assertNotNull(average);
        assertEquals(1.5, average, 1.0);
    }

    @Test
    public void testFindDefensesByFacultyNumber() {
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

        User user2 = new User();
        user2.setUsername("testuser2");
        user2.setPassword("password123");
        user2.setRoles(UserRole.STUDENT);
        userRepository.save(user2);

        Student student = new Student();
        student.setName("teststudent");
        student.setFacultyNumber("12345678");
        student.setUser(user);
        student.setGraduated((LocalDate.of(2023, 1, 15)));
        studentRepository.save(student);

        Student student1 = new Student();
        student1.setName("teststudent");
        student1.setFacultyNumber("12345679");
        student1.setUser(user2);
        student1.setGraduated((LocalDate.of(2023, 1, 15)));
        studentRepository.save(student1);

        Teacher teacher = new Teacher();
        teacher.setName("testteacher");
        teacher.setPosition(TeacherPosition.PROFESSOR);
        teacher.setUser(user1);
        teacherRepository.save(teacher);

        Department department = new Department();
        department.setName("testdepartment");

        Defense defense1 = new Defense();
        defense1.setDate(LocalDate.of(2025, 1, 1));
        defense1.setFacultyNumber("12345678");
        defense1.setSupervisor(teacher);
        defense1.setStudents(student);
        defense1.setSupervisorName(teacher.getName());
        defense1.setDepartmentName(department.getName());
        defenseRepository.save(defense1);

        Defense defense2 = new Defense();
        defense2.setDate(LocalDate.of(2025, 1, 2));
        defense2.setFacultyNumber("12345675");
        defense2.setStudents(student1);
        defense2.setSupervisor(teacher);
        defense2.setSupervisorName(teacher.getName());
        defense2.setDepartmentName(department.getName());
        defenseRepository.save(defense2);

        Defense defense3 = new Defense();
        defense3.setDate(LocalDate.of(2025, 1, 1));
        defense3.setFacultyNumber("12345679");
        defense3.setStudents(student);
        defense3.setSupervisor(teacher);
        defense3.setSupervisorName(teacher.getName());
        defense3.setDepartmentName(department.getName());
        defenseRepository.save(defense3);

        String facultyNumber = "12345678";
        List<Defense> defenses = defenseRepository.findDefensesByFacultyNumber(facultyNumber);

        assertNotNull(defenses);
        assertEquals(1, defenses.size());
        assertTrue(defenses.stream().allMatch(d -> d.getFacultyNumber().equals(facultyNumber)));
    }

}
