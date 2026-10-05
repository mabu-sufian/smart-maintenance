package com.example.maintenance.service;

import com.example.maintenance.dto.*;
import com.example.maintenance.entity.Assignment;
import com.example.maintenance.entity.Emergency;
import com.example.maintenance.entity.Enum.EmergencyStatus;
import com.example.maintenance.entity.Issue;
import com.example.maintenance.entity.User;
import com.example.maintenance.exception.AssignmentNotFoundException;
import com.example.maintenance.exception.IssueNotFoundException;
import com.example.maintenance.repositories.AssignmentRepository;
import com.example.maintenance.repositories.EmergencyRepository;
import com.example.maintenance.repositories.IssueRepository;
import com.example.maintenance.repositories.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmergencyService {

    private final EmergencyRepository emergencyRepository;
    private  final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final AssignmentService assignmentService;
    private final IssueService issueService;
    private final AssignmentRepository assignmentRepository;


    public EmergencyService(EmergencyRepository emergencyRepository,
                            IssueRepository issueRepository,
                            UserRepository userRepository,
                            AssignmentService assignmentService,
                            IssueService issueService,
                            AssignmentRepository assignmentRepository)
    {
        this.emergencyRepository=emergencyRepository;
        this.issueRepository=issueRepository;
        this.userRepository=userRepository;
        this.assignmentService=assignmentService;
        this.issueService=issueService;
        this.assignmentRepository=assignmentRepository;
    }

    public Emergency createEmergency(Long issueId, EmergencyRequestDTO requestDTO)
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Long userId=(Long) authentication.getPrincipal();

        User user=userRepository.findById(userId)
                .orElseThrow(()->new UsernameNotFoundException("You didn't create any issue"));

        Issue issue=issueRepository.findById(issueId)
                .orElseThrow(()-> new IssueNotFoundException("Issue Not Found...."));

        Emergency emergency=new Emergency();
        emergency.setIssue(issue);
        emergency.setDescription(requestDTO.getDescription());
        emergency.setEmergencySeverity(requestDTO.getSeverrity());
        emergency.setEmergencyStatus(EmergencyStatus.REPORTED);
        emergency.setReportedBy(user);
        emergency.setLocation(issue.getLocation());
        emergency.setCreatedAt(LocalDateTime.now());
        emergency.setTitle(issue.getTitle());

        emergencyRepository.save(emergency);
        return emergency;
    }

    public Emergency getEmergencyById(Long emergencyId)
    {
        Emergency emergency = emergencyRepository.findById(emergencyId)
                .orElseThrow(()-> new RuntimeException("Emergency Not found.."));

        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        Long userId= (Long) authentication.getPrincipal();
        if(userId != emergency.getReportedBy().getId() && !assignmentService.isAdmin() && !assignmentService.isManager())
        {
            throw new RuntimeException("You are able to view this emergency");
        }

        return emergency;
    }

    public List<EmergencyResponseDTO> getAllEmergency()
    {
        if(!assignmentService.isAdmin() && !assignmentService.isManager())
        {
            throw new RuntimeException("You are not able to view all emergencies...");
        }
        return emergencyRepository.findAll()
                .stream()
                .map(emergency -> emergencyMappingToDTO(emergency))
                .toList();
    }


    public Emergency updateEmergencyStatus(Long emergencyId, EmergencyStatusUpdateReqDTO reqDTO)
    {
        Emergency emergency=emergencyRepository.findById(emergencyId)
                .orElseThrow(()->new RuntimeException("Emergency Not Found..."));

        if(!issueService.isTechnicianOrAbove())
        {
            throw new AccessDeniedException("You are not able to update status");
        }

        if(!isValidEmergencyStatusTranstion(emergency.getEmergencyStatus(),reqDTO.getStatus()))
        {
            throw new RuntimeException("Status Transition is not Valid..");
        }

        emergency.setEmergencyStatus(reqDTO.getStatus());
        timeStampUpdate(emergency,emergency.getEmergencyStatus());
        emergencyRepository.save(emergency);
        return emergency;

    }


    @Transactional
    public Assignment creatEmergencyAssignment(Long emergencyId, Long technicianId)
    {
        Emergency emergency=emergencyRepository.findById(emergencyId).orElseThrow(()->new RuntimeException("No emergency Found.."));
        Long issueId=emergency.getIssue().getId();

        Assignment assignment=assignmentService.createAssign(issueId,technicianId);
        emergency.setAssignedAt(LocalDateTime.now());
        emergency.setEmergencyStatus(EmergencyStatus.ASSIGNED);

        emergencyRepository.save(emergency);
        assignment.setEmergency(emergency);
        return assignmentRepository.save(assignment);
    }


    public List<AssignmentResponseDTO> getAssignmnetByEmergencyId(Long emergencyId)
    {
        return assignmentRepository.findByEmergencyId(emergencyId)
                .stream()
                .map(assignment -> assignmentService.mapAssingmnetToResponse(assignment))
                .toList();
    }



    @Transactional
    public Emergency closedEmergency(Long emergencyId)
    {
        Emergency emergency=emergencyRepository.findById(emergencyId)
                .orElseThrow(()->new RuntimeException("Emergency Not found..."));

        if(!assignmentService.isAdmin() && !assignmentService.isManager())
        {
            throw  new RuntimeException("You are not able to close this assignments");
        }

        if(!emergency.getEmergencyStatus().equals(EmergencyStatus.RESOLVED))
        {
            throw  new RuntimeException("Only Resolve emergency can be closed.");
        }
        emergency.setEmergencyStatus(EmergencyStatus.CLOSED);
        emergency.setClosedAt(LocalDateTime.now());
        emergencyRepository.save(emergency);
        return emergency;
    }


    private boolean isValidEmergencyStatusTranstion(EmergencyStatus current, EmergencyStatus next)
    {
        switch (current)
        {
            case REPORTED:
                return next == EmergencyStatus.ACKNOWLEDGED;
            case ACKNOWLEDGED:
                return next==EmergencyStatus.ASSIGNED;
            case ASSIGNED:
                return next ==EmergencyStatus.RESPONDING;
            case RESPONDING:
                return next == EmergencyStatus.RESOLVED;
            case RESOLVED:
                return next==EmergencyStatus.CLOSED;
            default:
                return false;
        }}



    private void timeStampUpdate(Emergency emergency, EmergencyStatus emergencyStatus)
    {
        LocalDateTime time=LocalDateTime.now();
        switch (emergencyStatus)
        {
            case ACKNOWLEDGED ->
                emergency.setAcknowledgedAt(time);
            case ASSIGNED ->
                emergency.setAssignedAt(time);
            case RESPONDING ->
                emergency.setResponseStartedAt(time);
            case RESOLVED ->
                emergency.setResolvedAt(time);
            case CLOSED ->
                emergency.setClosedAt(time);

        }
    }



    public EmergencyResponseDTO emergencyMappingToDTO(Emergency emergency)
    {
        EmergencyResponseDTO responseDTO=new EmergencyResponseDTO();
        responseDTO.setId(emergency.getId());
        responseDTO.setTitle(emergency.getTitle());
        responseDTO.setDescription(emergency.getDescription());
        responseDTO.setIssueId(emergency.getIssue().getId());
        responseDTO.setLocation(emergency.getLocation());
        responseDTO.setEmergencySeverity(emergency.getEmergencySeverity().name());
        responseDTO.setEmergencyStatus(emergency.getEmergencyStatus().name());

        Long userId=emergency.getReportedBy().getId();
        String userName=emergency.getReportedBy().getName();
        String userEmail=emergency.getReportedBy().getEmail();
        String userRole=emergency.getReportedBy().getRole().name();

        responseDTO.setUser(new RegisteredResponseDTO(userId,userName,userEmail,userRole));
        responseDTO.setCreatedAt(emergency.getCreatedAt());
        responseDTO.setAcknowledgedAt(emergency.getAcknowledgedAt());
        responseDTO.setAssignedAt(emergency.getAssignedAt());
        responseDTO.setResponseStartedAt(emergency.getResponseStartedAt());
        responseDTO.setResolvedAt(emergency.getResolvedAt());
        responseDTO.setClosedAt(emergency.getClosedAt());

        return responseDTO;
    }



}
