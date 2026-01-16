package com.analistas.blue.model.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;

@Service
@ConditionalOnProperty(name = "mail.enabled", havingValue = "true")
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarFacturaConPdf(
            String para,
            String asunto,
            String htmlFactura,
            String nombreArchivo
    ) {

        try {
            ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlFactura, null);
            builder.toStream(pdfStream);
            builder.run();

            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(para);
            helper.setSubject(asunto);
            helper.setText(htmlFactura, true);
            helper.addAttachment(
                    nombreArchivo,
                    new ByteArrayResource(pdfStream.toByteArray())
            );

            mailSender.send(mensaje);

        } catch (Exception e) {
            System.err.println("Error al enviar factura por email: " + e.getMessage());
        }
    }

    public void enviarEmailSimple(String para, String asunto, String html) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(para);
            helper.setSubject(asunto);
            helper.setText(html, true);

            mailSender.send(mensaje);

        } catch (Exception e) {
            System.err.println("Error al enviar email: " + e.getMessage());
        }
    }
}
