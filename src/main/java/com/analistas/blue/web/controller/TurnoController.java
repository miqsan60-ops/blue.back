package com.analistas.blue.web.controller;

import com.analistas.blue.model.domain.EstadoServicio;
import com.analistas.blue.model.domain.OrdenServicio;
import com.analistas.blue.model.repository.OrdenServicioRepository;
import com.analistas.blue.model.service.EmailTurnoService;
import com.analistas.blue.web.dto.TurnoDTO;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/turnos")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
public class TurnoController {

    private final OrdenServicioRepository ordenRepo;
    private final EmailTurnoService emailService;

    public TurnoController(OrdenServicioRepository ordenRepo,
                           EmailTurnoService emailService) {
        this.ordenRepo = ordenRepo;
        this.emailService = emailService;
    }

    @PostMapping
    public void crearTurno(@RequestBody TurnoDTO dto) {

        OrdenServicio orden = new OrdenServicio();
        orden.setCliente(dto.getNombre());
        orden.setEmail(dto.getEmail());
        orden.setTelefono(dto.getTelefono());
        orden.setVehiculo(dto.getModelo());
        orden.setServicio(dto.getServicio());
        orden.setDescripcion(dto.getComentarios());
        orden.setEstado(EstadoServicio.PENDIENTE);
        orden.setCosto(BigDecimal.ZERO);


        ordenRepo.save(orden);

        // 📧 Email al cliente
        emailService.enviarConfirmacion(
                dto.getEmail(),
                dto.getNombre(),
                dto.getServicio(),
                dto.getModelo()
        );
    }
}
