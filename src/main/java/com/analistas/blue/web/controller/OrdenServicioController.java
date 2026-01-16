package com.analistas.blue.web.controller;

import com.analistas.blue.model.domain.EstadoServicio;
import com.analistas.blue.model.domain.OrdenServicio;
import com.analistas.blue.model.domain.Usuario;
import com.analistas.blue.model.service.EmailTurnoService;
import com.analistas.blue.model.service.OrdenServicioService;
import com.analistas.blue.model.service.UsuarioService;
import com.analistas.blue.web.security.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/ordenes-servicio")
@CrossOrigin(origins = {
        "http://localhost:5500",
        "http://127.0.0.1:5500"
})
public class OrdenServicioController {

    private final OrdenServicioService service;
    private final EmailTurnoService emailTurnoService;
    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    public OrdenServicioController(
            OrdenServicioService service,
            EmailTurnoService emailTurnoService,
            UsuarioService usuarioService,
            JwtUtil jwtUtil
    ) {
        this.service = service;
        this.emailTurnoService = emailTurnoService;
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    // ======================
    // LISTAR TODAS (ADMIN)
    // ======================
    @GetMapping
    public List<OrdenServicio> listar() {
        return service.listarTodas();
    }

    // ======================
    // CREAR ORDEN
    // - Cliente (con JWT) → guarda usuario
    // - Admin (sin JWT) → usuario null
    // ======================
    @PostMapping
    public OrdenServicio crear(
            @RequestBody OrdenServicio orden,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        // 📅 fecha / hora automáticas
        if (orden.getFecha() == null) {
            orden.setFecha(LocalDate.now());
        }

        if (orden.getHora() == null) {
            orden.setHora(LocalTime.now());
        }

        // 🟡 estado inicial
        if (orden.getEstado() == null) {
            orden.setEstado(EstadoServicio.PENDIENTE);
        }

        // 🔑 ASOCIAR USUARIO SI VIENE TOKEN
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");
            String username = jwtUtil.extraerUsername(token);

            Usuario usuario = usuarioService.buscarPorUsername(username);
            if (usuario != null) {
                orden.setUsuario(usuario);
            }
        }

        OrdenServicio guardada = service.crearOrden(orden);

        // 📧 enviar mail SOLO si hay email
        if (guardada.getEmail() != null && !guardada.getEmail().isBlank()) {
            emailTurnoService.enviarConfirmacion(
                    guardada.getEmail(),
                    guardada.getCliente(),
                    guardada.getServicio(),
                    guardada.getVehiculo()
            );
        }

        return guardada;
    }

    // ======================
    // ACTUALIZAR ORDEN (MODAL INFO)
    // ======================
    @PutMapping("/{id}")
    public OrdenServicio actualizar(
            @PathVariable Long id,
            @RequestBody OrdenServicio datos
    ) {
        return service.actualizarOrden(id, datos);
    }

    // ======================
    // CAMBIAR ESTADO
    // ======================
    @PutMapping("/{id}/estado")
    public void cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoServicio estado
    ) {
        service.cambiarEstado(id, estado);
    }

    // ======================
    // ELIMINAR
    // ======================
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
