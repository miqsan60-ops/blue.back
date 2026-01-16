package com.analistas.blue.model.repository;

import com.analistas.blue.model.domain.EstadoServicio;
import com.analistas.blue.model.domain.OrdenServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface OrdenServicioRepository
        extends JpaRepository<OrdenServicio, Long> {
              List<OrdenServicio> findByUsuario_Id(Integer usuarioId);

    long countByEstado(EstadoServicio estado);

   @Query("SELECT COALESCE(SUM(o.costo), 0) FROM OrdenServicio o WHERE o.estado = :estado")
BigDecimal totalFacturado(@Param("estado") EstadoServicio estado);
}