package com.analistas.blue.web.dto;


public class ProductoStockDTO {

    private Long accesorioId;
    private String nombre;
    private int stockActual;
    private int stockMinimo;

    /* ======================
       CONSTRUCTORES
    ====================== */

    public ProductoStockDTO() {
    }

    public ProductoStockDTO(
            Long accesorioId,
            String nombre,
            int stockActual,
            int stockMinimo
    ) {
        this.accesorioId = accesorioId;
        this.nombre = nombre;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
    }

    /* ======================
       GETTERS Y SETTERS
    ====================== */

    public Long getAccesorioId() {
        return accesorioId;
    }

    public void setAccesorioId(Long accesorioId) {
        this.accesorioId = accesorioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }
}