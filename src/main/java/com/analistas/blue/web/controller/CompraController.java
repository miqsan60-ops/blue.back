package com.analistas.blue.web.controller;

import com.analistas.blue.model.repository.CompraRepository;
import com.analistas.blue.model.service.CompraService;
import com.analistas.blue.web.dto.CompraDTO;
import com.analistas.blue.web.dto.CompraTarjetaRequest;
import com.analistas.blue.web.dto.FacturaDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import com.analistas.blue.web.security.JwtUtil;
import com.analistas.blue.model.service.UsuarioService;
import com.analistas.blue.model.domain.Compra;
import com.analistas.blue.model.domain.Usuario;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/compra")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
public class CompraController {

    private final CompraService compraService;
    private final CompraRepository compraRepository;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    public CompraController(CompraService compraService,
                            CompraRepository compraRepository,
                            JwtUtil jwtUtil,
                            UsuarioService usuarioService) {
        this.compraService = compraService;
        this.compraRepository = compraRepository;
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
    }


    // ======================
    // COMPRA NORMAL (CON STOCK)
    // ======================
  @PostMapping
public ResponseEntity<?> comprar(
        @RequestHeader("Authorization") String auth,
        @RequestBody CompraDTO dto
) {

    if (dto.getCantidad() == null || dto.getCantidad() <= 0)
        return ResponseEntity.badRequest().body("Cantidad inválida");

    if (dto.getMetodoPago() == null || dto.getMetodoPago().isBlank())
        return ResponseEntity.badRequest().body("Método de pago requerido");

    try {
        String token = auth.replace("Bearer ", "");
        String username = jwtUtil.extraerUsername(token);
        Usuario usuario = usuarioService.buscarPorUsername(username);

        Compra compra = compraService.comprar(
                dto.getAccesorioId(),
                dto.getCantidad(),
                dto.getMetodoPago(),
                usuario.getId()
        );

        return ResponseEntity.ok(compra);

    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}


    // ======================
    // ORDEN MANUAL (SIN STOCK)
    // ======================
    @PostMapping("/manual")
public ResponseEntity<?> crearOrdenManual(@RequestBody CompraDTO dto) {

    if (dto.getUsuarioId() == null &&
        (dto.getCliente() == null || dto.getCliente().isBlank())) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", "Debe indicar cliente manual o usuario"));
    }

    compraService.crearOrdenManual(
            dto.getProducto(),
            dto.getCantidad(),
            dto.getTotal(),
            dto.getMetodoPago(),
            dto.getUsuarioId(),
            dto.getCliente(),
            dto.getConfirmado()
    );

    return ResponseEntity.ok(
            Map.of("mensaje", "Orden manual creada correctamente")
    );
}

    // ======================
    // ELIMINAR COMPRA
    // ======================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCompra(@PathVariable Long id) {

        if (!compraRepository.existsById(id))
            return ResponseEntity.notFound().build();

        compraService.eliminarCompra(id);
        return ResponseEntity.ok("Compra eliminada correctamente");
    }

    // ======================
    // LISTAR / FACTURACIÓN
    // ======================
    @GetMapping
    public List<FacturaDTO> listarCompras() {

        return compraRepository.findAll().stream().map(compra -> {

            FacturaDTO dto = new FacturaDTO();
            dto.setId(compra.getId());
            dto.setProducto(compra.getNombreProducto());
            dto.setCantidad(compra.getCantidad());
            dto.setTotal(compra.getTotal());
            dto.setMetodoPago(compra.getMetodoPago());
            dto.setFecha(compra.getFecha());

            // 🟢 CLIENTE REAL
            if (compra.getUsuario() != null) {
                dto.setCliente(
                        compra.getUsuario().getNombre() +
                        (compra.getUsuario().getApellido() != null
                                ? " " + compra.getUsuario().getApellido()
                                : "")
                );
            }
            // 🟢 CLIENTE MANUAL
            else if (compra.getClienteManual() != null &&
                     !compra.getClienteManual().isBlank()) {
                dto.setCliente(compra.getClienteManual());
            }
            // 🟢 CLIENTE WEB
            else {
                dto.setCliente("Cliente web");
            }

            // 🟢 CONFIRMADO
            dto.setConfirmado(
                    compra.getConfirmado() != null
                            ? compra.getConfirmado()
                            : "N"
            );

            return dto;
        }).toList();
    }

    // ======================
