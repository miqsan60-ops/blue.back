package com.analistas.blue.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.analistas.blue.model.domain.Usuario;
import com.analistas.blue.model.service.MercadoPagoService;
import com.analistas.blue.model.service.UsuarioService;
import com.analistas.blue.web.dto.MercadoPagoRequest;
import com.analistas.blue.web.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mercadopago")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
@RequiredArgsConstructor
public class MercadoPagoController {

    private final MercadoPagoService mercadoPagoService;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    @PostMapping("/pagar")
    public ResponseEntity<?> pagar(
            @RequestHeader("Authorization") String auth,
            @RequestBody MercadoPagoRequest req
    ) throws Exception {
        String token = auth.replace("Bearer ", "");
        String username = jwtUtil.extraerUsername(token);
        Usuario usuario = usuarioService.buscarPorUsername(username);

        return ResponseEntity.ok(
                mercadoPagoService.procesarPago(req, usuario)
        );
    }
}
