package com.analistas.blue.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.analistas.blue.model.domain.Cliente;
import com.analistas.blue.model.domain.Usuario;
import com.analistas.blue.model.domain.Rol;
import com.analistas.blue.model.domain.ClienteGestion;
import com.analistas.blue.model.service.ClienteService;
import com.analistas.blue.model.service.UsuarioService;

import jakarta.transaction.Transactional;

import com.analistas.blue.model.service.ClienteGestionService;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private UsuarioService usuarioService;

    //  NUEVO (NO ROMPE NADA)
    @Autowired
    private ClienteGestionService clienteGestionService;

 @PostMapping("/registrar")
@Transactional
public ResponseEntity<?> registrarCliente(@RequestBody Cliente cliente) {

    System.out.println("👉 ENTRÓ AL REGISTRO");

    if (usuarioService.existeEmail(cliente.getEmail())) {
        return ResponseEntity.badRequest().body("Email ya registrado");
    }

    Usuario usuario = new Usuario();
    usuario.setNombre(cliente.getNombre());
    usuario.setApellido(cliente.getApellido());
    usuario.setEmail(cliente.getEmail());
    usuario.setUsername(cliente.getEmail());
    usuario.setPassword(cliente.getPassword());
    usuario.setRol(Rol.CLIENTE);

    usuarioService.guardar(usuario);
    System.out.println("✅ Usuario guardado");

    cliente.setUsuario(usuario);
    Cliente nuevoCliente = clienteService.guardar(cliente);
    System.out.println("✅ Cliente guardado");

    ClienteGestion gestion = new ClienteGestion();
    gestion.setNombre(cliente.getNombre());
    gestion.setApellido(cliente.getApellido());
    gestion.setEmail(cliente.getEmail());

    System.out.println("🟡 Antes de guardar ClienteGestion");
    clienteGestionService.guardar(gestion);
    System.out.println("🟢 ClienteGestion guardado");

    return ResponseEntity.ok(nuevoCliente);
}

}