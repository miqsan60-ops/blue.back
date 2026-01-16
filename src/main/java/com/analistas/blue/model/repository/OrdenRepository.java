package com.analistas.blue.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.analistas.blue.model.domain.EstadoOrden;
import com.analistas.blue.model.domain.Orden;

public interface OrdenRepository extends JpaRepository<Orden, Long> {

    List<Orden> findByEstado(EstadoOrden estado);

    List<Orden> findByUsuarioId(Long usuarioId);
}
