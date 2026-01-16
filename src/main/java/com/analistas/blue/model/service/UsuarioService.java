package com.analistas.blue.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.analistas.blue.model.domain.Usuario;
import com.analistas.blue.model.domain.Rol;
import com.analistas.blue.model.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private HttpSession session; // ⚠️ después lo sacamos

    // ======================
    // JWT / PERFIL
    // ======================
    public Usuario buscarPorUsername(String username) {
        return repository.findByUsername(username).orElse(null);
    }

    // ======================
    // LOGIN
    // ======================
    public Usuario login(String username, String password) {
        Usuario usuario = repository.findByUsername(username).orElse(null);
        if (usuario != null && usuario.getPassword().equals(password)) {
            return usuario;
        }
        return null;
    }
    // REGISTRAR CLIENTE
    public Usuario registrarCliente(Usuario usuario) {
        usuario.setRol(Rol.CLIENTE);
        return repository.save(usuario);
    }

    //  guardar usuario genérico (para ClienteController)
    public Usuario guardar(Usuario usuario) {
        return repository.save(usuario);
    }

    // CRUD
    public List<Usuario> buscarTodos() { return repository.findAll(); }
    public Usuario buscarPorId(Integer id) { return repository.findById(id).orElse(null); }
    public boolean existeUsername(String username) { return repository.existsByUsername(username); }
    public boolean existeEmail(String email) { return repository.existsByEmail(email); }
    public void eliminar(Integer id) { repository.deleteById(id); }
}

