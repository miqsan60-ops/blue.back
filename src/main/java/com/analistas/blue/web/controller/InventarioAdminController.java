package com.analistas.blue.web.controller;

import com.analistas.blue.model.domain.Inventario;
import com.analistas.blue.model.service.InventarioService;
import com.analistas.blue.web.dto.InventarioDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/inventario")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
public class InventarioAdminController {

    private final InventarioService service;

    public InventarioAdminController(InventarioService service) {
        this.service = service;
    }

    // ======================
    // BUSCAR POR ACCESORIO (lo que usa el JS)
    // ======================
    @GetMapping("/accesorio/{accesorioId}")
    public ResponseEntity<?> buscarPorAccesorio(@PathVariable Long accesorioId) {
        Inventario inv = service.buscarPorAccesorio(accesorioId);

        if (inv == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(inv);
    }

    // ======================
    // LISTAR TODO
    // ======================
    @GetMapping
    public List<Inventario> listar() {
        return service.listar();
    }

    // ======================
    // CREAR
    // ======================
    @PostMapping
    public Inventario crear(@RequestBody InventarioDTO dto) {
        return service.crear(
                dto.getAccesorioId(),
                dto.getStock(),
                dto.getStockMinimo()
        );
    }

    // ======================
    // ACTUALIZAR
    // ======================
   @PutMapping("/{accesorioId}")
public Inventario actualizarPorAccesorio(
        @PathVariable Long accesorioId,
        @RequestBody InventarioDTO dto) {

    return service.actualizarPorAccesorio(
            accesorioId,
            dto.getStock(),
            dto.getStockMinimo()
    );
}

    // ======================
    // ELIMINAR
    // ======================
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
