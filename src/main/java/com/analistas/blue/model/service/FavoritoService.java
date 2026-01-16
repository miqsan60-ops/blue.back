package com.analistas.blue.model.service;

import com.analistas.blue.model.domain.Accesorio;
import com.analistas.blue.model.domain.Favorito;
import com.analistas.blue.model.domain.Usuario;
import com.analistas.blue.model.repository.AccesorioRepository;
import com.analistas.blue.model.repository.FavoritoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



import com.analistas.blue.model.domain.Favorito;
import com.analistas.blue.model.repository.FavoritoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final AccesorioRepository accesorioRepository;

    public FavoritoService(
            FavoritoRepository favoritoRepository,
            AccesorioRepository accesorioRepository
    ) {
        this.favoritoRepository = favoritoRepository;
        this.accesorioRepository = accesorioRepository;
    }

    // ======================
    // LISTAR FAVORITOS
    // ======================
    public List<Favorito> favoritosDelUsuario(Long usuarioId) {
        return favoritoRepository.findByUsuarioId(usuarioId);
    }

    // ======================
    // AGREGAR FAVORITO
    // ======================
    public Favorito agregar(Usuario usuario, Long accesorioId) {

        Long usuarioId = usuario.getId().longValue();

        // 🔥 Evita duplicados
        List<Favorito> existentes =
                favoritoRepository.findByUsuarioIdAndAccesorioId(usuarioId, accesorioId);

        if (!existentes.isEmpty()) {
            throw new RuntimeException("Ya es favorito");
        }

        Accesorio acc = accesorioRepository.findById(accesorioId)
                .orElseThrow(() -> new RuntimeException("Accesorio no encontrado"));

        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setAccesorioId(acc.getId());
        favorito.setNombreProducto(acc.getNombre());
        favorito.setImagenProducto(acc.getImagen());
        favorito.setPrecio(acc.getPrecio());

        return favoritoRepository.save(favorito);
    }

    // ======================
    // ELIMINAR FAVORITO
    // ======================
    public void eliminar(Long usuarioId, Long accesorioId) {

        List<Favorito> favoritos =
                favoritoRepository.findByUsuarioIdAndAccesorioId(usuarioId, accesorioId);

        // 🔥 Borra TODOS los duplicados
        favoritos.forEach(favoritoRepository::delete);
    }

    // ======================
    // EXISTE?
    // ======================
    public boolean existe(Long usuarioId, Long accesorioId) {
        return !favoritoRepository
                .findByUsuarioIdAndAccesorioId(usuarioId, accesorioId)
                .isEmpty();
    }
}
