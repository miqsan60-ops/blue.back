package com.analistas.blue.model.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailTurnoService {

    private final JavaMailSender mailSender;

    public EmailTurnoService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // 🔹 Email cuando el cliente solicita turno
    public void enviarConfirmacion(String email,
                                   String nombre,
                                   String servicio,
                                   String modelo) {

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(email);
        mensaje.setSubject("Confirmación de Turno - BlueMotors");

        mensaje.setText(
                "Hola " + nombre + ",\n\n" +
                "Gracias por confiar en BlueMotors 🏍️\n\n" +
                "Hemos recibido tu solicitud de turno con los siguientes datos:\n\n" +
                "Servicio: " + servicio + "\n" +
                "Modelo: " + modelo + "\n\n" +
                "Tu turno quedó registrado y será revisado por nuestro equipo.\n" +
                "Nos contactaremos a la brevedad.\n\n" +
                "Saludos,\n" +
                "Equipo BlueMotors"
        );

        mailSender.send(mensaje);
    }
}
