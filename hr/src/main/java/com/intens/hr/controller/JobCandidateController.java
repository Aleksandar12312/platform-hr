package com.intens.hr.controller;

import com.intens.hr.dto.CandidateDto;
import com.intens.hr.dto.SkillDto;
import com.intens.hr.service.JobCandidateService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class JobCandidateController {

    private final JobCandidateService candidateService;

    @PostMapping
    public ResponseEntity<CandidateDto> addCandidate(@RequestBody CandidateDto candidateDto) {
        return new ResponseEntity<>(candidateService.addCandidate(candidateDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidateDto> updateCandidateWithSkills(@PathVariable Integer id,
            @RequestBody CandidateDto candidateDto) {
        return ResponseEntity.ok(candidateService.updateCandidateWithSkills(id, candidateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCandidate(@PathVariable Integer id) {
        candidateService.removeCandidate(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/skills")
    public ResponseEntity<CandidateDto> addSkill(@PathVariable("id") Integer candidateId,
            @RequestBody SkillDto skillDto) {
        return ResponseEntity.ok(candidateService.addSkillToCandidate(candidateId, skillDto));
    }

    @DeleteMapping("/{candidateId}/skills/{skillId}")
    public ResponseEntity<Void> removeSkill(@PathVariable("candidateId") Integer candidateId,
            @PathVariable("skillId") Integer skillId) {
        candidateService.removeSkillFromCandidate(candidateId, skillId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CandidateDto>> getAllCandidates() {
        return ResponseEntity.ok(candidateService.getAllCandidates());
    }

    @GetMapping("/search-by-name")
    public ResponseEntity<List<CandidateDto>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(candidateService.searchByName(name));
    }

    @GetMapping("/search-by-skill")
    public ResponseEntity<List<CandidateDto>> searchBySkill(@RequestParam String skill) {
        return ResponseEntity.ok(candidateService.searchBySkill(skill));
    }
}
