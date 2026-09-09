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
        return issueRepository.findById(id).orElseThrow(()-> new RuntimeException("Issue not found"+ id));
    }

    public Issue updateIssue(Long id, Issue issue) {
        Issue exisitngIssue = issueRepository.findById(id).orElseThrow(()-> new RuntimeException("Issue not found"+ id));


        exisitngIssue.setTitle(issue.getTitle());
        exisitngIssue.setDescription(issue.getDescription());
        exisitngIssue.setLocation(issue.getLocation());

        return issueRepository.save(exisitngIssue);
    }

    public void deleteIssue(Long id)
    {
        issueRepository.deleteById(id);
    }


}