// COMPRA POR TRANSFERENCIA (PENDIENTE)
// ======================
@PostMapping("/transferencia")
public ResponseEntity<?> comprarPorTransferencia(
        @RequestHeader("Authorization") String auth,
        @RequestBody CompraDTO dto
) {

    // 🔴 VALIDACIONES CLARAS
    if (dto.getAccesorioId() == null)
        return ResponseEntity.badRequest().body("Accesorio requerido");

    if (dto.getCantidad() == null || dto.getCantidad() <= 0)
        return ResponseEntity.badRequest().body("Cantidad inválida");

    try {
        String token = auth.replace("Bearer ", "");
        String username = jwtUtil.extraerUsername(token);
        Usuario usuario = usuarioService.buscarPorUsername(username);

        Compra compra = compraService.comprarPorTransferencia(
                dto.getAccesorioId(),
                dto.getCantidad(),
                usuario.getId()
        );

        return ResponseEntity.ok(compra);

    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}


@GetMapping("/pendientes")
public List<FacturaDTO> comprasPendientes() {

    return compraRepository.findByConfirmado("N")
            .stream()
            .map(compra -> {

                FacturaDTO dto = new FacturaDTO();
                dto.setId(compra.getId());
                dto.setProducto(compra.getNombreProducto());
                dto.setCantidad(compra.getCantidad());
                dto.setTotal(compra.getTotal());
                dto.setMetodoPago(compra.getMetodoPago());
                dto.setFecha(compra.getFecha());

                if (compra.getUsuario() != null) {
                    dto.setCliente(compra.getUsuario().getNombre());
                } else {
                    dto.setCliente("Cliente web");
                }

                dto.setConfirmado("N");
                return dto;

            }).toList();
}
// ======================
// CONFIRMAR COMPRA PENDIENTE (ADMIN)
// ======================
@PostMapping("/{id}/confirmar")
public ResponseEntity<?> confirmarCompra(@PathVariable Long id) {
    try {
        Compra compra = compraService.confirmarCompra(id);
        return ResponseEntity.ok(compra);
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

@PostMapping("/{id}/comprobante")
public ResponseEntity<?> subirComprobante(
        @PathVariable Long id,
        @RequestParam("comprobante") MultipartFile comprobante
) {
    if (comprobante.isEmpty()) {
        return ResponseEntity.badRequest().body("Archivo vacío");
    }

    try {
        compraService.subirComprobante(id, comprobante);
        return ResponseEntity.ok(
                Map.of("mensaje", "Comprobante subido correctamente")
        );
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
@GetMapping("/{id}/comprobante")
public ResponseEntity<?> verComprobante(@PathVariable Long id) {

    Compra compra = compraRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

    String comprobante = compra.getComprobanteUrl();

    if (comprobante == null || comprobante.isBlank()) {
        return ResponseEntity.ok(
                Map.of("comprobante", "")
        );
    }

    return ResponseEntity.ok(
            Map.of("comprobante", comprobante)
    );
}

// ======================
// COMPRA PENDIENTE DEL USUARIO LOGUEADO
// ======================
@GetMapping("/pendiente")
public ResponseEntity<?> compraPendienteUsuario(
        @RequestHeader("Authorization") String auth
) {
    String token = auth.replace("Bearer ", "");
    String username = jwtUtil.extraerUsername(token);
    Usuario usuario = usuarioService.buscarPorUsername(username);

    List<Compra> pendientes =
            compraRepository.findByUsuario_IdAndConfirmado(usuario.getId(), "N");

    if (pendientes.isEmpty()) {
        return ResponseEntity.ok(Map.of("pendiente", false));
    }

    Compra compra = pendientes.get(0);

    FacturaDTO dto = new FacturaDTO();
    dto.setId(compra.getId());
    dto.setProducto(compra.getNombreProducto());
    dto.setCantidad(compra.getCantidad());
    dto.setTotal(compra.getTotal());
    dto.setMetodoPago(compra.getMetodoPago());
    dto.setFecha(compra.getFecha());
    dto.setConfirmado("N");

    return ResponseEntity.ok(
            Map.of(
                    "pendiente", true,
                    "compra", dto
            )
    );
}
@PostMapping("/{id}/aprobar")
public ResponseEntity<?> aprobarCompra(@PathVariable Long id) {
    compraService.aprobarCompraConEmail(id);
    return ResponseEntity.ok(Map.of("mensaje", "Compra aprobada"));
}
 @PostMapping("/{id}/rechazar")
public ResponseEntity<?> rechazarCompra(@PathVariable Long id) {
    compraService.rechazarCompraConEmail(id);
    return ResponseEntity.ok(Map.of("mensaje", "Compra rechazada"));
}

@PostMapping("/tarjeta")
public ResponseEntity<?> comprarTarjeta(
        @RequestBody CompraTarjetaRequest req,
        @AuthenticationPrincipal Usuario usuario
) {

    Compra compra = compraService.comprarConTarjeta(
            req.getAccesorioId(),
            req.getCantidad(),
            req.getTipoTarjeta(),
            usuario.getId()
    );

    return ResponseEntity.ok(compra);
}
@PostMapping("/tarjeta/confirmar/{id}")
public ResponseEntity<?> confirmarPagoTarjeta(@PathVariable Long id) {

    compraService.confirmarPagoTarjeta(id);
    return ResponseEntity.ok().build();
}

}