package com.analistas.blue.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.analistas.blue.web.security.JwtUtil;

import com.analistas.blue.model.domain.ClienteGestion;
import com.analistas.blue.model.domain.Rol;
import com.analistas.blue.model.domain.Usuario;
import com.analistas.blue.model.service.UsuarioService;
import com.analistas.blue.web.dto.RegistroDTO;

import jakarta.transaction.Transactional;

import com.analistas.blue.model.service.ClienteGestionService;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})

public class UsuarioController {
    @Autowired
private JwtUtil jwtUtil;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ClienteGestionService clienteGestionService; //  

    // ======================
    // LOGIN
    // ======================
    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody Map<String, String> body) {

    String username = body.get("username");
    String password = body.get("password");

    Usuario usuario = usuarioService.login(username, password);
    if (usuario == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 🔐 GENERAR TOKEN
    String token = jwtUtil.generarToken(usuario.getUsername());

    // 📦 RESPUESTA COMPATIBLE
    return ResponseEntity.ok(
        new com.analistas.blue.web.dto.LoginResponseDTO(usuario, token)
    );
}


    // ======================
    // REGISTRAR CLIENTE
    // ======================
    @Transactional
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody RegistroDTO dto) {

        // validar duplicados
        if (usuarioService.existeUsername(dto.getUsername()) ||
            usuarioService.existeEmail(dto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Usuario o email ya registrado");
        }

        // ======================
        // CREAR USUARIO
        // ======================
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(Rol.CLIENTE);

        Usuario usuarioGuardado = usuarioService.guardar(usuario);

        // ======================
        // CREAR CLIENTE_GESTION
        // ======================
        ClienteGestion gestion = new ClienteGestion();
        gestion.setNombre(dto.getNombre());
        gestion.setApellido(dto.getApellido());
        gestion.setEmail(dto.getEmail());
        gestion.setDni(dto.getDni());
        gestion.setUsuario(usuarioGuardado); //  CLAVE

        clienteGestionService.guardar(gestion);

        return ResponseEntity.ok("Registro completo");
    }

    // ======================
    // LISTAR
    // ======================
    @GetMapping
    public ResponseEntity<?> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.buscarTodos());
    }

    // ======================
    // BUSCAR POR ID
    // ======================
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(usuario);
    }

    // ======================
    // ELIMINAR
    // ======================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}