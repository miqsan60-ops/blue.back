package com.analistas.blue.model.service;

import java.util.Optional;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;




@Service
public class EmailTurnoService {

    private final Optional<JavaMailSender> mailSender;

    public EmailTurnoService(Optional<JavaMailSender> mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarTurno() {
        mailSender.ifPresent(sender -> {
            // enviar mail real
        });
    }
}