package com.analistas.blue.model.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "accesorios")
public class Accesorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // === CATALOGO ===
    @Column(nullable = false)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    private String categoria;

    @Column(nullable = false)
    private BigDecimal precio;

    private String imagen;

    @Column(nullable = false)
    private Boolean activo = true;

    // === GETTERS Y SETTERS ===

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
