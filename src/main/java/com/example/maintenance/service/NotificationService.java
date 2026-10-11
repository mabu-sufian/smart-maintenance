package com.example.maintenance.service;

import com.example.maintenance.dto.EmailRequestDTO;
import com.example.maintenance.dto.NotificationResponseDTO;
import com.example.maintenance.entity.Enum.NotificationType;
import com.example.maintenance.entity.Notification;
import com.example.maintenance.entity.User;
import com.example.maintenance.repositories.NotificationRepository;
import com.example.maintenance.repositories.UserRepository;
import org.aspectj.weaver.ast.Not;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    private final EmailService emailService;

    public NotificationService(UserRepository userRepository, NotificationRepository notificationRepository,
                               EmailService emailService)
    {
        this.userRepository=userRepository;
        this.notificationRepository=notificationRepository;
        this.emailService=emailService;
    }

    protected boolean isAdminOrManager()
    {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();

        return authentication.getAuthorities()
                .stream()
                .anyMatch(authority -> {
                    String role=authority.getAuthority();
                return role.equals("ROLE_ADMIN")
                        || role.equals("ROLE_MANAGER");
                });
    }

    public Notification createNotification(Long userId, String title, String message, NotificationType notificationType)
    {
        User user= userRepository.findById(userId)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found.."));
        Notification notification=new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(notificationType);
        notification.setReadStatus(false);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);

        if( user.getEmail() != null && !user.getEmail().isBlank())
        {
            EmailRequestDTO requestDTO=new EmailRequestDTO(
                    user.getEmail(), title, message
            );
            try{
                emailService.sendMail(requestDTO);
            }catch (Exception e)
            {
                System.out.println("Email sending failed for user"+ user.getEmail());
            }
        }

        return  notification;
    }

    public List<NotificationResponseDTO> getCurrentUserNotification(Long userId)
    {

        User user= userRepository.findById(userId)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long user_id=(Long) authentication.getPrincipal();

        if(! userId.equals(user_id) && !isAdminOrManager())
        {
         throw new RuntimeException("You are not able to see the notification");
        }

        List<Notification> notifications=notificationRepository.findByUserOrderByCreatedAtDesc(user);

        if(notifications.isEmpty())
        {
            throw new RuntimeException("Notification Not found");
        }
        return notifications
                .stream()
                .map(this::mappingToDTO)
                .toList();
}


public Notification markAsRead(Long notificationId, Long userId)
{
    Notification notification=notificationRepository.findById(notificationId)
            .orElseThrow(()->new RuntimeException("Notification Not Found.."));
    if(!notification.getUser().getId().equals(userId))
    {
        throw new AccessDeniedException("You cannot access this notification");
    }
    notification.setReadStatus(true);
    return notificationRepository.save(notification);
}


public void markAllAsRead(Long userId)
{
    User user=userRepository.findById(userId)
            .orElseThrow(()->new UsernameNotFoundException("User not found.."));

    List<Notification>notifications=notificationRepository.findByUserOrderByCreatedAtDesc(user);
    notifications.forEach(notify -> notify.setReadStatus(true));
notificationRepository.saveAll(notifications);

}

public void createSchedulingNotification_IfAbsent(Long userId, String title, String message, NotificationType notificationType)
{
    if(notificationRepository.existsByUser_IdAndTypeAndMessage(userId,notificationType,message))
    {
        return;
    }
    createNotification(userId,title,message,notificationType);
}



private NotificationResponseDTO mappingToDTO(Notification notification){
        return new NotificationResponseDTO(notification.getId(),
                notification.getTitle(), notification.getMessage(),
                notification.getType(), notification.isReadStatus(), notification.getCreatedAt());
    }
}
