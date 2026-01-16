package com.analistas.blue.web.dto;


import java.math.BigDecimal;

public class ReporteDashboardDTO {

    private long totalOrdenes;
    private long ordenesPendientes;
    private long ordenesEnProceso;
    private long ordenesFinalizadas;

    private BigDecimal totalFacturado;

    private int productosEnStock;
    private int productosBajoStock;

    /* ======================
       CONSTRUCTORES
    ====================== */

    public ReporteDashboardDTO() {
    }

    public ReporteDashboardDTO(
            long totalOrdenes,
            long ordenesPendientes,
            long ordenesEnProceso,
            long ordenesFinalizadas,
            BigDecimal totalFacturado,
            int productosEnStock,
            int productosBajoStock
    ) {
        this.totalOrdenes = totalOrdenes;
        this.ordenesPendientes = ordenesPendientes;
        this.ordenesEnProceso = ordenesEnProceso;
        this.ordenesFinalizadas = ordenesFinalizadas;
        this.totalFacturado = totalFacturado;
        this.productosEnStock = productosEnStock;
        this.productosBajoStock = productosBajoStock;
    }

    /* ======================
       GETTERS Y SETTERS
    ====================== */

    public long getTotalOrdenes() {
        return totalOrdenes;
    }

    public void setTotalOrdenes(long totalOrdenes) {
        this.totalOrdenes = totalOrdenes;
    }

    public long getOrdenesPendientes() {
        return ordenesPendientes;
    }

    public void setOrdenesPendientes(long ordenesPendientes) {
        this.ordenesPendientes = ordenesPendientes;
    }

    public long getOrdenesEnProceso() {
        return ordenesEnProceso;
    }

    public void setOrdenesEnProceso(long ordenesEnProceso) {
        this.ordenesEnProceso = ordenesEnProceso;
    }

    public long getOrdenesFinalizadas() {
        return ordenesFinalizadas;
    }

    public void setOrdenesFinalizadas(long ordenesFinalizadas) {
        this.ordenesFinalizadas = ordenesFinalizadas;
    }

    public BigDecimal getTotalFacturado() {
        return totalFacturado;
    }

    public void setTotalFacturado(BigDecimal totalFacturado) {
        this.totalFacturado = totalFacturado;
    }

    public int getProductosEnStock() {
        return productosEnStock;
    }

    public void setProductosEnStock(int productosEnStock) {
        this.productosEnStock = productosEnStock;
    }

    public int getProductosBajoStock() {
        return productosBajoStock;
    }

    public void setProductosBajoStock(int productosBajoStock) {
        this.productosBajoStock = productosBajoStock;
    }
}