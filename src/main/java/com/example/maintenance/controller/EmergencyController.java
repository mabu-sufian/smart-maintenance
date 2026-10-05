package com.example.maintenance.controller;

import com.example.maintenance.dto.*;
import com.example.maintenance.entity.Assignment;
import com.example.maintenance.entity.Emergency;
import com.example.maintenance.service.EmergencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergencies")
public class EmergencyController {

    private final EmergencyService emergencyService;

    public EmergencyController(EmergencyService emergencyService) {
        this.emergencyService = emergencyService;
    }

    @PostMapping("/{issueId}/create")
    public ResponseEntity<EmergencyResponseDTO> createEmergency(@PathVariable Long issueId, @RequestBody EmergencyRequestDTO requestDTO) {
        Emergency emergency = emergencyService.createEmergency(issueId, requestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(emergencyService.emergencyMappingToDTO(emergency));

    }

    @GetMapping("/{emergencyId}")
    public ResponseEntity<EmergencyResponseDTO> getEmergencyById(@PathVariable Long emergencyId) {
        Emergency emergency = emergencyService.getEmergencyById(emergencyId);
        return ResponseEntity
                .ok()
                .body(emergencyService.emergencyMappingToDTO(emergency));
    }

    @GetMapping()
    public ResponseEntity<List<EmergencyResponseDTO>> getAllEmergency() {
        return ResponseEntity.ok()
                .body(emergencyService.getAllEmergency());
    }


    @PostMapping("/{emergencyId}/status_update")
    public ResponseEntity<EmergencyResponseDTO> updateEmergencyStatus(@PathVariable Long emergencyId, @RequestBody EmergencyStatusUpdateReqDTO requestDTO) {
        Emergency emergency = emergencyService.updateEmergencyStatus(emergencyId, requestDTO);

        return ResponseEntity
                .ok()
                .body(emergencyService.emergencyMappingToDTO(emergency));

    }


    @PostMapping("/{emergencyId}/create_assignment")
    public ResponseEntity<AssignmentResponseDTO> createEmergencyAssignment(@PathVariable Long emergencyId, @RequestBody AssignmentRequestDTO requestDTO) {
        Assignment assignment = emergencyService.creatEmergencyAssignment(emergencyId, requestDTO.getTechnicianId());
        AssignmentResponseDTO responseDTO = new AssignmentResponseDTO();

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


    @GetMapping("/assignements/{Id}")
    public ResponseEntity<List<AssignmentResponseDTO>> getAssignmentByEmergencyId(@PathVariable Long Id) {
        return ResponseEntity.ok()
                .body(emergencyService.getAssignmnetByEmergencyId(Id));
    }

    @PatchMapping("/{id}/closed")
    public ResponseEntity<EmergencyResponseDTO> closedEmergency(@PathVariable Long id)
    {
        Emergency emergency=emergencyService.closedEmergency(id);
        return ResponseEntity
                .ok()
                .body(emergencyService.emergencyMappingToDTO(emergency));
    }


}



