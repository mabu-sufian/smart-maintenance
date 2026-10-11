package com.example.maintenance.repositories;

import com.example.maintenance.entity.Enum.NotificationType;
import com.example.maintenance.entity.Notification;
import com.example.maintenance.entity.User;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long>{

    List<Notification> findByUserOrderByCreatedAtDesc(User user);
    boolean existsByUser_IdAndTypeAndMessage(Long user_id, NotificationType notificationType, String message);
}

