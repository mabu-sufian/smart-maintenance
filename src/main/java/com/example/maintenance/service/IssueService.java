package com.example.maintenance.service;

import com.example.maintenance.model.Issue;
import com.example.maintenance.repositories.IssueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueService {

    private final IssueRepository issueRepository;
    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }
    public Issue createIssue(Issue issue)
    {
       return issueRepository.save(issue);
    }

    public List<Issue>getAllIssues()
    {
        return issueRepository.findAll();
    }

    public Issue getIssueById(Long id)
    {
        return issueRepository.findById(id);
    }

    public Issue updateIssue(Long id, Issue issue)
    {
        return issueRepository.update(id,issue);
    }

    public void deleteIssue(Long id)
    {
        issueRepository.delete(id);
    }


}
