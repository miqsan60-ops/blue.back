package com.analistas.blue.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.analistas.blue.model.domain.Turno;

public interface TurnoRepository extends JpaRepository<Turno, Long> {

 List<Turno> findByUsuario_Id(Long usuarioId);
    List<Turno> findByUsuario_Id(Integer usuarioId);
}


 

