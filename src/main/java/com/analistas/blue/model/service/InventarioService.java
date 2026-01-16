package com.analistas.blue.model.service;

import com.analistas.blue.model.domain.Accesorio;
import com.analistas.blue.model.domain.Inventario;
import com.analistas.blue.model.repository.AccesorioRepository;
import com.analistas.blue.model.repository.InventarioRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioService {

    private final InventarioRepository inventarioRepo;
    private final AccesorioRepository accesorioRepo;

    public InventarioService(InventarioRepository inventarioRepo,
                             AccesorioRepository accesorioRepo) {
        this.inventarioRepo = inventarioRepo;
        this.accesorioRepo = accesorioRepo;
    }

    // ======================
    // LISTAR
    // ======================
    public List<Inventario> listar() {
        return inventarioRepo.findAll();
    }

    // ======================
    // BUSCAR POR ACCESORIO
    // ======================
    public Inventario buscarPorAccesorio(Long accesorioId) {
        return inventarioRepo.findByAccesorioId(accesorioId).orElse(null);
    }

    // ======================
    // CREAR
    // ======================
    public Inventario crear(Long accesorioId, Integer stock, Integer stockMinimo) {
        Accesorio accesorio = accesorioRepo.findById(accesorioId)
                .orElseThrow(() -> new RuntimeException("Accesorio no existe"));

        Inventario inv = new Inventario();
        inv.setAccesorio(accesorio);
        inv.setStock(stock);
        inv.setStockMinimo(stockMinimo);

        return inventarioRepo.save(inv);
    }

    // ======================
    // ACTUALIZAR POR ACCESORIO
    // ======================
    public Inventario actualizarPorAccesorio(Long accesorioId,
                                             Integer stock,
                                             Integer stockMinimo) {

        Inventario inv = inventarioRepo.findByAccesorioId(accesorioId)
                .orElseThrow(() -> new RuntimeException("Inventario no existe"));

        inv.setStock(stock);
        inv.setStockMinimo(stockMinimo);

        return inventarioRepo.save(inv);
    }

    // ======================
    // ELIMINAR
    // ======================
    public void eliminar(Long id) {
        inventarioRepo.deleteById(id);
    }

    @Transactional
    public void eliminarPorAccesorio(Long accesorioId) {
        inventarioRepo.deleteByAccesorioId(accesorioId);
    }
    @Transactional
public void descontarStock(Long accesorioId, int cantidad) {

    Inventario inv = inventarioRepo.findByAccesorioId(accesorioId)
            .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

    if (inv.getStock() < cantidad) {
        throw new RuntimeException("Stock insuficiente");
    }

    inv.setStock(inv.getStock() - cantidad);
    inventarioRepo.save(inv);
}
public Inventario guardar(Inventario inv) {
    return inventarioRepo.save(inv);
}

}
