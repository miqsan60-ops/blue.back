package com.analistas.blue.model.repository;

import com.analistas.blue.model.domain.Accesorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccesorioRepository extends JpaRepository<Accesorio, Long> {

    List<Accesorio> findByNombreContainingIgnoreCase(String nombre);

}
