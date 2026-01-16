package com.analistas.blue.model.service;

import com.analistas.blue.model.domain.Compra;
import com.analistas.blue.model.domain.Inventario;
import com.analistas.blue.model.domain.Usuario;
import com.analistas.blue.model.repository.CompraRepository;
import com.analistas.blue.model.repository.InventarioRepository;
import com.analistas.blue.model.repository.UsuarioRepository;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import com.analistas.blue.model.repository.AccesorioRepository;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompraService {

    private final InventarioRepository inventarioRepo;
    private final CompraRepository compraRepo;
    private final UsuarioRepository usuarioRepo;
  

private final Optional<EmailService> emailService;

private final AccesorioRepository accesorioRepo;

public CompraService(
        InventarioRepository inventarioRepo,
        CompraRepository compraRepo,
        UsuarioRepository usuarioRepo,
        Optional<EmailService> emailService,
        AccesorioRepository accesorioRepo
) {
    this.inventarioRepo = inventarioRepo;
    this.compraRepo = compraRepo;
    this.usuarioRepo = usuarioRepo;
    this.emailService = emailService;
    this.accesorioRepo = accesorioRepo;
}

    

    // ======================
    // COMPRA NORMAL (CON STOCK)
    // ======================

   @Transactional
public Compra comprar(Long accesorioId,
                      int cantidad,
                      String metodoPago,
                      Integer usuarioId) {

    if (usuarioId == null)
        throw new RuntimeException("Usuario logueado requerido");

    Inventario inv = inventarioRepo.findByAccesorioId(accesorioId)
            .orElseThrow(() -> new RuntimeException("No hay inventario"));

    if (inv.getStock() < cantidad)
        throw new RuntimeException("Stock insuficiente");

    inv.setStock(inv.getStock() - cantidad);
    inventarioRepo.save(inv);

    Usuario usuario = usuarioRepo.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    BigDecimal precio = inv.getAccesorio().getPrecio();
    BigDecimal total = precio.multiply(BigDecimal.valueOf(cantidad));

    Compra compra = new Compra();
    compra.setAccesorioId(accesorioId);
    compra.setNombreProducto(inv.getAccesorio().getNombre());
    compra.setImagenProducto(inv.getAccesorio().getImagen());
    compra.setCantidad(cantidad);
    compra.setPrecioUnitario(precio);
    compra.setTotal(total);
    compra.setMetodoPago(metodoPago);
    compra.setFecha(LocalDateTime.now());
    compra.setUsuario(usuario);
    compra.setConfirmado("S");

    Compra compraGuardada = compraRepo.save(compra);

    // 📩 email (NO rompe la compra)
    enviarEmailCompra(usuario, compraGuardada);

    return compraGuardada;
}

    // ======================
    // ELIMINAR
    // ======================
    @Transactional
    public void eliminarCompra(Long id) {
        compraRepo.deleteById(id);
    }

    // ======================
    // EMAIL COMPRA
    // ======================
  private void enviarEmailCompra(Usuario usuario, Compra compra) {
    if (usuario.getEmail() == null || usuario.getEmail().isBlank()) return;

    try {
        String htmlFactura = FacturaAdminHtmlBuilder.generarHtml(compra);
        emailService.ifPresent(email ->
        email.enviarFacturaConPdf(
                usuario.getEmail(),
                "Factura de tu compra - BlueMotors",
                htmlFactura,
                "Factura_BlueMotors_" + compra.getId() + ".pdf"
        )
);

    } catch (Exception e) {
        // Solo logueamos el error, no bloqueamos la compra
        System.err.println("Error al enviar factura por email: " + e.getMessage());
    }
}
// ======================
    // COMPRAS DEL USUARIO LOGUEADO
    // ======================
    public List<Compra> comprasDelUsuarioLogueado(Integer usuarioId) {
        if (usuarioId == null) {
            throw new RuntimeException("Usuario no logueado");
        }
        return compraRepo.findByUsuarioIdOrderByFechaDesc(usuarioId);
    }
    // ======================
// COMPRAS DEL USUARIO
// ======================
public List<Compra> comprasDelUsuario(Integer usuarioId) {
    List<Compra> compras = compraRepo.findByUsuario_Id(usuarioId);

    for (Compra compra : compras) {

        // ⚠️ Si la compra NO tiene imagen
        if (compra.getImagenProducto() == null &&
            compra.getAccesorioId() != null) {

            accesorioRepo.findById(compra.getAccesorioId())
                .ifPresent(accesorio -> {
                    compra.setImagenProducto(accesorio.getImagen());
                });
        }
    }

    return compras;
}
@Transactional
public Compra comprarPorTransferencia(Long accesorioId,
                                      int cantidad,
                                      Integer usuarioId) {

    if (usuarioId == null)
        throw new RuntimeException("Usuario requerido");

    Usuario usuario = usuarioRepo.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    Inventario inv = inventarioRepo.findByAccesorioId(accesorioId)
            .orElseThrow(() -> new RuntimeException("No hay inventario"));

    // ✅ VALIDACIÓN CLAVE
    if (inv.getStock() < cantidad)
        throw new RuntimeException("Stock insuficiente");

    BigDecimal precio = inv.getAccesorio().getPrecio();
    BigDecimal total = precio.multiply(BigDecimal.valueOf(cantidad));

    Compra compra = new Compra();
    compra.setAccesorioId(accesorioId);
    compra.setNombreProducto(inv.getAccesorio().getNombre());
    compra.setImagenProducto(inv.getAccesorio().getImagen());
    compra.setCantidad(cantidad);
    compra.setPrecioUnitario(precio);
    compra.setTotal(total);
   compra.setMetodoPago("TRANSFERENCIA");

    compra.setFecha(LocalDateTime.now());
    compra.setUsuario(usuario);
    compra.setConfirmado("N");

    return compraRepo.save(compra);
}


// ======================
// ORDEN MANUAL (SIN STOCK / ADMIN)
// ======================
@Transactional
public void crearOrdenManual(
        String nombreProducto,
        Integer cantidad,
        BigDecimal total,
        String metodoPago,
        Integer usuarioId,
        String cliente,
        String confirmado
) {

    if (nombreProducto == null || nombreProducto.isBlank())
        throw new RuntimeException("Producto requerido");

    if (cantidad == null || cantidad <= 0)
        throw new RuntimeException("Cantidad inválida");

    if (total == null || total.compareTo(BigDecimal.ZERO) <= 0)
        throw new RuntimeException("Total inválido");

    Compra compra = new Compra();
    compra.setNombreProducto(nombreProducto);
    compra.setCantidad(cantidad);
    compra.setPrecioUnitario(
            total.divide(BigDecimal.valueOf(cantidad))
    );
    compra.setTotal(total);
    compra.setMetodoPago(metodoPago);
    compra.setFecha(LocalDateTime.now());

    if (usuarioId != null) {
        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        compra.setUsuario(usuario);
    } else {
        compra.setClienteManual(cliente);
    }

    compra.setConfirmado(
            (confirmado != null && !confirmado.isBlank()) ? confirmado : "N"
    );

    compraRepo.save(compra);
}

@Transactional
public Compra confirmarCompra(Long compraId) {

    Compra compra = compraRepo.findById(compraId)
            .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

    if ("S".equals(compra.getConfirmado()))
        throw new RuntimeException("La compra ya está confirmada");

    // 🔴 VALIDACIÓN CLAVE
    if (compra.getComprobanteUrl() == null || compra.getComprobanteUrl().isBlank()) {
        throw new RuntimeException("No se puede confirmar la compra sin comprobante");
    }

    Inventario inv = inventarioRepo.findByAccesorioId(compra.getAccesorioId())
            .orElseThrow(() -> new RuntimeException("No hay inventario"));

    if (inv.getStock() < compra.getCantidad())
        throw new RuntimeException("Stock insuficiente para confirmar");

    inv.setStock(inv.getStock() - compra.getCantidad());
    inventarioRepo.save(inv);

    compra.setConfirmado("S");

    return compraRepo.save(compra);
}


@Transactional
public void subirComprobante(Long compraId, MultipartFile archivo) {

    Compra compra = compraRepo.findById(compraId)
            .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

    if (archivo == null || archivo.isEmpty()) {
        throw new RuntimeException("Archivo vacío");
    }

    try {
        String original = archivo.getOriginalFilename();
        String limpio = original != null
                ? original.replaceAll("[^a-zA-Z0-9\\.\\-]", "_")
                : "archivo";

        String nombreArchivo = "comprobante_" + compraId + "_" + limpio;

        Path ruta = Paths.get(System.getProperty("user.dir"))
                .resolve("uploads")
                .resolve("comprobantes")
                .resolve(nombreArchivo);

        Files.createDirectories(ruta.getParent());
        Files.write(ruta, archivo.getBytes());

        compra.setComprobanteUrl("/uploads/comprobantes/" + nombreArchivo);
        compra.setConfirmado("N");
        compraRepo.save(compra);

        // 📧 EMAIL PAGO PENDIENTE
        Usuario usuario = compra.getUsuario();
        if (usuario != null && usuario.getEmail() != null) {

            String html = """
            <div style="font-family:Arial,Helvetica,sans-serif;background:#f5f7fa;padding:20px">
              <div style="max-width:600px;margin:auto;background:#ffffff;
                          border-radius:12px;overflow:hidden;
                          box-shadow:0 8px 24px rgba(0,0,0,0.08)">

                <div style="background:#0f3c68;color:#ffffff;padding:18px 22px">
                  <h2 style="margin:0;font-size:20px">⏳ Pago pendiente de verificación</h2>
                </div>

                <div style="padding:24px;color:#333;font-size:14px;line-height:1.6">
                  <p>Hola <b>%s</b>,</p>

                  <p>
                    Recibimos correctamente tu <b>comprobante de pago por transferencia</b>.
                    Nuestro equipo lo revisará a la brevedad.
                  </p>

                  <div style="margin:20px 0;padding:16px;
                              background:#f1f5f9;border-radius:10px">
                    <p style="margin:0 0 8px 0;font-weight:bold;color:#0f3c68">
                      🧾 Detalle de la compra
                    </p>
                    <p><b>Compra Nº:</b> %d</p>
                    <p><b>Producto:</b> %s</p>
                    <p><b>Total:</b> $ %,.2f</p>
                  </div>

                  <p>
                    Te notificaremos por email una vez que el pago sea
                    <b>aprobado o rechazado</b>.
                  </p>

                  <p>Gracias por tu paciencia 🙌</p>

                  <p style="margin-top:28px"><b>Equipo BlueMotors</b></p>
                </div>

                <div style="background:#f1f5f9;text-align:center;
                            padding:12px;font-size:12px;color:#666">
                  © BlueMotors · Atención al cliente
                </div>

              </div>
            </div>
            """.formatted(
                usuario.getNombre(),
                compra.getId(),
                compra.getNombreProducto(),
                compra.getTotal()
            );
String htmlFactura = FacturaAdminHtmlBuilder.generarHtml(compra);
          emailService.ifPresent(email ->
        email.enviarFacturaConPdf(
                usuario.getEmail(),
                "Factura de tu compra - BlueMotors",
                htmlFactura,
                "Factura_BlueMotors_" + compra.getId() + ".pdf"
        )
);

        }

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(
                "Error al guardar el comprobante: " + e.getMessage()
        );
    }
}

@Transactional
public void aprobarCompraConEmail(Long id) {

    Compra compra = compraRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

    if (compra.getComprobanteUrl() == null || compra.getComprobanteUrl().isBlank())
        throw new RuntimeException("No hay comprobante cargado");

    confirmarCompra(id); // descuenta stock y confirma

    Usuario usuario = compra.getUsuario();
    if (usuario == null || usuario.getEmail() == null) return;

    String htmlFactura = FacturaAdminHtmlBuilder.generarHtml(compra);

   emailService.ifPresent(email ->
        email.enviarFacturaConPdf(
                usuario.getEmail(),
                "Factura de tu compra - BlueMotors",
                htmlFactura,
                "Factura_BlueMotors_" + compra.getId() + ".pdf"
        )
);

}



@Transactional
public void rechazarCompraConEmail(Long id) {

    Compra compra = compraRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

    compra.setConfirmado("R");
    compraRepo.save(compra);

    Usuario usuario = compra.getUsuario();
    if (usuario != null && usuario.getEmail() != null) {
        String htmlFactura = FacturaAdminHtmlBuilder.generarHtml(compra);
    emailService.ifPresent(email ->
        email.enviarFacturaConPdf(
                usuario.getEmail(),
                "Factura de tu compra - BlueMotors",
                htmlFactura,
                "Factura_BlueMotors_" + compra.getId() + ".pdf"
        )
);

    }
}
@Transactional
public Compra comprarConTarjeta(
        Long accesorioId,
        int cantidad,
        String tipoTarjeta,
        Integer usuarioId
) {

    if (usuarioId == null)
        throw new RuntimeException("Usuario requerido");

    if (accesorioId == null)
        throw new RuntimeException("Accesorio requerido");

    if (cantidad <= 0)
        throw new RuntimeException("Cantidad inválida");

    if (tipoTarjeta == null ||
        (!tipoTarjeta.equalsIgnoreCase("CREDITO")
         && !tipoTarjeta.equalsIgnoreCase("DEBITO")))
        throw new RuntimeException("Tipo de tarjeta inválido");

    Usuario usuario = usuarioRepo.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    Inventario inv = inventarioRepo.findByAccesorioId(accesorioId)
            .orElseThrow(() -> new RuntimeException("No hay inventario"));

    if (inv.getStock() < cantidad)
        throw new RuntimeException("Stock insuficiente");

    BigDecimal precio = inv.getAccesorio().getPrecio();
    BigDecimal total = precio.multiply(BigDecimal.valueOf(cantidad));

    String metodoPago = tipoTarjeta.equalsIgnoreCase("CREDITO")
            ? "TARJETA_CREDITO"
            : "TARJETA_DEBITO";

    Compra compra = new Compra();
    compra.setAccesorioId(accesorioId);
    compra.setNombreProducto(inv.getAccesorio().getNombre());
    compra.setImagenProducto(inv.getAccesorio().getImagen());
    compra.setCantidad(cantidad);
    compra.setPrecioUnitario(precio);
    compra.setTotal(total);
    compra.setMetodoPago(metodoPago);
    compra.setFecha(LocalDateTime.now());
    compra.setUsuario(usuario);
    compra.setConfirmado("N");

    return compraRepo.save(compra);
}

@Transactional
public void confirmarPagoTarjeta(Long compraId) {

    Compra compra = compraRepo.findById(compraId)
            .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

    if ("S".equalsIgnoreCase(compra.getConfirmado()))
        throw new RuntimeException("La compra ya está confirmada");

    if (compra.getMetodoPago() == null ||
        !compra.getMetodoPago().startsWith("TARJETA"))
        throw new RuntimeException("La compra no es con tarjeta");

    Inventario inv = inventarioRepo.findByAccesorioId(compra.getAccesorioId())
            .orElseThrow(() -> new RuntimeException("No hay inventario"));

    if (inv.getStock() < compra.getCantidad())
        throw new RuntimeException("Stock insuficiente");

    // 🔻 descontar stock
    inv.setStock(inv.getStock() - compra.getCantidad());
    inventarioRepo.save(inv);

    // ✅ confirmar compra
    compra.setConfirmado("S");
    compraRepo.save(compra);

    // 📧 enviar factura
    Usuario usuario = compra.getUsuario();
    if (usuario != null && usuario.getEmail() != null) {

        String htmlFactura = FacturaAdminHtmlBuilder.generarHtml(compra);

        emailService.ifPresent(email ->
        email.enviarFacturaConPdf(
                usuario.getEmail(),
                "Factura de tu compra - BlueMotors",
                htmlFactura,
                "Factura_BlueMotors_" + compra.getId() + ".pdf"
        )
);

    }
}


}