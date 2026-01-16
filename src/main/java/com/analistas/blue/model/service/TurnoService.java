package com.analistas.blue.model.service;

import com.analistas.blue.model.domain.Turno;
import com.analistas.blue.model.domain.Usuario;
import com.analistas.blue.model.repository.TurnoRepository;
import com.analistas.blue.web.dto.TurnoDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TurnoService {

    private final TurnoRepository turnoRepository;
    private final EmailTurnoService emailTurnoService;

    // ✅ UN SOLO CONSTRUCTOR
    public TurnoService(TurnoRepository turnoRepository,
                        EmailTurnoService emailTurnoService) {
        this.turnoRepository = turnoRepository;
        this.emailTurnoService = emailTurnoService;
    }

    // =========================
// TURNOS DEL USUARIO
// =========================
public List<Turno> turnosDelUsuario(Integer usuarioId) {
    return turnoRepository.findByUsuario_Id(usuarioId);
}


    // =========================
    // CREAR TURNO
    // =========================
    public void crearTurno(TurnoDTO dto, Usuario usuarioLogueado) {

        Turno turno = new Turno();
        turno.setNombre(dto.getNombre());
        turno.setEmail(dto.getEmail());
        turno.setTelefono(dto.getTelefono());
        turno.setModelo(dto.getModelo());
        turno.setServicio(dto.getServicio());
        turno.setComentarios(dto.getComentarios());
        turno.setFecha(LocalDateTime.now());
        turno.setEstado("PENDIENTE");

        // 🔑 CLAVE PARA PERFIL
        turno.setUsuario(usuarioLogueado);

        turnoRepository.save(turno);

        // 📧 Email al cliente
        emailTurnoService.enviarConfirmacion(
                dto.getEmail(),
                dto.getNombre(),
                dto.getServicio(),
                dto.getModelo()
        );
    }
}
