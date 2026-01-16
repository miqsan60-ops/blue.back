package com.analistas.blue.model.service;

import com.analistas.blue.model.domain.Accesorio;
import com.analistas.blue.model.repository.AccesorioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccesorioService {

    private final AccesorioRepository repo;

    public AccesorioService(AccesorioRepository repo) {
        this.repo = repo;
    }

    // =========================
    // LISTAR
    // =========================
    public List<Accesorio> listarTodos() {
        return repo.findAll();
    }

    // =========================
    // OBTENER POR ID
    // =========================
    public Accesorio obtenerPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Accesorio no encontrado"));
    }

    // =========================
    // CREAR
    // =========================
    public Accesorio crear(Accesorio accesorio) {
        accesorio.setId(null);

        if (accesorio.getActivo() == null) {
            accesorio.setActivo(true);
        }

        return repo.save(accesorio);
    }

    // =========================
    // ACTUALIZAR
    // =========================
    public Accesorio actualizar(Long id, Accesorio datos) {
        Accesorio existente = obtenerPorId(id);

        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        existente.setCategoria(datos.getCategoria());
        existente.setPrecio(datos.getPrecio());
        existente.setImagen(datos.getImagen());
        existente.setActivo(datos.getActivo());

        return repo.save(existente);
    }

    // =========================
    // BORRADO LÓGICO
    // =========================
    public void desactivar(Long id) {
        Accesorio acc = obtenerPorId(id);
        acc.setActivo(false);
        repo.save(acc);
    }

    // =========================
    // BORRADO FÍSICO
    // =========================
    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    // =========================
    // BUSCAR
    // =========================
    public List<Accesorio> buscarPorNombre(String texto) {
        return repo.findByNombreContainingIgnoreCase(texto);
    }
}
