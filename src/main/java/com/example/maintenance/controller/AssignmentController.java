package com.example.maintenance.controller;

import com.example.maintenance.dto.AssignmentRequestDTO;
import com.example.maintenance.dto.AssignmentResponseDTO;
import com.example.maintenance.entity.Assignment;
import com.example.maintenance.service.AssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService  assignmentService)
    {
        this.assignmentService=assignmentService;
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @PostMapping("/{issueId}")
    public ResponseEntity<AssignmentResponseDTO> createAssignment(@PathVariable Long issueId, @RequestBody AssignmentRequestDTO request )
    {
        Assignment assignment=assignmentService.createAssign(issueId, request.getTechnicianId());
        AssignmentResponseDTO responseDTO=new AssignmentResponseDTO();

        responseDTO.setId(assignment.getId());
        responseDTO.setIssueId(assignment.getIssue().getId());
        responseDTO.setTechnicianId(assignment.getTechnician().getId());
        responseDTO.setStatus(assignment.getStatus());
        responseDTO.setAssignedAt(assignment.getAssignedAt());
        responseDTO.setAcceptedAt(assignment.getAcceptedAt());
        responseDTO.setCompletedAt(assignment.getCompletedAt());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);

    }


    @GetMapping()
    public ResponseEntity<List<AssignmentResponseDTO>>getAllAssignment()
    {
      List<AssignmentResponseDTO> responseDTO=assignmentService.getAllAssignment();

        return ResponseEntity
                .ok().body(responseDTO);
    }

    @GetMapping("/me/assigned_issue")
    public ResponseEntity<List<AssignmentResponseDTO>> getAssignmnentByTechnicianId()
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Long userId=(Long) authentication.getPrincipal();

       List<AssignmentResponseDTO> responseDTO=assignmentService.getAssignmentByTechnicianId(userId);
       return ResponseEntity.ok(responseDTO);
    }


    @PatchMapping("/{assignmentId}/accept")
    public ResponseEntity<AssignmentResponseDTO> acceptAssignment(@PathVariable Long assignmentId)
    {
       Assignment assignment=assignmentService.AcceptAssignment(assignmentId);

       AssignmentResponseDTO responseDTO=assignmentService.mapAssingmnetToResponse(assignment);

       return ResponseEntity.ok(responseDTO);
    }

    @PreAuthorize("hasRole('TECHNICIAN')")
    @PatchMapping("/{assignmentId}/reject")
    public ResponseEntity<AssignmentResponseDTO> rejectAssignment(@PathVariable Long assignmentId)
    {
        Assignment assignment=assignmentService.rejectAssignment(assignmentId);
        AssignmentResponseDTO responseDTO=assignmentService.mapAssingmnetToResponse(assignment);
        return ResponseEntity.ok(responseDTO);
    }

    @PreAuthorize("hasRole('TECHNICIAN')")
    @PatchMapping("/{assignmentId}/complete")
    public ResponseEntity<AssignmentResponseDTO> completeAssingment(@PathVariable Long assignmentId)
    {
        Assignment assignment=assignmentService.completeAssignment(assignmentId);
        AssignmentResponseDTO responseDTO=assignmentService.mapAssingmnetToResponse(assignment);
        return ResponseEntity.ok(responseDTO);
    }




}
