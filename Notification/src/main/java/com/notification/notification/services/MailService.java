package com.notification.notification.services;

public interface MailService {
    void sendEmail(String to , String subject , String message);
}
