package com.analistas.blue.web.dto;

public class LoginResponseDTO {

    private Object usuario;
    private String token;

    public LoginResponseDTO(Object usuario, String token) {
        this.usuario = usuario;
        this.token = token;
    }

    public Object getUsuario() {
        return usuario;
    }

    public String getToken() {
        return token;
    }
}
