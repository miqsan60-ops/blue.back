package com.analistas.blue.model.service;

import com.analistas.blue.model.domain.ClienteGestion;
import com.analistas.blue.model.repository.ClienteGestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteGestionService {

    private final ClienteGestionRepository repo;

    public ClienteGestionService(ClienteGestionRepository repo) {
        this.repo = repo;
    }

    public List<ClienteGestion> listar() {
        return repo.findAll();
    }

    public ClienteGestion guardar(ClienteGestion c) {
        return repo.save(c);
    }

    public ClienteGestion actualizar(Long id, ClienteGestion c) {
        ClienteGestion existente = repo.findById(id).orElseThrow();
        existente.setDni(c.getDni());
        existente.setApellido(c.getApellido());
        existente.setNombre(c.getNombre());
        existente.setTelefono(c.getTelefono());
        existente.setEmail(c.getEmail());
        return repo.save(existente);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
