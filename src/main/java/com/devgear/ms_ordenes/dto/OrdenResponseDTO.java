package com.devgear.ms_ordenes.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrdenResponseDTO(
    Long id,
    LocalDateTime fecha,
    String estado,
    BigDecimal total,
    List<ItemOrdenResponseDTO> items
) {}