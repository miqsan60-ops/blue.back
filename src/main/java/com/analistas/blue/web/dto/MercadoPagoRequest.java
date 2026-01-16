package com.analistas.blue.web.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MercadoPagoRequest {

    private Long accesorioId;
    private Integer cantidad;

    private String token;
    private String paymentMethodId;
    private Integer installments;
    private Long issuerId;
}
