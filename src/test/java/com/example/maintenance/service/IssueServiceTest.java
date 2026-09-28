package com.example.maintenance.service;

import com.example.maintenance.dto.IssueRequestDTO;
import com.example.maintenance.dto.IssueResponseDTO;
import com.example.maintenance.exception.IssueNotFoundException;
import com.example.maintenance.entity.Issue;
import com.example.maintenance.entity.Enum.IssueStatus;
import com.example.maintenance.repositories.IssueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IssueServiceTest {
    @Mock
    private IssueRepository issueRepository;

    @InjectMocks
    private IssueService issueService;

    private Issue issue;

    @BeforeEach
    void setUp()
    {
        issue =new Issue();
        issue.setId(1L);
        issue.setTitle("Broken Ac");
        issue.setIssueStatus(IssueStatus.REPORTED);
    }

    @Test
    void shouldCreateIssue()
    {
        IssueRequestDTO issueDT=new IssueRequestDTO();
        issueDT.setTitle("Broken AC");
        issueDT.setDescription("Ac is not working");
        issueDT.setLocation("Office");

        when(issueRepository.save(any(Issue.class))).thenReturn(issue);

        Issue result=issueService.createIssue(issueDT);

        assertEquals("Broken Ac", result.getTitle());
        verify(issueRepository).save(any(Issue.class));

    }

    @Test
    void shouldReturnIssueById()
    {

        when(issueRepository.findById(1L)).thenReturn(Optional.of(issue));

        IssueResponseDTO result = issueService.getIssueById(1L);

        assertEquals("Broken Ac", result.getTitle());
        verify(issueRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionIssueNotFound()
    {
        when(issueRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IssueNotFoundException.class, ()-> issueService.getIssueById(999L));

        verify(issueRepository).findById(999L);

    }


    @Test
    void shouldUpdateStatusWhenTransitionIsValid()
    {
        when(issueRepository.findById(1L)).thenReturn(Optional.of(issue));
        when(issueRepository.save(any(Issue.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Issue result=issueService.updateStatus(1L, IssueStatus.ACKNOWLEDGED);

        assertEquals(IssueStatus.ACKNOWLEDGED, result.getIssueStatus());

        verify(issueRepository).findById(1L);
        verify(issueRepository).save(issue);
    }

    @Test
    void shouldThrowExceptionWhenTransitionIsValid()
    {
        when(issueRepository.findById(1L)).thenReturn(Optional.of(issue));

        assertThrows(RuntimeException.class,()->issueService.updateStatus(1L,IssueStatus.CLOSED));
        verify(issueRepository).findById(1L);
        verify(issueRepository,never()).save(any(Issue.class));
    }


}