package com.devgear.ms_ordenes.service;

import com.devgear.ms_ordenes.client.CarritoClient;
import com.devgear.ms_ordenes.client.ItemCarritoDTO;
import com.devgear.ms_ordenes.dto.ItemOrdenResponseDTO;
import com.devgear.ms_ordenes.dto.OrdenResponseDTO;
import com.devgear.ms_ordenes.event.OrdenCreadaEvento;
import com.devgear.ms_ordenes.event.OrdenEventPublisher;
import com.devgear.ms_ordenes.exception.ResourceNotFoundException;
import com.devgear.ms_ordenes.model.ItemOrden;
import com.devgear.ms_ordenes.model.Orden;
import com.devgear.ms_ordenes.repository.OrdenRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final CarritoClient carritoClient;
    private final OrdenEventPublisher eventPublisher;

    public OrdenService(OrdenRepository ordenRepository, CarritoClient carritoClient,
                         OrdenEventPublisher eventPublisher) {
        this.ordenRepository = ordenRepository;
        this.carritoClient = carritoClient;
        this.eventPublisher = eventPublisher;
    }

    public OrdenResponseDTO crearOrden(String usuarioId, String email, String bearerToken) {
        List<ItemCarritoDTO> itemsCarrito = carritoClient.obtenerCarrito(bearerToken);

        if (itemsCarrito.isEmpty()) {
            throw new IllegalArgumentException("No se puede crear una orden con el carrito vacío");
        }

        Orden orden = new Orden();
        orden.setUsuarioId(usuarioId);
        orden.setFecha(LocalDateTime.now());
        orden.setEstado("PENDIENTE");

        BigDecimal total = BigDecimal.ZERO;
        for (ItemCarritoDTO itemCarrito : itemsCarrito) {
            ItemOrden item = new ItemOrden();
            item.setProductoId(itemCarrito.productoId());
            item.setNombreProducto(itemCarrito.nombreProducto());
            item.setPrecioUnitario(itemCarrito.precioUnitario());
            item.setCantidad(itemCarrito.cantidad());
            item.setSubtotal(itemCarrito.subtotal());
            orden.agregarItem(item);
            total = total.add(itemCarrito.subtotal());
        }
        orden.setTotal(total);

        Orden guardada = ordenRepository.save(orden);

        // Publicar el evento es lo crítico (Productos/Notificaciones/Despachos
        // dependen de esto) -- va primero y sin red de seguridad: si esto falla,
        // sí queremos que la operación completa falle.
        publicarEvento(guardada, email);

        // Vaciar el carrito es secundario: si falla, no debe tumbar una orden
        // que ya se creó y ya se publicó correctamente. En el peor caso, el
        // usuario ve productos "viejos" en su carrito y los borra a mano.
        try {
            carritoClient.vaciarCarrito(bearerToken);
        } catch (Exception ex) {
            System.err.println("No se pudo vaciar el carrito tras crear la orden " + guardada.getId() + ": " + ex.getMessage());
        }

        return mapToDTO(guardada);
    }

    public List<OrdenResponseDTO> obtenerOrdenesDelUsuario(String usuarioId) {
        return ordenRepository.findByUsuarioId(usuarioId).stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<OrdenResponseDTO> obtenerTodas() {
        return ordenRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    public OrdenResponseDTO obtenerPorId(String usuarioId, Long id, boolean esAdmin) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada: " + id));

        // Mismo criterio que ya usamos en ms-carrito: 404 en vez de 403 para
        // no confirmarle a un usuario que el id que probó existe y es ajeno.
        if (!esAdmin && !orden.getUsuarioId().equals(usuarioId)) {
            throw new ResourceNotFoundException("Orden no encontrada: " + id);
        }
        return mapToDTO(orden);
    }

    private void publicarEvento(Orden orden, String email) {
        List<OrdenCreadaEvento.ItemEvento> items = orden.getItems().stream()
                .map(i -> new OrdenCreadaEvento.ItemEvento(
                        i.getProductoId(), i.getNombreProducto(), i.getCantidad(), i.getPrecioUnitario()))
                .toList();

        OrdenCreadaEvento evento = new OrdenCreadaEvento(
                orden.getId(), orden.getUsuarioId(), email, orden.getFecha(), orden.getTotal(), items);

        eventPublisher.publicarOrdenCreada(evento);
    }

    private OrdenResponseDTO mapToDTO(Orden orden) {
        List<ItemOrdenResponseDTO> items = orden.getItems().stream()
                .map(i -> new ItemOrdenResponseDTO(
                        i.getProductoId(), i.getNombreProducto(), i.getPrecioUnitario(), i.getCantidad(), i.getSubtotal()))
                .toList();

        return new OrdenResponseDTO(orden.getId(), orden.getFecha(), orden.getEstado(), orden.getTotal(), items);
    }
}