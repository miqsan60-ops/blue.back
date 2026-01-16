package com.analistas.blue.model.service;

import java.util.Optional;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;





@Service
public class EmailTurnoService {

    private final Optional<JavaMailSender> mailSender;

    public EmailTurnoService(Optional<JavaMailSender> mailSender) {
        this.mailSender = mailSender;
    }

    // ✅ ESTE MÉTODO ES EL QUE FALTABA
    public void enviarConfirmacion(
            String email,
            String nombre,
            String fecha,
            String servicio
    ) {

        // 👉 Si NO hay mail configurado, no hace nada
        if (mailSender.isEmpty()) {
            System.out.println(
                "[EMAIL DESACTIVADO] Turno confirmado para " + nombre
            );
            return;
        }

        try {
            MimeMessage mensaje = mailSender.get().createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("✅ Confirmación de turno - BlueMotors");

            helper.setText("""
                <h2>Turno confirmado</h2>
                <p>Hola <b>%s</b>,</p>
                <p>Tu turno fue confirmado:</p>
                <ul>
                    <li><b>Servicio:</b> %s</li>
                    <li><b>Fecha:</b> %s</li>
                </ul>
                <p>Gracias por confiar en BlueMotors 🚗</p>
            """.formatted(nombre, servicio, fecha), true);

            mailSender.get().send(mensaje);

        } catch (Exception e) {
            System.err.println(
                "Error enviando confirmación de turno: " + e.getMessage()
            );
        }
    }
}
