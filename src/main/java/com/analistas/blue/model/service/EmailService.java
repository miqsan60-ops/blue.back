package com.analistas.blue.model.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
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
            // 📄 Generar PDF desde HTML
            ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlFactura, null);
            builder.toStream(pdfStream);
            builder.run();

            // 📧 Crear mail
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
            // No arroja excepción para que la compra no se interrumpa
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
