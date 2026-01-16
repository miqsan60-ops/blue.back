package com.analistas.blue.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "compras")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comprobanteUrl;

  
    // ======================
    // PRODUCTO
    // ======================
    @Column(name = "accesorio_id", nullable = true)
    private Long accesorioId;

    @Column(name = "nombre_producto", nullable = false)
    private String nombreProducto;

    @Column(name = "imagen_producto")
    private String imagenProducto;

    @Column(name = "precio_unitario", nullable = false)
    private BigDecimal precioUnitario;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private BigDecimal total;

    // ======================
    // PAGO
    // ======================
    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago;

    @Column(nullable = false)
    private LocalDateTime fecha;

    // ======================
    // CLIENTE REAL
    // ======================
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties({"password", "compras"})
    private Usuario usuario;

    // ======================
    // CLIENTE MANUAL
    // ======================
    @Column(name = "cliente_manual")
    private String clienteManual;



    // ======================
    // CONFIRMADO (S / N)
    // ======================
    @Column(name = "confirmado", length = 1)
    private String confirmado;


    // ======================
// MERCADO PAGO
// ======================
@Column(name = "payment_id")
private Long paymentId;

public Long getPaymentId() {
    return paymentId;
}

public void setPaymentId(Long paymentId) {
    this.paymentId = paymentId;
}

public String getPaymentStatus() {
    return paymentStatus;
}

public void setPaymentStatus(String paymentStatus) {
    this.paymentStatus = paymentStatus;
}

@Column(name = "payment_status")
private String paymentStatus;

    // ======================
    // GETTERS Y SETTERS
    // ======================
    public Long getId() { return id; }

    public Long getAccesorioId() { return accesorioId; }
    public void setAccesorioId(Long accesorioId) { this.accesorioId = accesorioId; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public String getImagenProducto() { return imagenProducto; }
    public void setImagenProducto(String imagenProducto) { this.imagenProducto = imagenProducto; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getClienteManual() { return clienteManual; }
    public void setClienteManual(String clienteManual) { this.clienteManual = clienteManual; }

    public String getConfirmado() { return confirmado; }
    public void setConfirmado(String confirmado) { this.confirmado = confirmado; }

      public String getComprobanteUrl() {
        return comprobanteUrl;
    }

    public void setComprobanteUrl(String comprobanteUrl) {
        this.comprobanteUrl = comprobanteUrl;
    }
    
}
