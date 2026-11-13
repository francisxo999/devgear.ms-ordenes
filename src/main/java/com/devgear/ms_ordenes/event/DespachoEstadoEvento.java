package com.devgear.ms_ordenes.event;

import java.time.LocalDateTime;

// Copia propia del contrato que publica ms-despachos (no se comparte código entre servicios).
public record DespachoEstadoEvento(
    Long despachoId,
    Long ordenId,
    String usuarioId,
    String estado,
    LocalDateTime fecha
) {}
