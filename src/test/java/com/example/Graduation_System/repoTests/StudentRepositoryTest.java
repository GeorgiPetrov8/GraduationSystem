package com.example.Graduation_System.repoTests;

import com.example.Graduation_System.data.Student;
import com.example.Graduation_System.data.User;
import com.example.Graduation_System.data.enums.UserRole;
import com.example.Graduation_System.data.repo.StudentRepository;
import com.example.Graduation_System.data.repo.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testFindGraduatedStudentsInPeriod() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setRoles(UserRole.STUDENT);
        userRepository.save(user);

        Student student = new Student();
        student.setName("teststudent");
        student.setFacultyNumber("12345678");
        student.setUser(user);
        student.setGraduated((LocalDate.of(2023, 1, 15)));
        studentRepository.save(student);

        List<Student> results = studentRepository.findGraduatedStudentsInPeriod(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 12, 31)
        );
        Assertions.assertEquals(1, results.size());
        Assertions.assertEquals(LocalDate.of(2023, 1, 15), results.get(0).getGraduated());
    }
}
