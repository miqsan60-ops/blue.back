package com.analistas.blue.web.dto;

public class InventarioDTO {

    private Long accesorioId;
    private Integer stock;
    private Integer stockMinimo;

    public Long getAccesorioId() {
        return accesorioId;
    }

    public void setAccesorioId(Long accesorioId) {
        this.accesorioId = accesorioId;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }
}
