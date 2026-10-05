package com.devgear.ms_ordenes.client;

import com.devgear.ms_ordenes.exception.ServicioCarritoException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
public class CarritoClient {

    private final RestClient restClient;

    public CarritoClient(RestClient carritoRestClient) {
        this.restClient = carritoRestClient;
    }

    /**
     * Trae el carrito actual del usuario. Reenvía el token JWT porque
     * ms-carrito requiere autenticación.
     */
    public List<ItemCarritoDTO> obtenerCarrito(String bearerToken) {
        try {
            return restClient.get()
                    .uri("/api/carrito")
                    .header(HttpHeaders.AUTHORIZATION, bearerToken)
                    .retrieve()
                    .body(new org.springframework.core.ParameterizedTypeReference<List<ItemCarritoDTO>>() {});
        } catch (RestClientException ex) {
            throw new ServicioCarritoException("No se pudo consultar el carrito en ms-carrito: " + ex.getMessage());
        }
    }

    /**
     * Vacía el carrito del usuario tras confirmar la orden.
     */
    public void vaciarCarrito(String bearerToken) {
        try {
            restClient.delete()
                    .uri("/api/carrito")
                    .header(HttpHeaders.AUTHORIZATION, bearerToken)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException ex) {
            throw new ServicioCarritoException("No se pudo vaciar el carrito en ms-carrito: " + ex.getMessage());
        }
    }
}
