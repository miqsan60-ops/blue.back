package com.analistas.blue.web.dto;

import java.math.BigDecimal;

public class CompraDTO {

    private Long accesorioId;       // Para compras normales
    private Integer cantidad;       // Cantidad de unidades
    private String metodoPago;      // Ej: "Efectivo", "Tarjeta"
    private Integer usuarioId;      // ID opcional del cliente

    // Para órdenes manuales
    private String producto;        // Nombre del producto
    private BigDecimal total;       // Total de la orden

    // Nuevos campos
    private String cliente;         // Nombre del cliente manual
    private String confirmado;      // "S" o "N"

    
    // ===== Getters y Setters =====
    public Long getAccesorioId() { return accesorioId; }
    public void setAccesorioId(Long accesorioId) { this.accesorioId = accesorioId; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getConfirmado() { return confirmado; }
    public void setConfirmado(String confirmado) { this.confirmado = confirmado; }
}
