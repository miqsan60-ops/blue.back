package com.analistas.blue.model.service;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

@Service
public class FacturaPdfService {

    public byte[] generarPdf(String html) {

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(output);
            builder.run();

            return output.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF de la factura", e);
        }
    }
}
