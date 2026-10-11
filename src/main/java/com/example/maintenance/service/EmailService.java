package com.example.maintenance.service;

import com.example.maintenance.dto.EmailRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {


    @Value("${spring.mail.username}")
    private String adminEmail;
    private final JavaMailSender javaMailSender;

    public  EmailService(JavaMailSender javaMailSender)
    {
        this.javaMailSender=javaMailSender;
    }

    public void sendMail(EmailRequestDTO requestDTO)
    {
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(adminEmail);
        message.setTo(requestDTO.getTo());
        message.setSubject(requestDTO.getSubject());
        message.setText(requestDTO.getBody());

        try{
            javaMailSender.send(message);
        }
        catch (Exception e)
        {
            System.out.println("Message Sending failed: "+ e.getMessage() );
        }

    }


}
