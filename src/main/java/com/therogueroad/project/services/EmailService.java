package com.therogueroad.project.services;

public interface EmailService {
    void sendPasswordResetEmail(String recipient, String resetUrl);
}
