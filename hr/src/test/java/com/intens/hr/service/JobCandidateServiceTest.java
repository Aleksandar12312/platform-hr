package com.intens.hr.service;

import com.intens.hr.dto.CandidateDto;
import com.intens.hr.dto.SkillDto;
import com.intens.hr.model.JobCandidate;
import com.intens.hr.model.Skill;
import com.intens.hr.repository.JobCandidateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JobCandidateServiceTest {

    @InjectMocks
    private JobCandidateService candidateService;

    @Mock
    private JobCandidateRepository jobCandidateRepository;

    @Test
    void shouldAddCandidateWithSkills() {

        CandidateDto dto = new CandidateDto();
        dto.setName("TestName");
        dto.setEmail("test@gmail.com");
        dto.setContactNumber("123456");

        SkillDto skill = new SkillDto();
        skill.setSkillName("Java");

        dto.setSkills(List.of(skill));

        JobCandidate savedEntity = new JobCandidate();
        savedEntity.setId(1);
        savedEntity.setName("TestName");

        Skill savedSkill = new Skill();
        savedSkill.setId(1);
        savedSkill.setSkillName("Java");
        savedSkill.setJobCandidate(savedEntity);
        savedEntity.getSkills().add(savedSkill);

        when(jobCandidateRepository.save(any(JobCandidate.class))).thenAnswer(i -> {
            JobCandidate arg = (JobCandidate) i.getArguments()[0];
            arg.setId(1);
            return arg;
        });

        CandidateDto result = candidateService.addCandidate(dto);

        assertNotNull(result.getId());
        assertEquals("TestName", result.getName());
        assertEquals(1, result.getSkills().size());
        assertEquals("Java", result.getSkills().get(0).getSkillName());
        verify(jobCandidateRepository, times(1)).save(any(JobCandidate.class));
    }

    @Test
    void shouldUpdateCandidateWithSkills() {

        Integer id = 1;
        CandidateDto dto = new CandidateDto();
        dto.setName("UpdatedName");

        SkillDto skill = new SkillDto();
        skill.setSkillName("Spring");
        dto.setSkills(List.of(skill));

        JobCandidate existingCandidate = new JobCandidate();
        existingCandidate.setId(id);
        existingCandidate.setName("OldName");

        when(jobCandidateRepository.findById(id)).thenReturn(Optional.of(existingCandidate));
        when(jobCandidateRepository.save(any(JobCandidate.class))).thenAnswer(i -> i.getArguments()[0]);

        CandidateDto result = candidateService.updateCandidateWithSkills(id, dto);

        assertEquals("UpdatedName", result.getName());
        assertEquals(1, result.getSkills().size());
        assertEquals("Spring", result.getSkills().get(0).getSkillName());
        verify(jobCandidateRepository, times(1)).findById(id);
        verify(jobCandidateRepository, times(1)).save(existingCandidate);
    }

    @Test
    void shouldThrowExceptionWhenUpdateCandidateNotFound() {

        Integer id = 1;
        CandidateDto dto = new CandidateDto();

        when(jobCandidateRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> candidateService.updateCandidateWithSkills(id, dto));
        verify(jobCandidateRepository, times(1)).findById(id);
        verify(jobCandidateRepository, never()).save(any(JobCandidate.class));
    }

    @Test
    void shouldRemoveCandidate() {

        Integer id = 1;

        candidateService.removeCandidate(id);

        verify(jobCandidateRepository, times(1)).deleteById(id);
    }

    @Test
    void shouldAddSkillToCandidate() {

        Integer candidateId = 1;
        SkillDto skillDto = new SkillDto();
        skillDto.setSkillName("Spring");

        JobCandidate existingCandidate = new JobCandidate();
        existingCandidate.setId(candidateId);

        when(jobCandidateRepository.findById(candidateId)).thenReturn(Optional.of(existingCandidate));
        when(jobCandidateRepository.save(any(JobCandidate.class))).thenAnswer(i -> i.getArguments()[0]);

        CandidateDto result = candidateService.addSkillToCandidate(candidateId, skillDto);

        assertEquals(1, result.getSkills().size());
        assertEquals("Spring", result.getSkills().get(0).getSkillName());
        verify(jobCandidateRepository, times(1)).findById(candidateId);
        verify(jobCandidateRepository, times(1)).save(existingCandidate);
    }

    @Test
    void shouldRemoveSkillFromCandidate() {

        Integer candidateId = 1;
        Integer skillId = 2;

        JobCandidate existingCandidate = new JobCandidate();
        existingCandidate.setId(candidateId);

        Skill skill = new Skill();
        skill.setId(skillId);
        skill.setSkillName("Spring");
        existingCandidate.getSkills().add(skill);

        when(jobCandidateRepository.findById(candidateId)).thenReturn(Optional.of(existingCandidate));

        candidateService.removeSkillFromCandidate(candidateId, skillId);

        assertTrue(existingCandidate.getSkills().isEmpty());
        verify(jobCandidateRepository, times(1)).findById(candidateId);
        verify(jobCandidateRepository, times(1)).save(existingCandidate);
    }

    @Test
    void shouldSearchByName() {

        String name = "Test";
        JobCandidate candidate = new JobCandidate();
        candidate.setId(1);
        candidate.setName("TestName");

        when(jobCandidateRepository.findByNameContainingIgnoreCase(name)).thenReturn(List.of(candidate));

        List<CandidateDto> result = candidateService.searchByName(name);

        assertEquals(1, result.size());
        assertEquals("TestName", result.get(0).getName());
        verify(jobCandidateRepository, times(1)).findByNameContainingIgnoreCase(name);
    }

    @Test
    void shouldSearchBySkill() {

        String skillName = "Java";
        JobCandidate candidate = new JobCandidate();
        candidate.setId(1);
        candidate.setName("TestName");

        when(jobCandidateRepository.findBySkillNameParams(skillName)).thenReturn(List.of(candidate));

        List<CandidateDto> result = candidateService.searchBySkill(skillName);

        assertEquals(1, result.size());
        assertEquals("TestName", result.get(0).getName());
        verify(jobCandidateRepository, times(1)).findBySkillNameParams(skillName);
    }

    @Test
    void shouldGetAllCandidates() {

        JobCandidate candidate = new JobCandidate();
        candidate.setId(1);
        candidate.setName("TestName");

        when(jobCandidateRepository.findAll()).thenReturn(List.of(candidate));

        List<CandidateDto> result = candidateService.getAllCandidates();

        assertEquals(1, result.size());
        verify(jobCandidateRepository, times(1)).findAll();
    }
}
