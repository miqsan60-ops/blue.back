package com.analistas.blue.web.controller;

import com.analistas.blue.model.domain.Compra;
import com.analistas.blue.model.repository.CompraRepository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/admin/facturacion")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
public class FacturacionController {

    private final CompraRepository compraRepository;

    public FacturacionController(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    // 🔹 Listar todas las facturas (compras)
    @GetMapping
    public List<Compra> listarFacturacion() {
        return compraRepository.findAll();
    }

    // 🔹 Total facturado
    @GetMapping("/total")
    public BigDecimal totalFacturado() {
        return compraRepository.findAll()
                .stream()
                .map(Compra::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
