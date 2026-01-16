package com.analistas.blue.web.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.analistas.blue.model.domain.Favorito;
import com.analistas.blue.model.domain.Usuario;
import com.analistas.blue.model.service.FavoritoService;
import com.analistas.blue.model.service.UsuarioService;
import com.analistas.blue.web.security.JwtUtil;

@RestController
@RequestMapping("/api/favoritos")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
public class FavoritoController {

    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;
    private final FavoritoService favoritoService;

    public FavoritoController(
            JwtUtil jwtUtil,
            UsuarioService usuarioService,
            FavoritoService favoritoService
    ) {
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
        this.favoritoService = favoritoService;
    }

    // ======================
    // LISTAR FAVORITOS
    // ======================
    @GetMapping
    public List<Favorito> listar(@RequestHeader("Authorization") String auth) {
        String token = auth.replace("Bearer ", "");
        String username = jwtUtil.extraerUsername(token);

        Usuario usuario = usuarioService.buscarPorUsername(username);
        Long usuarioId = usuario.getId().longValue();

        return favoritoService.favoritosDelUsuario(usuarioId);
    }

    // ======================
    // AGREGAR FAVORITO
    // ======================
   @PostMapping
public Favorito agregar(
        @RequestHeader("Authorization") String auth,
        @RequestBody Map<String, Long> body
) {
    String token = auth.replace("Bearer ", "");
    String username = jwtUtil.extraerUsername(token);

    Usuario usuario = usuarioService.buscarPorUsername(username);

    Long accesorioId = body.get("accesorioId");

    return favoritoService.agregar(usuario, accesorioId);
}

    // ======================
    // ELIMINAR FAVORITO
    // ======================
    @DeleteMapping("/{accesorioId}")
    public void eliminar(
            @RequestHeader("Authorization") String auth,
            @PathVariable Long accesorioId
    ) {
        String token = auth.replace("Bearer ", "");
        String username = jwtUtil.extraerUsername(token);

        Usuario usuario = usuarioService.buscarPorUsername(username);
        Long usuarioId = usuario.getId().longValue();

        favoritoService.eliminar(usuarioId, accesorioId);
    }
}
