package com.analistas.blue.web.dto;

public class PerfilDTO {

    private Integer id;
    private String nombre;
    private String apellido;
    private String email;
    private String username;

    public PerfilDTO() {}

    public PerfilDTO(Integer id, String nombre, String apellido, String email, String username) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.username = username;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
