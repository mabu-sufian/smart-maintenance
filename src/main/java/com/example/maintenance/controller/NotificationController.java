package com.example.maintenance.controller;

import com.example.maintenance.dto.NotificationResponseDTO;
import com.example.maintenance.entity.Notification;
import com.example.maintenance.service.NotificationService;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Security;
import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;
    public NotificationController(NotificationService notificationService)
    {
        this.notificationService=notificationService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>>getCurrentUserNotification( @PathVariable Long userId)
    {
        return ResponseEntity.ok()
                .body(notificationService.getCurrentUserNotification(userId));
    }

    @PatchMapping("/{notifyId}/read")
    public ResponseEntity<Void>markAsRead(@PathVariable long notifyId)
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Long userId=(Long)authentication.getPrincipal();
        notificationService.markAsRead(notifyId,userId);
        return ResponseEntity.noContent().build();

    }

    @PatchMapping("/read_all")
    public ResponseEntity<Void> markAllAsRead()
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Long userId=(Long)authentication.getPrincipal();
        notificationService.markAllAsRead(userId);
        return ResponseEntity.noContent().build();
    }

}
