package com.intens.hr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intens.hr.dto.CandidateDto;
import com.intens.hr.dto.SkillDto;
import com.intens.hr.service.JobCandidateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class JobCandidateControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private JobCandidateService candidateService;

    @InjectMocks
    private JobCandidateController jobCandidateController;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(jobCandidateController).build();
    }

    @Test
    void shouldAddCandidate() throws Exception {
        CandidateDto inputDto = new CandidateDto();
        inputDto.setName("Test");

        CandidateDto outputDto = new CandidateDto();
        outputDto.setId(1);
        outputDto.setName("Test");

        when(candidateService.addCandidate(any(CandidateDto.class))).thenReturn(outputDto);

        mockMvc.perform(post("/api/candidates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test"));

        verify(candidateService, times(1)).addCandidate(any(CandidateDto.class));
    }

    @Test
    void shouldUpdateCandidateWithSkills() throws Exception {
        Integer id = 1;
        CandidateDto inputDto = new CandidateDto();
        inputDto.setName("UpdatedName");

        CandidateDto outputDto = new CandidateDto();
        outputDto.setId(id);
        outputDto.setName("UpdatedName");

        when(candidateService.updateCandidateWithSkills(eq(id), any(CandidateDto.class))).thenReturn(outputDto);

        mockMvc.perform(put("/api/candidates/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedName"));

        verify(candidateService, times(1)).updateCandidateWithSkills(eq(id), any(CandidateDto.class));
    }

    @Test
    void shouldRemoveCandidate() throws Exception {
        Integer id = 1;

        doNothing().when(candidateService).removeCandidate(id);

        mockMvc.perform(delete("/api/candidates/{id}", id))
                .andExpect(status().isNoContent());

        verify(candidateService, times(1)).removeCandidate(id);
    }

    @Test
    void shouldAddSkillToCandidate() throws Exception {
        Integer candidateId = 1;
        SkillDto skillDto = new SkillDto();
        skillDto.setSkillName("Spring");

        CandidateDto outputDto = new CandidateDto();
        outputDto.setId(candidateId);
        outputDto.setSkills(List.of(skillDto));

        when(candidateService.addSkillToCandidate(eq(candidateId), any(SkillDto.class))).thenReturn(outputDto);

        mockMvc.perform(post("/api/candidates/{id}/skills", candidateId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(skillDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.skills[0].skillName").value("Spring"));

        verify(candidateService, times(1)).addSkillToCandidate(eq(candidateId), any(SkillDto.class));
    }

    @Test
    void shouldRemoveSkillFromCandidate() throws Exception {
        Integer candidateId = 1;
        Integer skillId = 2;

        doNothing().when(candidateService).removeSkillFromCandidate(candidateId, skillId);

        mockMvc.perform(delete("/api/candidates/{candidateId}/skills/{skillId}", candidateId, skillId))
                .andExpect(status().isNoContent());

        verify(candidateService, times(1)).removeSkillFromCandidate(candidateId, skillId);
    }

    @Test
    void shouldGetAllCandidates() throws Exception {
        CandidateDto dto = new CandidateDto();
        dto.setId(1);
        dto.setName("Test");

        when(candidateService.getAllCandidates()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/candidates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Test"));

        verify(candidateService, times(1)).getAllCandidates();
    }

    @Test
    void shouldSearchByName() throws Exception {
        String name = "Test";
        CandidateDto dto = new CandidateDto();
        dto.setId(1);
        dto.setName(name);

        when(candidateService.searchByName(name)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/candidates/search-by-name")
                .param("name", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value(name));

        verify(candidateService, times(1)).searchByName(name);
    }

    @Test
    void shouldSearchBySkill() throws Exception {
        String skill = "Java";
        CandidateDto dto = new CandidateDto();
        dto.setId(1);
        
        when(candidateService.searchBySkill(skill)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/candidates/search-by-skill")
                .param("skill", skill))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));

        verify(candidateService, times(1)).searchBySkill(skill);
    }
}
