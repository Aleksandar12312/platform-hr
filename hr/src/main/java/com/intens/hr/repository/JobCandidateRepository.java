package com.intens.hr.repository;

import com.intens.hr.model.JobCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobCandidateRepository extends JpaRepository<JobCandidate, Integer> {
    JobCandidate findByName(String name);
    List<JobCandidate> findByNameContainingIgnoreCase(String name);

    @Query("SELECT DISTINCT c FROM JobCandidate c JOIN c.skills s WHERE LOWER(s.skillName) LIKE LOWER(CONCAT('%', :skillName, '%'))")
    List<JobCandidate> findBySkillNameParams(@Param("skillName") String skillName);
}
