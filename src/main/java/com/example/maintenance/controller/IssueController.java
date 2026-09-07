package com.example.maintenance.controller;

import com.example.maintenance.model.Issue;
import com.example.maintenance.service.IssueService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;
   public IssueController(IssueService issueService)
    {
        this.issueService=issueService;
    }

    @PostMapping()
    public Issue createIssue( @RequestBody Issue issue)
    {
        return issueService.createIssue(issue);
    }

    @GetMapping()
    public List<Issue> getIssues()
    {
        return issueService.getAllIssues();
    }

    @GetMapping("/{id}")
    public Issue getIssueById( @PathVariable  Long id)
    {
        return issueService.getIssueById(id);
    }

    @PutMapping("/{id}")
    public Issue updateIssue(@PathVariable Long id, @RequestBody Issue issue)
    {
        return issueService.updateIssue(id,issue);
    }

    @DeleteMapping("/{id}")
    public void deleteIssue(@PathVariable Long id)
    {
        issueService.deleteIssue(id);
    }


}
