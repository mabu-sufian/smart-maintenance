package com.example.maintenance.service;

import com.example.maintenance.dto.AssignmentResponseDTO;
import com.example.maintenance.entity.Assignment;
import com.example.maintenance.entity.Emergency;
import com.example.maintenance.entity.Enum.AssignmentStatus;
import com.example.maintenance.entity.Enum.EmergencyStatus;
import com.example.maintenance.entity.Enum.IssueStatus;
import com.example.maintenance.entity.Enum.Role;
import com.example.maintenance.entity.Issue;
import com.example.maintenance.entity.User;
import com.example.maintenance.exception.AssignmentNotFoundException;
import com.example.maintenance.exception.IssueNotFoundException;
import com.example.maintenance.exception.WrongTechnicianException;
import com.example.maintenance.repositories.AssignmentRepository;
import com.example.maintenance.repositories.EmergencyRepository;
import com.example.maintenance.repositories.IssueRepository;
import com.example.maintenance.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final IssueRepository issueRepository;
    private final EmergencyRepository emergencyRepository;

    public AssignmentService(AssignmentRepository assignmentRepository, IssueRepository issueRepository, UserRepository userRepository,
                             EmergencyRepository emergencyRepository)
    {
        this.assignmentRepository=assignmentRepository;
        this.issueRepository=issueRepository;
        this.userRepository=userRepository;
        this.emergencyRepository=emergencyRepository;
    }

    public boolean isManager()
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_MANAGER"));
    }

    public boolean isAdmin()
    {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities()
                .stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }



    public Assignment createAssign(Long issueId, Long technicianId)
    {
        Issue issue=issueRepository.findById(issueId)
                .orElseThrow(()-> new IssueNotFoundException("Issue Not found"));

        User technician=userRepository.findById(technicianId)
                .orElseThrow(()->new UsernameNotFoundException("Technician not found: " + technicianId));

        if(technician.getRole() != Role.TECHNICIAN)
        {
            throw new RuntimeException(technician.getRole().name()+ " is not a Technician.. ");
        }

        if(!isManager() && !isAdmin())
        {
            throw  new RuntimeException("You are not allowed to assign technician");
        }

        Assignment assignment=new Assignment();

        assignment.setIssue(issue);
        assignment.setTechnician(technician);
        assignment.setAssignedAt(LocalDateTime.now());
        assignment.setStatus(AssignmentStatus.ASSIGNED);

        issue.setIssueStatus(IssueStatus.ASSIGNED);
        issueRepository.save(issue);
        return assignmentRepository.save((assignment));

    }


    public List<AssignmentResponseDTO> getAllAssignment()
    {

        if(!isAdmin() && !isManager())
        {
            throw new RuntimeException("You are not allowed to see all assignments..");
        }
        return assignmentRepository.findAll()
                .stream()
                .map(assignment -> mapAssingmnetToResponse(assignment))
                .toList();
    }



    public List<AssignmentResponseDTO> getAssignmentByTechnicianId(Long technicianId)
    {

        User technician=userRepository.findById(technicianId).orElseThrow(()->new UsernameNotFoundException("Technician is not found.."));
        if (technician.getRole() != Role.TECHNICIAN) {
            throw new RuntimeException("User is not a technician");
        }


        return assignmentRepository.findByTechnicianId(technicianId)
                .stream()
                .map(assignment -> mapAssingmnetToResponse(assignment))
                .toList();
    }


    public AssignmentResponseDTO mapAssingmnetToResponse(Assignment assignment)
    {
        AssignmentResponseDTO responseDTO=new AssignmentResponseDTO();
        responseDTO.setId(assignment.getId());
        responseDTO.setIssueId(assignment.getIssue().getId());
        responseDTO.setTechnicianId(assignment.getTechnician().getId());
        responseDTO.setStatus(assignment.getStatus());
        responseDTO.setAssignedAt(assignment.getAssignedAt());
        responseDTO.setAcceptedAt(assignment.getAcceptedAt());
        responseDTO.setCompletedAt(assignment.getCompletedAt());

        return responseDTO;
    }


    @Transactional
    public Assignment AcceptAssignment(Long assignmentId)
    {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        Long userId=(Long) authentication.getPrincipal();
        Assignment assignment=assignmentRepository.findById(assignmentId)
                .orElseThrow(()-> new AssignmentNotFoundException("Assignment Not found..."));
        User technician=assignment.getTechnician();
        if(!technician.getId().equals(userId))
        {
            throw new WrongTechnicianException("You are not assigned by this assignment..");

        }

        if(assignment.getStatus() != AssignmentStatus.ASSIGNED )
        {
            throw  new RuntimeException("Only ASSIGNED assignment can be accepted");
        }

        assignment.setStatus(AssignmentStatus.ACCEPTED);
        assignment.setAcceptedAt(LocalDateTime.now());
        assignmentRepository.save(assignment);

        Issue issue=assignment.getIssue();
        issue.setIssueStatus(IssueStatus.IN_PROGRESS);
        issueRepository.save(issue);

        if(assignment.getEmergency() !=null)
        {
           Emergency emergency= assignment.getEmergency();
           emergency.setEmergencyStatus(EmergencyStatus.RESPONDING);
           emergency.setResponseStartedAt(LocalDateTime.now());
           emergencyRepository.save(emergency);

        }

        return assignment;
    }



    public  Assignment rejectAssignment(Long assignmentId)
    {
        Assignment assignment=assignmentRepository.findById(assignmentId)
                .orElseThrow(()->new AssignmentNotFoundException("Assignment not found.."));
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        Long userId=(Long) authentication.getPrincipal();

        User technician=assignment.getTechnician();

        if(technician.getId().equals(userId))
        {
            throw new WrongTechnicianException("You are not assigned by this assignments.. ");

        }

        if(assignment.getStatus() != AssignmentStatus.ASSIGNED)
        {
            throw  new RuntimeException("Only Assigned issue can be rejected...");
        }

        assignment.setStatus(AssignmentStatus.REJECTED);
        assignmentRepository.save(assignment);

        return assignment;
    }



    @Transactional
    public Assignment completeAssignment(Long assignmentId)
    {
        Assignment assignment=assignmentRepository.findById(assignmentId)
                .orElseThrow(()-> new AssignmentNotFoundException("Assignment not found..."));

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Long userId=(Long) authentication.getPrincipal();
        User technician=assignment.getTechnician();
        if(!technician.getId().equals(userId))
        {
            throw new WrongTechnicianException("You are not assigned by this assignment..");
        }
        if(!assignment.getStatus().equals(AssignmentStatus.ACCEPTED))
        {
            throw  new RuntimeException("Only accepted assignment can be completed.. ");
        }

        assignment.setStatus(AssignmentStatus.COMPLETED);
        assignment.setCompletedAt(LocalDateTime.now());

        Issue issue=assignment.getIssue();
        issue.setIssueStatus(IssueStatus.RESOLVED);
        issueRepository.save(issue);

        if(assignment.getEmergency() !=null)
        {
            Emergency emergency= assignment.getEmergency();
            emergency.setEmergencyStatus(EmergencyStatus.RESOLVED);
            emergency.setResolvedAt(LocalDateTime.now());
            emergencyRepository.save(emergency);

        }

       return assignmentRepository.save(assignment);


    }


}
