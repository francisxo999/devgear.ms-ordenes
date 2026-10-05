package com.devgear.ms_ordenes.controller;

import com.devgear.ms_ordenes.dto.OrdenResponseDTO;
import com.devgear.ms_ordenes.service.OrdenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    private final OrdenService ordenService;

    public OrdenController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    // Crea la orden a partir del carrito actual del usuario (sin body: no hace
    // falta mandar nada, se lee todo del carrito + del token).
    @PostMapping
    public ResponseEntity<OrdenResponseDTO> crearOrden(JwtAuthenticationToken auth) {
        OrdenResponseDTO orden = ordenService.crearOrden(usuarioId(auth), email(auth), bearerToken(auth));
        return ResponseEntity.status(HttpStatus.CREATED).body(orden);
    }

    @GetMapping
    public ResponseEntity<List<OrdenResponseDTO>> misOrdenes(JwtAuthenticationToken auth) {
        return ResponseEntity.ok(ordenService.obtenerOrdenesDelUsuario(usuarioId(auth)));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrdenResponseDTO>> todasLasOrdenes() {
        return ResponseEntity.ok(ordenService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenResponseDTO> obtenerOrden(@PathVariable Long id, JwtAuthenticationToken auth) {
        boolean esAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(ordenService.obtenerPorId(usuarioId(auth), id, esAdmin));
    }

    private String usuarioId(JwtAuthenticationToken auth) {
        return auth.getToken().getClaimAsString("oid");
    }

    private String email(JwtAuthenticationToken auth) {
        return auth.getToken().getClaimAsString("preferred_username");
    }

    private String bearerToken(JwtAuthenticationToken auth) {
        return "Bearer " + auth.getToken().getTokenValue();
    }
}