package com.analistas.blue.model.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.analistas.blue.model.domain.Accesorio;
import com.analistas.blue.model.domain.Compra;
import com.analistas.blue.model.domain.Inventario;
import com.analistas.blue.model.domain.Usuario;
import com.analistas.blue.model.repository.CompraRepository;
import com.analistas.blue.model.repository.InventarioRepository;
import com.analistas.blue.web.dto.MercadoPagoRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MercadoPagoService {

    private final CompraRepository compraRepo;
    private final InventarioRepository inventarioRepo;

    @Transactional
public Map<String, Object> procesarPago(MercadoPagoRequest req, Usuario usuario) throws Exception {

    // 1️⃣ Buscar inventario por accesorio
    Inventario inv = inventarioRepo.findByAccesorioId(req.getAccesorioId())
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

    if (inv.getStock() < req.getCantidad()) {
        throw new RuntimeException("Stock insuficiente");
    }

    Accesorio acc = inv.getAccesorio();

    // 2️⃣ Crear compra
    Compra compra = new Compra();
    compra.setAccesorioId(acc.getId());
    compra.setNombreProducto(acc.getNombre());
    compra.setImagenProducto(acc.getImagen());
    compra.setPrecioUnitario(acc.getPrecio());
    compra.setCantidad(req.getCantidad());

    BigDecimal total = acc.getPrecio()
            .multiply(BigDecimal.valueOf(req.getCantidad()));

    compra.setTotal(total);
    compra.setMetodoPago("TARJETA");
    compra.setConfirmado("N");
    compra.setFecha(LocalDateTime.now()); // 🔥 ESTE ERA EL ERROR
    compra.setUsuario(usuario);

    compraRepo.save(compra);

    // 3️⃣ Crear pago Mercado Pago
    PaymentClient paymentClient = new PaymentClient();

    PaymentCreateRequest paymentRequest =
            PaymentCreateRequest.builder()
                    .transactionAmount(total)
                    .token(req.getToken())
                    .description("Compra BlueMotors #" + compra.getId())
                    .installments(req.getInstallments())
                    .paymentMethodId(req.getPaymentMethodId())
                    .issuerId(
                            req.getIssuerId() != null
                                    ? String.valueOf(req.getIssuerId())
                                    : null
                    )
                    .payer(
                            PaymentPayerRequest.builder()
                                    .email(usuario.getEmail())
                                    .build()
                    )
                    .build();

    var payment = paymentClient.create(paymentRequest);

// 🔐 Guardar info del pago
compra.setPaymentId(payment.getId());
compra.setPaymentStatus(payment.getStatus());
compraRepo.save(compra);

// ✅ Si aprobado → confirmar
if ("approved".equals(payment.getStatus())) {
    inv.setStock(inv.getStock() - req.getCantidad());
    inventarioRepo.save(inv);

    compra.setConfirmado("S");
    compraRepo.save(compra);
}


    // 4️⃣ Confirmar si está aprobado
    if ("approved".equals(payment.getStatus())) {
        inv.setStock(inv.getStock() - req.getCantidad());
        inventarioRepo.save(inv);

        compra.setConfirmado("S");
        compraRepo.save(compra);
    }

    // 5️⃣ Respuesta al frontend
   Map<String, Object> response = new HashMap<>();
response.put("status", payment.getStatus());
response.put("paymentId", payment.getId());
response.put("compraId", compra.getId());
return response;

}


    private void confirmarCompra(Compra compra, Inventario inv) {

        inv.setStock(inv.getStock() - compra.getCantidad());
        inventarioRepo.save(inv);

        compra.setConfirmado("S");
        compraRepo.save(compra);
    }
}
