package com.analistas.blue.model.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.analistas.blue.model.domain.EstadoOrden;
import com.analistas.blue.model.domain.Orden;
import com.analistas.blue.model.domain.Usuario;
import com.analistas.blue.model.repository.OrdenRepository;

@Service
public class OrdenService {

    private final OrdenRepository ordenRepository;

    public OrdenService(OrdenRepository ordenRepository) {
        this.ordenRepository = ordenRepository;
    }

    // ======================
    // CREAR ORDEN
    // ======================
    public Orden crearOrden(Usuario usuario, Double total, String metodoPago) {

        Orden orden = new Orden();
        orden.setUsuario(usuario);
        orden.setTotal(total);
        orden.setMetodoPago(metodoPago);
        orden.setEstado(EstadoOrden.PENDIENTE_PAGO);
        orden.setFecha(LocalDateTime.now());

        return ordenRepository.save(orden);
    }

    // ======================
    // CAMBIAR ESTADO (ADMIN)
    // ======================
    public void cambiarEstado(Long ordenId, EstadoOrden estado) {
        Orden orden = ordenRepository.findById(ordenId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        orden.setEstado(estado);
        ordenRepository.save(orden);
    }

    // ======================
    // LISTAR POR USUARIO
    // ======================
    public List<Orden> ordenesUsuario(Long usuarioId) {
        return ordenRepository.findByUsuarioId(usuarioId);
    }

    // ======================
    // LISTAR TODAS (ADMIN)
    // ======================
    public List<Orden> todas() {
        return ordenRepository.findAll();
    }
}

