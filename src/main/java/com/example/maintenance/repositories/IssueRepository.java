package com.example.maintenance.repositories;

import com.example.maintenance.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


public interface IssueRepository extends JpaRepository<Issue,Long> {
    
}




// -----------------Custome code for jpa repository without jpaRepository----------------------
//@Repository
//public class IssueRepository {
//     private final List<Issue> issueList= new ArrayList<>();
//     private long nextId=1;
//
//    public Issue save(Issue issue)
//    {
//        issue.setId(nextId++);
//        issueList.add(issue);
//        return issue;
//    }
//
//    public List<Issue>findAll()
//    {
//        return issueList;
//    }
//
//    public  Issue findById(Long id)
//    {
//        for(Issue issue: issueList)
//        {
//            if(issue.getId().equals(id))
//            {
//                return issue;
//            }
//        }
//        return null;
//    }
//
//    public Issue update(Long id, Issue updatedIssue)
//    {
//        for(Issue issue:issueList)
//        {
//            if(issue.getId().equals(id))
//            {
//                issue.setTitle(updatedIssue.getTitle());
//                issue.setDescription(updatedIssue.getDescription());
//                issue.setLocation(updatedIssue.getLocation());
//
//                return issue;
//            }
//        }
//        return null;
//    }
//
//    public boolean delete(Long id)
//    {
//        boolean removed = issueList.removeIf(issue ->
//                issue.getId().equals(id));
//
//        return removed;
//    }
//
//
//}
