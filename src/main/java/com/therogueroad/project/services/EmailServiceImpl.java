package com.therogueroad.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendPasswordResetEmail(String recipient, String resetUrl){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(recipient);
        message.setSubject("Password Reset | The Rogue Road");
        message.setText("Click the link to reset your password: " + resetUrl);
        mailSender.send(message);
    }

    @Override
    public void sendEmailVerificationLink(String recipient, String verificationUrl){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject("Email Verification | The Rogue Road");
        message.setText("Click the link to verify your email address and complete your registration: " + verificationUrl);
        mailSender.send(message);
    }
}
