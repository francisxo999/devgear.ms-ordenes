package com.devgear.ms_ordenes.listener;

import com.devgear.ms_ordenes.config.RabbitMQConfig;
import com.devgear.ms_ordenes.event.DespachoEstadoEvento;
import com.devgear.ms_ordenes.service.OrdenService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DespachoEstadoListener {

    private final OrdenService ordenService;

    public DespachoEstadoListener(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_DESPACHO_ESTADO)
    public void onEstadoDespacho(DespachoEstadoEvento evento) {
        try {
            ordenService.sincronizarEstado(evento.ordenId(), evento.estado());
        } catch (Exception ex) {
            System.err.println("No se pudo sincronizar la orden " + evento.ordenId() + ": " + ex.getMessage());
        }
    }
}
