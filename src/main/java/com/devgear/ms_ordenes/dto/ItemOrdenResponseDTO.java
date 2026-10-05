package com.devgear.ms_ordenes.dto;

import java.math.BigDecimal;

public record ItemOrdenResponseDTO(
    Long productoId,
    String nombreProducto,
    BigDecimal precioUnitario,
    Integer cantidad,
    BigDecimal subtotal
) {}