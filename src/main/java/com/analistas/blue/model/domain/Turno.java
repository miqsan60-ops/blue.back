package com.analistas.blue.model.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "turnos")
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String email;
    private String telefono;
    private String modelo;
    private String servicio;

    @Column(length = 1000)
    private String comentarios;

    private LocalDateTime fecha;

    private String estado; // PENDIENTE / CONFIRMADO / CANCELADO

    // 🔑 RELACION CON USUARIO
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
