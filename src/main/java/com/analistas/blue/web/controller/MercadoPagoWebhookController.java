package com.analistas.blue.web.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.analistas.blue.model.domain.Compra;
import com.analistas.blue.model.domain.Pago;
import com.analistas.blue.model.repository.CompraRepository;
import com.analistas.blue.model.repository.PagoRepository;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mercadopago")
@RequiredArgsConstructor
public class MercadoPagoWebhookController {

    private final PagoRepository pagoRepository;
    private final CompraRepository compraRepository;

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(@RequestBody Map<String, Object> payload) {

        try {
            Map<String, Object> data = (Map<String, Object>) payload.get("data");
            Long paymentId = Long.valueOf(data.get("id").toString());

            PaymentClient client = new PaymentClient();
            Payment payment = client.get(paymentId);

            // 🔹 Evitar duplicados
            if (pagoRepository.existsByPaymentId(payment.getId().toString())) {
                System.out.println("⚠️ Pago duplicado ignorado");
                return ResponseEntity.ok().build();
            }

            // 🔹 Obtener compra
            String description = payment.getDescription();
            Long compraId = Long.valueOf(description.replaceAll("\\D+", ""));

            Compra compra = compraRepository.findById(compraId)
                    .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

            // 🔹 Guardar pago
            Pago pago = new Pago();
            pago.setPaymentId(payment.getId().toString());
            pago.setStatus(payment.getStatus()); // approved / pending / rejected
            pago.setAmount(payment.getTransactionAmount().doubleValue());
            pago.setMetodoPago(payment.getPaymentMethodId());
            pago.setFecha(LocalDateTime.now());
            pago.setCompra(compra);

            pagoRepository.save(pago);

            // 🔹 ACTUALIZAR COMPRA (CLAVE)
            String status = payment.getStatus();

            if ("approved".equals(status)) {
                compra.setConfirmado("S");
                compra.setPaymentStatus("approved");
            }
            else if ("rejected".equals(status) || "cancelled".equals(status)) {
                compra.setConfirmado("N");
                compra.setPaymentStatus("rejected");
            }
            else {
                // pending / in_process
                compra.setConfirmado("N");
                compra.setPaymentStatus("pending");
            }

            compraRepository.save(compra);

            System.out.println("✅ Pago registrado: " + status);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
