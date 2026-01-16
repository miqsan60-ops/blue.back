package com.analistas.blue.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.analistas.blue.model.domain.Cliente;
import com.analistas.blue.model.repository.ClienteRepository;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    public Cliente guardar(Cliente cliente) {
        return repository.save(cliente);
    }

    public Cliente buscarPorEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }

    public List<Cliente> listar() {
        return repository.findAll();
    }
}
