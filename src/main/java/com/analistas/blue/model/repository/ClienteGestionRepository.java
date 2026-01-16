package com.analistas.blue.model.repository;

import com.analistas.blue.model.domain.ClienteGestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteGestionRepository 
        extends JpaRepository<ClienteGestion, Long> {
}
