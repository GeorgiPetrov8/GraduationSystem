package com.example.Graduation_System.web.api;

import com.example.Graduation_System.data.DiplomaAssignment;
import com.example.Graduation_System.data.DiplomaThesis;
import com.example.Graduation_System.data.Student;
import com.example.Graduation_System.services.DiplomaThesisService;
import com.example.Graduation_System.services.StudentService;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/thesis")
public class DiplomaThesisApiController {
    @Autowired
    private DiplomaThesisService diplomaThesisService;

    @GetMapping("/search")
    public List<DiplomaThesis> searchThesesByTitle(@RequestParam String keyword) {
        return diplomaThesisService.searchByTitle(keyword);
    }

}
