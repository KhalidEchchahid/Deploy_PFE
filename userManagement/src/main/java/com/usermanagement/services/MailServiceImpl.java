package com.usermanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;

    @Async
    public void sendMail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }
}
