package com.analistas.blue.web.controller;

import com.analistas.blue.model.domain.ClienteGestion;
import com.analistas.blue.model.service.ClienteGestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gestion-clientes")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
public class ClienteGestionController {

    private final ClienteGestionService service;

    public ClienteGestionController(ClienteGestionService service) {
        this.service = service;
    }

    // LISTAR
    @GetMapping
    public List<ClienteGestion> listar() {
        return service.listar();
    }

    // CREAR
    @PostMapping
    public ClienteGestion crear(@RequestBody ClienteGestion c) {
        return service.guardar(c);
    }

    // EDITAR
    @PutMapping("/{id}")
    public ClienteGestion editar(@PathVariable Long id,
                                 @RequestBody ClienteGestion c) {
        return service.actualizar(id, c);
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
