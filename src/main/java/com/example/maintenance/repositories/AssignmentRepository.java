package com.example.maintenance.repositories;

import com.example.maintenance.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment>findByTechnicianId(Long technician_id);
    List<Assignment>findByIssueId(Long issueId);
}
