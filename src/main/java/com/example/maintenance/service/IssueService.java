package com.example.maintenance.service;

import com.example.maintenance.dto.IssueRequestDTO;
import com.example.maintenance.dto.IssueResponseDTO;
import com.example.maintenance.entity.User;
import com.example.maintenance.exception.IssueNotFoundException;
import com.example.maintenance.entity.Issue;
import com.example.maintenance.entity.Enum.IssueCategory;
import com.example.maintenance.entity.Enum.IssuePriority;
import com.example.maintenance.entity.Enum.IssueStatus;
import com.example.maintenance.repositories.IssueRepository;
import com.example.maintenance.repositories.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service

public class IssueService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    public IssueService(IssueRepository issueRepository, UserRepository userRepository) {
        this.issueRepository = issueRepository;
        this.userRepository=userRepository;
    }
    //OwnerShip check of an issue
    private Long getCurrentUserId()
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }


    private boolean isOwner(Issue issue)
    {
        Long currentUserId=getCurrentUserId();
        return issue.getCreatedBy()
                .getId().equals(currentUserId);
    }

    private boolean isAdmin()
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        return authentication.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }


    protected boolean isTechnicianOrAbove()
    {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();

        return authentication.getAuthorities()
                .stream()
                .anyMatch(authority -> {
                    String role= authority.getAuthority();
                    return role.equals("ROLE_TECHNICIAN") || role.equals("ROLE_ADMIN") || role.equals("ROLE_MANAGER");
                });
    }

    public Issue createIssue(IssueRequestDTO issueRequestDTO)
    {
        Long userId=getCurrentUserId();
        User currentUser=userRepository.findById(userId).orElseThrow(()->new UsernameNotFoundException("User not Found"));
        Issue issue = new Issue();

        issue.setTitle(issueRequestDTO.getTitle());
        issue.setLocation(issueRequestDTO.getLocation());
        issue.setDescription(issueRequestDTO.getDescription());
        issue.setIssueCategory(issueRequestDTO.getIssueCategory());
        issue.setIssuePriority(issueRequestDTO.getIssuePriority());
        issue.setIssueStatus(IssueStatus.REPORTED);
        issue.setCreatedBy(currentUser);
        issue.setCreatedAT(LocalDateTime.now());
       return issueRepository.save(issue);
    }

    public List<IssueResponseDTO>getAllIssues()
    {
        return issueRepository.findAll()
                .stream()
                .map(issue->mapIssueToResponse(issue))
                .toList();
    }

    public IssueResponseDTO getIssueById(Long id)
    {

        Issue issue= issueRepository.findById(id)
                .orElseThrow(()-> new IssueNotFoundException("Issue not found "+ id));
        if(!isAdmin() && !isOwner(issue))
        {
            throw new AccessDeniedException("You do not have permission to view this issue");
        }

        return mapIssueToResponse(issue);
    }


    public Issue updateIssue(Long id, IssueRequestDTO issueRequestDTO) {
        Issue exisitngIssue = issueRepository.findById(id)
                .orElseThrow(()-> new IssueNotFoundException("Issue not found "+ id));

              if(!isAdmin() && !isOwner(exisitngIssue))
              {
                  throw new AccessDeniedException("You do not have permission to update this issue");
              }
        exisitngIssue.setTitle(issueRequestDTO.getTitle());
        exisitngIssue.setDescription(issueRequestDTO.getDescription());
        exisitngIssue.setLocation(issueRequestDTO.getLocation());

        return issueRepository.save(exisitngIssue);
    }


    public void deleteIssue(Long id)
    {
        Issue issue=issueRepository.findById(id)
                        .orElseThrow(()-> new IssueNotFoundException("Issue Not Found"));

        if(!isOwner(issue) && !isAdmin())
        {
            throw new AccessDeniedException("You do not have permission to update this issue");
        }
        issueRepository.deleteById(id);
    }

    public IssueResponseDTO mapIssueToResponse(Issue issue)
    {
        IssueResponseDTO issueResponseDTO=new IssueResponseDTO();
        issueResponseDTO.setId(issue.getId());
        issueResponseDTO.setDescription(issue.getDescription());
        issueResponseDTO.setLocation(issue.getLocation());
        issueResponseDTO.setTitle(issue.getTitle());
//        issueResponseDTO.setIssueCategory(issue.getIssueCategory());
//        issueResponseDTO.setIssuePriority(issue.getIssuePriority());
//        issueResponseDTO.setIssueStatus(issue.getIssueStatus());
        return issueResponseDTO;
    }


    public Issue updateStatus(Long id, IssueStatus newStatus)
    {
        if(!isTechnicianOrAbove())
        {
            throw new AccessDeniedException(" Only technician, manager or admin can update issue status");
        }


        Issue issue=issueRepository.findById(id)
                .orElseThrow(()->new IssueNotFoundException("Issue is not found"));

        IssueStatus current=issue.getIssueStatus();

        if(!isValidTransition(current, newStatus))
        {
            throw new RuntimeException("Invalid Status: "+ current +" --> "+newStatus);

        }
        issue.setIssueStatus(newStatus);
        return  issueRepository.save(issue);

    }

    private boolean isValidTransition(IssueStatus current, IssueStatus next)
    {
        switch (current)
        {
            case REPORTED:
                return next == IssueStatus.ACKNOWLEDGED;
            case ACKNOWLEDGED:
                return next==IssueStatus.ASSIGNED;
            case ASSIGNED:
                return next==IssueStatus.IN_PROGRESS;
            case IN_PROGRESS:
                return next==IssueStatus.RESOLVED;
            case RESOLVED:
                return next==IssueStatus.CLOSED;
            default:
                return false;
        }

    }

    public Issue updatePriority(Long id, IssuePriority newPriority)
    {
        Issue issue= issueRepository.findById(id)
                .orElseThrow(()->new IssueNotFoundException("Issue Not Found "+ id));
        if(!isOwner(issue) && !isTechnicianOrAbove())
        {
            throw new AccessDeniedException(" You do not have permission to update issue priority");
        }
        issue.setIssuePriority(newPriority);
        return issueRepository.save(issue);
    }

    public Issue updateCategory(Long id, IssueCategory newCategory)
    {
        Issue issue=issueRepository.findById(id)
                .orElseThrow(()-> new IssueNotFoundException("Issue Not Found "+ id));
        if(!isOwner(issue) && !isTechnicianOrAbove())
        {
            throw new AccessDeniedException(" You do not have permission to update issue priority");
        }

        issue.setIssueCategory(newCategory);
        return issueRepository.save(issue);
    }



}
