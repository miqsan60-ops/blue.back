package com.analistas.blue.web.dto;

public class CompraTransferenciaRequest {
  
    // getters / setters


    private Long accesorioId;
    private Integer cantidad;
    public Long getAccesorioId() {
        return accesorioId;
    }
    public void setAccesorioId(Long accesorioId) {
        this.accesorioId = accesorioId;
    }
    public Integer getCantidad() {
        return cantidad;
    }
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
