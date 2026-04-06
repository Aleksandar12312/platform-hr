package com.intens.hr.service;

import com.intens.hr.dto.CandidateDto;
import com.intens.hr.dto.SkillDto;
import com.intens.hr.model.JobCandidate;
import com.intens.hr.model.Skill;
import com.intens.hr.repository.JobCandidateRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobCandidateService {

    private final JobCandidateRepository candidateRepository;

    public CandidateDto addCandidate(CandidateDto candidateDto) {
        JobCandidate entity = new JobCandidate();
        entity.setName(candidateDto.getName());
        entity.setBirthday(candidateDto.getBirthday());
        entity.setContactNumber(candidateDto.getContactNumber());
        entity.setEmail(candidateDto.getEmail());

        if (candidateDto.getSkills() != null) {
            for (SkillDto skillDto : candidateDto.getSkills()) {
                Skill skill = new Skill();
                skill.setSkillName(skillDto.getSkillName());
                skill.setJobCandidate(entity);
                entity.getSkills().add(skill);
            }
        }

        JobCandidate saved = candidateRepository.save(entity);
        return mapToDto(saved);
    }

    @Transactional
    public CandidateDto updateCandidateWithSkills(Integer id, CandidateDto candidateDto) {
        JobCandidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + id));

        candidate.setName(candidateDto.getName());
        candidate.setBirthday(candidateDto.getBirthday());
        candidate.setContactNumber(candidateDto.getContactNumber());
        candidate.setEmail(candidateDto.getEmail());

        if (candidateDto.getSkills() != null) {
            candidate.getSkills().clear();
            for (SkillDto skillDto : candidateDto.getSkills()) {
                Skill skill = new Skill();
                skill.setSkillName(skillDto.getSkillName());
                skill.setJobCandidate(candidate);
                candidate.getSkills().add(skill);
            }
        }

        JobCandidate saved = candidateRepository.save(candidate);
        return mapToDto(saved);
    }

    public void removeCandidate(Integer id) {
        candidateRepository.deleteById(id);
    }

    @Transactional
    public CandidateDto addSkillToCandidate(Integer candidateId, SkillDto skillDto) {
        JobCandidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + candidateId));

        Skill skill = new Skill();
        skill.setSkillName(skillDto.getSkillName());
        skill.setJobCandidate(candidate);

        candidate.getSkills().add(skill);
        JobCandidate saved = candidateRepository.save(candidate);
        return mapToDto(saved);
    }

    @Transactional
    public void removeSkillFromCandidate(Integer candidateId, Integer skillId) {
        JobCandidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + candidateId));

        candidate.getSkills().removeIf(skill -> skill.getId().equals(skillId));
        candidateRepository.save(candidate);
    }

    public List<CandidateDto> searchByName(String name) {
        return candidateRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CandidateDto> searchBySkill(String skillName) {
        return candidateRepository.findBySkillNameParams(skillName).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CandidateDto> getAllCandidates() {
        return candidateRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private CandidateDto mapToDto(JobCandidate entity) {
        CandidateDto dto = new CandidateDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setBirthday(entity.getBirthday());
        dto.setContactNumber(entity.getContactNumber());
        dto.setEmail(entity.getEmail());

        if (entity.getSkills() != null) {
            List<SkillDto> skillDtos = entity.getSkills().stream().map(skill -> {
                SkillDto sdto = new SkillDto();
                sdto.setId(skill.getId());
                sdto.setSkillName(skill.getSkillName());
                return sdto;
            }).collect(Collectors.toList());
            dto.setSkills(skillDtos);
        }
        return dto;
    }
}
