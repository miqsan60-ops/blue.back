package com.analistas.blue.model.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.analistas.blue.model.domain.Compra;

public interface CompraRepository extends JpaRepository<Compra, Long> {

List<Compra> findByUsuario_Id(Integer usuarioId);

    List<Compra> findByUsuarioIdOrderByFechaDesc(Integer usuarioId);
    List<Compra> findByConfirmado(String confirmado);
    List<Compra> findByUsuario_IdAndConfirmado(Integer usuarioId, String confirmado);


}