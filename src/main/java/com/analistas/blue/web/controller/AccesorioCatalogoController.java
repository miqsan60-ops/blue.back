package com.analistas.blue.web.controller;

import com.analistas.blue.model.domain.Accesorio;
import com.analistas.blue.model.domain.Inventario;
import com.analistas.blue.model.service.AccesorioService;
import com.analistas.blue.model.service.InventarioService;
import com.analistas.blue.web.dto.AccesorioCatalogoDTO;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/catalogo")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
public class AccesorioCatalogoController {

    private final AccesorioService accesorioService;
    private final InventarioService inventarioService;

    public AccesorioCatalogoController(
            AccesorioService accesorioService,
            InventarioService inventarioService) {
        this.accesorioService = accesorioService;
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public List<AccesorioCatalogoDTO> listarCatalogo() {

        List<Accesorio> accesorios = accesorioService.listarTodos();
        List<AccesorioCatalogoDTO> resultado = new ArrayList<>();

        for (Accesorio acc : accesorios) {

            Inventario inv = inventarioService.buscarPorAccesorio(acc.getId());

            AccesorioCatalogoDTO dto = new AccesorioCatalogoDTO();
            dto.setId(acc.getId());
            dto.setNombre(acc.getNombre());
            dto.setDescripcion(acc.getDescripcion());
            dto.setCategoria(acc.getCategoria());
            dto.setPrecio(acc.getPrecio());
            dto.setImagen(acc.getImagen());

            if (inv != null) {
                dto.setStock(inv.getStock());
                dto.setStockMinimo(inv.getStockMinimo());

                if (inv.getStock() <= 0) {
                    dto.setEstadoStock("SIN_STOCK");
                } else if (inv.getStock() <= inv.getStockMinimo()) {
                    dto.setEstadoStock("STOCK_BAJO");
                } else {
                    dto.setEstadoStock("OK");
                }
            } else {
                dto.setStock(0);
                dto.setStockMinimo(0);
                dto.setEstadoStock("SIN_INVENTARIO");
            }

            resultado.add(dto);
        }

        return resultado;
    }
}
