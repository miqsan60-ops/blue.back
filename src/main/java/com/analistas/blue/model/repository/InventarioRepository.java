package com.analistas.blue.model.repository;

import com.analistas.blue.model.domain.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    Optional<Inventario> findByAccesorioId(Long accesorioId);

    void deleteByAccesorioId(Long accesorioId);

    // 🔴 Productos con stock bajo
    @Query("SELECT i FROM Inventario i WHERE i.stock <= i.stockMinimo")
    List<Inventario> productosBajoStock();
}