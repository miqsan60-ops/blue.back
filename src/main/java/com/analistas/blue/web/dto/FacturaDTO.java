package com.analistas.blue.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FacturaDTO {

    private Long id;              // Nº de orden / comprobante
    private String cliente;       // Nombre y apellido del cliente
    private String producto;      // Nombre del producto
    private Integer cantidad;
    private BigDecimal total;
    private String metodoPago;
    private LocalDateTime fecha;
    private String comprobanteUrl;

  

    // 👉 CAMPO FALTANTE
    private String confirmado;    // "S" o "N"

    // ======================
    // GETTERS Y SETTERS
    // ======================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    // 👉 CONFIRMADO
    public String getConfirmado() {
        return confirmado;
    }

    public void setConfirmado(String confirmado) {
        this.confirmado = confirmado;
    }
      public String getComprobanteUrl() {
        return comprobanteUrl;
    }

    public void setComprobanteUrl(String comprobanteUrl) {
        this.comprobanteUrl = comprobanteUrl;
    }
}
