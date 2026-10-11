package com.example.maintenance.example;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//@Component
//public class TestScheduling {
//
//    @Scheduled(fixedDelay = 10000)
//    public void scheduleTest()
//    {
//        DateTimeFormatter formtter=DateTimeFormatter.ofPattern("y-M-d HH:mm:ss");
//        System.out.println("Scheduling working: "+ LocalDateTime.now().format(formtter));
//    }
//
//}
