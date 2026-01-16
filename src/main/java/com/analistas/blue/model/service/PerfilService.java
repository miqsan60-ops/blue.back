package com.analistas.blue.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.analistas.blue.model.domain.Usuario;

import com.analistas.blue.model.repository.UsuarioRepository;
import com.analistas.blue.web.dto.PerfilDTO;

@Service
public class PerfilService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Obtener perfil por username
    public PerfilDTO obtenerPerfil(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new PerfilDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getUsername()
        );
    }

    // Actualizar datos del perfil
    public PerfilDTO actualizarPerfil(Integer id, PerfilDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());

        usuarioRepository.save(usuario);

        return new PerfilDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getUsername()
        );
    }
}
