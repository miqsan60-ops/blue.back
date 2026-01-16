package com.analistas.blue.web.controller;

import com.analistas.blue.model.service.ReporteService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = {
        "http://localhost:5500",
        "http://127.0.0.1:5500"
})
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/resumen")
    public Map<String, Object> obtenerResumen() {
        return reporteService.obtenerResumenGeneral();
    }
}