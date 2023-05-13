package com.usermanagement.services;
import org.springframework.mail.SimpleMailMessage;

public interface MailService {
    void sendMail(SimpleMailMessage email);
}
