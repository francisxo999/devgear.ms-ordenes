package com.devgear.ms_ordenes.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// Este es el "contrato" del mensaje que viaja por RabbitMQ.
// Productos, Notificaciones y Despachos van a tener SU PROPIA copia de esta
// misma forma (no se comparte código entre microservicios), así que si cambias
// algo acá, hay que replicarlo manualmente en los otros 3 proyectos.
public record OrdenCreadaEvento(
    Long ordenId,
    String usuarioId,
    String email,
    LocalDateTime fecha,
    BigDecimal total,
    List<ItemEvento> items
) {
    public record ItemEvento(
        Long productoId,
        String nombreProducto,
        Integer cantidad,
        BigDecimal precioUnitario
    ) {}
}