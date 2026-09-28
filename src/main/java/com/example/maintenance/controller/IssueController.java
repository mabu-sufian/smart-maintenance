package com.example.maintenance.controller;

import com.example.maintenance.dto.*;
import com.example.maintenance.entity.Issue;
import com.example.maintenance.service.IssueService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public Issue createIssue( @Valid @RequestBody IssueRequestDTO issueRequestDTO)
    {
        return issueService.createIssue(issueRequestDTO);
    }

    @GetMapping()
    public List<IssueResponseDTO> getALLIssues()
    {
        return issueService.getAllIssues();
    }

    @GetMapping("/{id}")
    public IssueResponseDTO getIssueById( @PathVariable  Long id)
    {
        return issueService.getIssueById(id);
    }

    @PutMapping("/{id}")
    public Issue updateIssue(@PathVariable Long id, @Valid @RequestBody IssueRequestDTO issueRequestDTO)
    {
        return issueService.updateIssue(id, issueRequestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteIssue(@PathVariable Long id)
    {
        issueService.deleteIssue(id);
    }

    @PatchMapping("/{id}/status")
//    public ResponseEntity<Issue>updateStatus(@Valid @PathVariable Long id, @RequestBody IssueStatusUpdateRequest request)
    public Issue updateStatus( @PathVariable Long id, @RequestBody IssueStatusUpdateRequest request)
    {
//        Issue updatedIssue= issueService.updateStatus(id, request.getStatus());
//        return ResponseEntity.ok(updatedIssue);

        return issueService.updateStatus(id,request.getStatus());
    }

    @PatchMapping("/{id}/priority")
    public ResponseEntity<Issue> updatePriority(@PathVariable Long id, @RequestBody UpdatePriorityRequest request)
    {
        Issue updatedPriority = issueService.updatePriority(id, request.getIssuePriority());
        return ResponseEntity.ok(updatedPriority);
    }

    @PatchMapping("/{id}/category")
    public ResponseEntity<Issue> updateCategory(@PathVariable Long id, @RequestBody UpdateCategoryRequest request)
    {
        Issue updatedCategory = issueService.updateCategory(id, request.getIssueCategory());
        return ResponseEntity.ok(updatedCategory);
    }


}
