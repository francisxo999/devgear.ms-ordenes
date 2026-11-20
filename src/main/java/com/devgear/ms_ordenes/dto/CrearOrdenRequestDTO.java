package com.devgear.ms_ordenes.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Cuerpo opcional de POST /api/ordenes. ms-pagos lo manda con el medio de pago que
// informó Webpay; si el cuerpo no viene, la orden se crea igual (metodoPago = null).
@JsonIgnoreProperties(ignoreUnknown = true)
public record CrearOrdenRequestDTO(String metodoPago) {}
