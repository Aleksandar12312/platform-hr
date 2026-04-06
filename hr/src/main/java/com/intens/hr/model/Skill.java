package com.intens.hr.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "skill")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idskill", nullable = false)
    private Integer id;

    @Column(name = "skill_name", nullable = false, length = 45)
    private String skillName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idjob_candidate", nullable = false)
    private JobCandidate jobCandidate;
}