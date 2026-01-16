package com.analistas.blue.model.repository;

import com.analistas.blue.model.domain.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    List<Favorito> findByUsuarioId(Long usuarioId);

    List<Favorito> findByUsuarioIdAndAccesorioId(Long usuarioId, Long accesorioId);
}
