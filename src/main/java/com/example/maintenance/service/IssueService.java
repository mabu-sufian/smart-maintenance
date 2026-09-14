package com.example.maintenance.service;

import com.example.maintenance.dto.IssueRequestDTO;
import com.example.maintenance.dto.IssueResponseDTO;
import com.example.maintenance.dto.IssueStatusUpdateRequest;
import com.example.maintenance.dto.UpdatePriorityRequest;
import com.example.maintenance.exception.IssueNotFoundException;
import com.example.maintenance.model.Issue;
import com.example.maintenance.model.IssueCategory;
import com.example.maintenance.model.IssuePriority;
import com.example.maintenance.model.IssueStatus;
import com.example.maintenance.repositories.IssueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueService {

    private final IssueRepository issueRepository;
    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }
    public Issue createIssue(IssueRequestDTO issueRequestDTO)
    {
        Issue issue = new Issue();

        issue.setTitle(issueRequestDTO.getTitle());
        issue.setLocation(issueRequestDTO.getLocation());
        issue.setDescription(issueRequestDTO.getDescription());
        issue.setIssueCategory(issueRequestDTO.getIssueCategory());
        issue.setIssuePriority(issueRequestDTO.getIssuePriority());
        issue.setIssueStatus(IssueStatus.REPORTED);
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
        return mapIssueToResponse(issue);
    }

    public Issue updateIssue(Long id, IssueRequestDTO issueRequestDTO) {
        Issue exisitngIssue = issueRepository.findById(id)
                .orElseThrow(()-> new IssueNotFoundException("Issue not found "+ id));


        exisitngIssue.setTitle(issueRequestDTO.getTitle());
        exisitngIssue.setDescription(issueRequestDTO.getDescription());
        exisitngIssue.setLocation(issueRequestDTO.getLocation());

        return issueRepository.save(exisitngIssue);
    }

    public void deleteIssue(Long id)
    {
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
        issue.setIssuePriority(newPriority);
        return issueRepository.save(issue);
    }

    public Issue updateCategory(Long id, IssueCategory newCategory)
    {
        Issue issue=issueRepository.findById(id)
                .orElseThrow(()-> new IssueNotFoundException("Issue Not Found "+ id));
        issue.setIssueCategory(newCategory);
        return issueRepository.save(issue);
    }



}
