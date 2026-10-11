package com.example.maintenance.service;

import com.example.maintenance.entity.Assignment;
import com.example.maintenance.entity.Enum.AssignmentStatus;
import com.example.maintenance.entity.Enum.IssueStatus;
import com.example.maintenance.entity.Enum.NotificationType;
import com.example.maintenance.entity.Enum.Role;
import com.example.maintenance.entity.Issue;
import com.example.maintenance.entity.User;
import com.example.maintenance.repositories.AssignmentRepository;
import com.example.maintenance.repositories.IssueRepository;
import com.example.maintenance.repositories.UserRepository;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RemainderSchedularSerice {
    private final AssignmentService assignmentService;
    private final AssignmentRepository assignmentRepository;
    private final NotificationService notificationService;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;

    public RemainderSchedularSerice(AssignmentService assignmentService, AssignmentRepository assignmentRepository,
                                    NotificationService notificationService,
                                    IssueRepository issueRepository,
                                    UserRepository userRepository) {
        this.assignmentService = assignmentService;
        this.assignmentRepository = assignmentRepository;
        this.notificationService = notificationService;
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
    }

//    @Scheduled(fixedDelay = 10000)
//    public void checkOverDueAssignment() {
//        LocalDateTime now = LocalDateTime.now();
//        List<Assignment> assignments = assignmentRepository.findAll();
//
//        for (Assignment assignment1 : assignments) {
//            if ( assignment1.getDeadline() == null ) {
//                continue;
//            }
//
//if(assignment1.getStatus() == AssignmentStatus.COMPLETED)
//{
//    continue;
//}
//            if(assignment1.getDeadline().isAfter(now) )
//            {
//                continue;
//            }
//
//            Long technicianId = assignment1.getTechnician().getId();
//            String message =
//                    "Assignment #" + assignment1.getId()
//                            + " is overdue and has not been completed.";
//
//            notificationService.createSchedulingNotification_IfAbsent(technicianId,
//                    assignment1.getIssue().getTitle(), message, NotificationType.ASSIGNMENT_OVERDUE);
//        }
//
//    }
@Scheduled(fixedDelay = 10000)
public void checkOverDueAssignment() {

    LocalDateTime now = LocalDateTime.now();
    List<Assignment> assignments = assignmentRepository.findAll();

    System.out.println("===== OVERDUE SCHEDULER STARTED =====");
    System.out.println("Current time: " + now);
    System.out.println("Total assignments: " + assignments.size());

    for (Assignment assignment : assignments) {

        System.out.println("-----------------------------");
        System.out.println("Assignment ID: " + assignment.getId());
        System.out.println("Status: " + assignment.getStatus());
        System.out.println("Deadline: " + assignment.getDeadline());

        if (assignment.getDeadline() == null) {
            System.out.println("SKIPPED: Deadline is null");
            continue;
        }

        if (assignment.getStatus() == AssignmentStatus.COMPLETED) {
            System.out.println("SKIPPED: Assignment completed");
            continue;
        }

        if (assignment.getDeadline().isAfter(now)) {
            System.out.println("SKIPPED: Deadline has not passed");
            continue;
        }

        Long technicianId = assignment.getTechnician().getId();

        String message = "Assignment #" + assignment.getId()
                + " is overdue and has not been completed.";

        System.out.println("OVERDUE DETECTED!");
        System.out.println("Technician ID: " + technicianId);
        System.out.println("Message: " + message);

        notificationService.createSchedulingNotification_IfAbsent(
                technicianId,
                assignment.getIssue().getTitle(),
                message,
                NotificationType.ASSIGNMENT_OVERDUE
        );

        System.out.println("Notification method call finished.");
    }
}
    @Scheduled(fixedDelay = 60000)
    public void checkPendingAssignment() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(10);
        List<Issue> issues = issueRepository.findAll();
        List<User> managers = userRepository.findByRole(Role.MANAGER);

        for (Issue issue : issues) {
            if(issue.getCreatedAT() == null)
            {
                continue;
            }

            if( issue.getIssueStatus() != IssueStatus.REPORTED )
            {
                continue;
            }
            if (issue.getCreatedAT().isAfter(threshold) ) {
                continue;
            }
            String message =
                    "Issue #" + issue.getId()
                            + " has not been assigned within 30 minutes.";

            for (User manager : managers) {
                notificationService.createSchedulingNotification_IfAbsent(
                        manager.getId(),
                        "Pending Issue Assignment",
                        message,
                        NotificationType.ISSUE_ASSIGNMENT_PENDING
                );
            }
        }

    }
}
