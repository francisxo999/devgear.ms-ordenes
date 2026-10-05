package com.devgear.ms_ordenes.client;

import java.math.BigDecimal;

// Refleja el ItemCarritoResponseDTO de ms-carrito
public record ItemCarritoDTO(
    Long id,
    Long productoId,
    String nombreProducto,
    BigDecimal precioUnitario,
    Integer cantidad,
    BigDecimal subtotal
) {}