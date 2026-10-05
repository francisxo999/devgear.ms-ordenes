package com.devgear.ms_ordenes.event;

import com.devgear.ms_ordenes.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrdenEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public OrdenEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicarOrdenCreada(OrdenCreadaEvento evento) {
        // Routing key vacía: en un fanout exchange se ignora, todas las colas
        // enganchadas reciben el mensaje igual.
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_ORDEN_CREADA, "", evento);
    }
}