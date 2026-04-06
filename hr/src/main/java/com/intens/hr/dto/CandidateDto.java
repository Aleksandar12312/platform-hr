package com.intens.hr.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class CandidateDto {
    private Integer id;
    private String name;
    private LocalDate birthday;
    private String contactNumber;
    private String email;
    private List<SkillDto> skills;
}
