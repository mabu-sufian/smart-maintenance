package com.example.maintenance.service;

import com.example.maintenance.entity.Assignment;
import com.example.maintenance.entity.Enum.AssignmentStatus;
import com.example.maintenance.entity.Enum.Role;
import com.example.maintenance.entity.Issue;
import com.example.maintenance.entity.User;
import com.example.maintenance.repositories.AssignmentRepository;
import com.example.maintenance.repositories.IssueRepository;
import com.example.maintenance.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceTest {
    @Mock
    private AssignmentRepository assignmentRepository;
    @Mock
    private UserRepository userRepository;

    @Mock
    private IssueRepository issueRepository;

    @InjectMocks
    private AssignmentService assignmentService;

    @BeforeEach
    void setUpSecurityContext() {

        Authentication authentication =
                mock(Authentication.class);

        when(authentication.getAuthorities())
                .thenAnswer(invocation ->
                        List.of(
                                new SimpleGrantedAuthority("ROLE_MANAGER")
                        )
                );;

        SecurityContext securityContext =
                SecurityContextHolder.createEmptyContext();

        securityContext.setAuthentication(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldAssignmentCreate()
    {
        Issue issue=new Issue();
        issue.setId(10L);

        User technician=new User();
        technician.setId(20L);
        technician.setRole(Role.TECHNICIAN);

        Assignment assignment=new Assignment();
        assignment.setIssue(issue);
        assignment.setTechnician(technician);
        assignment.setStatus(AssignmentStatus.ASSIGNED);

        when(issueRepository.findById(10L))
            .thenReturn(Optional.of(issue));
        when(userRepository.findById(20L)).thenReturn(Optional.of(technician));

        when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);

        Assignment result=assignmentService.createAssign(10L, 20L);

        assertEquals(AssignmentStatus.ASSIGNED, result.getStatus());

        verify(assignmentRepository).save(any(Assignment.class));

    }

}