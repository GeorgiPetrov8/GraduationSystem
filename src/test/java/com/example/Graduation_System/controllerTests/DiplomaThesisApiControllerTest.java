package com.example.Graduation_System.controllerTests;

import com.example.Graduation_System.data.DiplomaThesis;
import com.example.Graduation_System.services.DiplomaThesisService;
import com.example.Graduation_System.web.api.DiplomaThesisApiController;
import com.example.Graduation_System.web.api.StudentApiController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class DiplomaThesisApiControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DiplomaThesisService diplomaThesisService;

    @InjectMocks
    private DiplomaThesisApiController diplomaThesisApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(diplomaThesisApiController).build();
    }

    @Test
    void testSearchThesesByTitle() throws Exception {

        DiplomaThesis thesis1 = new DiplomaThesis();
        thesis1.setTitle("Thesis on Java");
        DiplomaThesis thesis2 = new DiplomaThesis();
        thesis2.setTitle("Thesis on Spring");

        List<DiplomaThesis> mockTheses = Arrays.asList(thesis1, thesis2);

        when(diplomaThesisService.searchByTitle("Java")).thenReturn(mockTheses);

        mockMvc.perform(get("/api/thesis/search")
                        .param("keyword", "Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Thesis on Java"))
                .andExpect(jsonPath("$[1].title").value("Thesis on Spring"));
    }
}
