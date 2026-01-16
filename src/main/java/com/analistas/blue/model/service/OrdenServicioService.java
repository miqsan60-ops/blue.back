package com.analistas.blue.model.service;

import com.analistas.blue.model.domain.EstadoServicio;
import com.analistas.blue.model.domain.OrdenServicio;
import com.analistas.blue.model.repository.OrdenServicioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class OrdenServicioService {

    private final OrdenServicioRepository repository;

    public OrdenServicioService(OrdenServicioRepository repository) {
        this.repository = repository;
    }

    // 🔹 LISTAR TODAS
    public List<OrdenServicio> listarTodas() {
        return repository.findAll();
    }

    // 🔹 CREAR ORDEN (solo crear)
    public OrdenServicio crearOrden(OrdenServicio orden) {

        // ✅ si viene estado desde el frontend lo usamos, sino PENDIENTE
        if (orden.getEstado() == null) {
            orden.setEstado(EstadoServicio.PENDIENTE);
        }

        // 📅 fecha / hora automáticas si no vienen del frontend
        if (orden.getFecha() == null) {
            orden.setFecha(LocalDate.now());
        }

        if (orden.getHora() == null) {
            orden.setHora(LocalTime.now());
        }

        return repository.save(orden);
    }

    // 🔹 ACTUALIZAR ORDEN (editar desde INFO – NO mail)
    public OrdenServicio actualizarOrden(Long id, OrdenServicio datos) {

        OrdenServicio orden = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        orden.setCliente(datos.getCliente());
        orden.setEmail(datos.getEmail());
        orden.setTelefono(datos.getTelefono());
        orden.setServicio(datos.getServicio());
        orden.setVehiculo(datos.getVehiculo());
        orden.setDescripcion(datos.getDescripcion());
        orden.setCosto(datos.getCosto());
        orden.setDni(datos.getDni());
        orden.setPatente(datos.getPatente());

        // ✅ permitir actualizar el estado desde el modal
        if (datos.getEstado() != null) {
            orden.setEstado(datos.getEstado());
        }

        // ❗ no tocar fecha ni hora
        return repository.save(orden);
    }

    // 🔹 CAMBIAR ESTADO
    public void cambiarEstado(Long id, EstadoServicio estado) {
        OrdenServicio orden = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        orden.setEstado(estado);
        repository.save(orden);
    }

    // 🔹 ELIMINAR
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Orden no encontrada");
        }
        repository.deleteById(id);
    }
    // =========================
// ORDENES DEL USUARIO (PERFIL)
// =========================
public List<OrdenServicio> ordenesDelUsuario(Integer usuarioId) {
    return repository.findByUsuario_Id(usuarioId);
}

}