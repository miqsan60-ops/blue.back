package com.analistas.blue.web.controller;

import com.analistas.blue.model.domain.Compra;
import com.analistas.blue.model.domain.OrdenServicio;
import com.analistas.blue.model.domain.Turno;
import com.analistas.blue.model.domain.Usuario;
import com.analistas.blue.model.service.CompraService;
import com.analistas.blue.model.service.TurnoService;
import com.analistas.blue.model.service.UsuarioService;
import com.analistas.blue.web.security.JwtUtil;
import com.analistas.blue.model.service.OrdenServicioService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfil")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
public class PerfilController {

    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;
    private final CompraService compraService;
    private final TurnoService turnoService;
    private final OrdenServicioService ordenServicioService;

    public PerfilController(
            JwtUtil jwtUtil,
            UsuarioService usuarioService,
            CompraService compraService,
            TurnoService turnoService,
            OrdenServicioService ordenServicioService
    ) {
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
        this.compraService = compraService;
        this.turnoService = turnoService;
        this.ordenServicioService = ordenServicioService;
    }

    // ======================
    // USUARIO
    // ======================
    @GetMapping("/usuario")
    public ResponseEntity<Usuario> usuario(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.extraerUsername(token);

        Usuario usuario = usuarioService.buscarPorUsername(username);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usuario);
    }

    // ======================
    // COMPRAS
    // ======================
    @GetMapping("/compras")
    public List<Compra> compras(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.extraerUsername(token);

        Usuario usuario = usuarioService.buscarPorUsername(username);
        return compraService.comprasDelUsuarioLogueado(usuario.getId());
    }

    // ======================
    // ORDENES DE SERVICIO (TURNOS DEL PERFIL)
    // ======================
    @GetMapping("/ordenes-servicio")
    public ResponseEntity<List<OrdenServicio>> ordenesDelPerfil(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.extraerUsername(token);

        Usuario usuario = usuarioService.buscarPorUsername(username);

        if (usuario == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(
                ordenServicioService.ordenesDelUsuario(usuario.getId())
        );
    }
    @PutMapping("/usuario")
public ResponseEntity<Usuario> actualizarPerfil(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody Usuario datos
) {
    String token = authHeader.replace("Bearer ", "");
    String username = jwtUtil.extraerUsername(token);

    Usuario usuario = usuarioService.buscarPorUsername(username);
    if (usuario == null) {
        return ResponseEntity.status(401).build();
    }

    // ✔ campos editables
   usuario.setNombre(datos.getNombre());
usuario.setApellido(datos.getApellido());
usuario.setEmail(datos.getEmail());
usuario.setTelefono(datos.getTelefono()); // si lo agregás

 

    Usuario actualizado = usuarioService.guardar(usuario);
    return ResponseEntity.ok(actualizado);
}

}
