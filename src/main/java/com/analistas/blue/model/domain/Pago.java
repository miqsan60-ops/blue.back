package com.analistas.blue.model.domain;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "pagos")
public class Pago {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String paymentId;
    private String status;
    private Double amount;
    private String metodoPago;
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "compra_id", nullable = false)
    private Compra compra;
    // ===== GETTERS Y SETTERS =====

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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
    public Compra getCompra() {
    return compra;
}

public void setCompra(Compra compra) {
    this.compra = compra;
}

}
