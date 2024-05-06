package com.BookingManagementService.modeldemo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private JavaMailSender javaMailSender;
    @Autowired
    public MailService(JavaMailSender javaMailSender){
        this.javaMailSender=javaMailSender;
    }
    private static final Logger logger= LoggerFactory.getLogger(BookManagementService.class);

    public void sendEmail(String toEmail, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("Bookstore <y.kanakri310@gmail.com>");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        logger.info("Sending email done");

        javaMailSender.send(message);
    }
}
