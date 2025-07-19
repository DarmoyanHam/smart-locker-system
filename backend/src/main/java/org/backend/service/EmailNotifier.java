package org.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EmailNotifier {

    @Autowired
    private JavaMailSender mailSender;

    private final String adminEmail = "admin@example.com";

    public void notifyExpiringLocker(Long lockerId, LocalDateTime lockedUntil) {
        String subject = "Уведомление: Шкафчик #" + lockerId + " скоро освободится";
        String text = "Шкафчик #" + lockerId + " истекает в " + lockedUntil +
                ". Пожалуйста, проверьте его состояние.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(adminEmail);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}

