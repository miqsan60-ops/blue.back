package com.analistas.blue.web.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.analistas.blue.model.domain.Orden;
import com.analistas.blue.model.domain.Usuario;
import com.analistas.blue.model.service.OrdenService;
import com.analistas.blue.model.service.UsuarioService;
import com.analistas.blue.web.security.JwtUtil;

@RestController
@RequestMapping("/api/ordenes")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
public class OrdenController {

    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;
    private final OrdenService ordenService;

    public OrdenController(
            JwtUtil jwtUtil,
            UsuarioService usuarioService,
            OrdenService ordenService
    ) {
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
        this.ordenService = ordenService;
    }

    // ======================
    // CREAR ORDEN (CLIENTE)
    // ======================
    @PostMapping
    public Map<String, Object> crearOrden(
            @RequestHeader("Authorization") String auth,
            @RequestBody Map<String, Object> body
    ) {
        String token = auth.replace("Bearer ", "");
        String username = jwtUtil.extraerUsername(token);

        Usuario usuario = usuarioService.buscarPorUsername(username);

        Double total = Double.valueOf(body.get("total").toString());
        String metodoPago = body.get("metodoPago").toString();

        Orden orden = ordenService.crearOrden(usuario, total, metodoPago);

        // datos bancarios
        return Map.of(
                "ordenId", orden.getId(),
                "total", orden.getTotal(),
                "cbu", "0000003100001234567890",
                "alias", "BLUE.MOTORS.OK"
        );
    }

    // ======================
    // LISTAR MIS ORDENES
    // ======================
    @GetMapping("/mis-ordenes")
    public List<Orden> misOrdenes(
            @RequestHeader("Authorization") String auth
    ) {
        String token = auth.replace("Bearer ", "");
        String username = jwtUtil.extraerUsername(token);

        Usuario usuario = usuarioService.buscarPorUsername(username);
        return ordenService.ordenesUsuario(usuario.getId().longValue());
    }
}
