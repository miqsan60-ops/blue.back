package com.analistas.blue.web.controller;

import com.analistas.blue.model.domain.Accesorio;
import com.analistas.blue.model.service.AccesorioService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/accesorios")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
public class AccesorioAdminController {

    private final AccesorioService service;

    public AccesorioAdminController(AccesorioService service) {
        this.service = service;
    }

    // =========================
    // LISTAR TODOS
    // =========================
    @GetMapping
    public List<Accesorio> listar() {
        return service.listarTodos();
    }

    // =========================
    // OBTENER POR ID
    // =========================
    @GetMapping("/{id}")
    public Accesorio obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    // =========================
    // BUSCAR
    // =========================
    @GetMapping("/buscar")
    public List<Accesorio> buscar(@RequestParam String q) {
        return service.buscarPorNombre(q);
    }

    // =========================
    // CREAR
    // =========================
    @PostMapping
    public Accesorio crear(@RequestBody Accesorio accesorio) {
        return service.crear(accesorio);
    }

    // =========================
    // EDITAR
    // =========================
    @PutMapping("/{id}")
    public Accesorio editar(
            @PathVariable Long id,
            @RequestBody Accesorio accesorio) {
        return service.actualizar(id, accesorio);
    }

    // =========================
    // BORRADO LÓGICO
    // =========================
    @PutMapping("/{id}/desactivar")
    public void desactivar(@PathVariable Long id) {
        service.desactivar(id);
    }

    // =========================
    // BORRADO FÍSICO + INVENTARIO
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}