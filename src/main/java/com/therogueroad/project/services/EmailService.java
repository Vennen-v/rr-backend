package com.therogueroad.project.services;

public interface EmailService {
    void sendPasswordResetEmail(String recipient, String resetUrl);

    void sendEmailVerificationLink(String recipient, String verificationUrl);
}
