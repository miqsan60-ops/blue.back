package com.analistas.blue.model.service;

import com.analistas.blue.model.domain.EstadoServicio;
import com.analistas.blue.model.domain.Inventario;
import com.analistas.blue.model.repository.ClienteGestionRepository;
import com.analistas.blue.model.repository.InventarioRepository;
import com.analistas.blue.model.repository.OrdenServicioRepository;


import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReporteService {

    private final ClienteGestionRepository clienteRepo;
    private final OrdenServicioRepository ordenRepo;
    private final InventarioRepository inventarioRepo;

    public ReporteService(
            ClienteGestionRepository clienteRepo,
            OrdenServicioRepository ordenRepo,
            InventarioRepository inventarioRepo
    ) {
        this.clienteRepo = clienteRepo;
        this.ordenRepo = ordenRepo;
        this.inventarioRepo = inventarioRepo;
    }

    public Map<String, Object> obtenerResumenGeneral() {
        Map<String, Object> data = new HashMap<>();

        long totalClientes = clienteRepo.count();
        long totalOrdenes = ordenRepo.count();
        long ordenesFinalizadas = ordenRepo.countByEstado(EstadoServicio.FINALIZADO);
        BigDecimal totalFacturado = ordenRepo.totalFacturado(EstadoServicio.FINALIZADO);

        List<Inventario> stockBajo = inventarioRepo.productosBajoStock();

        data.put("totalClientes", totalClientes);
        data.put("totalOrdenes", totalOrdenes);
        data.put("ordenesFinalizadas", ordenesFinalizadas);
        data.put("totalFacturado", totalFacturado);
        data.put("productosBajoStock", stockBajo.size());

        // Para la tabla
        List<Map<String, Object>> listaStock = stockBajo.stream().map(i -> {
            Map<String, Object> m = new HashMap<>();
            m.put("descripcion", i.getAccesorio().getDescripcion());
            m.put("stock", i.getStock());
            m.put("stockMinimo", i.getStockMinimo());
            return m;
        }).toList();

        data.put("stockBajo", listaStock);

        return data;
    }
}