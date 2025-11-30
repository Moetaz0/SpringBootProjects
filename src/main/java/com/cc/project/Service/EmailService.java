package com.cc.project.Service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendResetCode(String to, String code) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject("MedLink Password Reset Code");
            msg.setText("Your password reset code is: " + code
                    + "\n\nThis code expires in 10 minutes. If you did not request a password reset, you can ignore this email.");
            mailSender.send(msg);
        } catch (Exception e) {
            // For now we silently ignore; optionally integrate logging framework
            System.err.println("Failed to send reset code email: " + e.getMessage());
        }
    }
}
