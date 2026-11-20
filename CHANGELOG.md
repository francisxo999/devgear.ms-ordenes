* v1.0.0: Inicialización del microservicio de órdenes.
* v1.0.1: Corrección de la dependencia Jackson para el convertidor JSON de RabbitMQ.
* v1.0.2: Corrige el MessageConverter de RabbitMQ (Jackson2JsonMessageConverter estaba deprecado y roto en Spring Boot 4 / Spring AMQP 4, que usan Jackson 3 por defecto). Reordena crearOrden: la publicación del evento ahora es crítica y va antes de vaciar el carrito; si vaciar el carrito falla, ya no bloquea ni la creación de la orden ni la publicación del evento.
* v1.0.3: agrega docker-compose.yml para despliegue en AWS (MySQL + RabbitMQ + app).
* v1.1.0: Consume el estado del despacho (cola ordenes.despacho-estado.queue, enlace despacho.*) y actualiza el estado de la orden.
* v1.2.0: Cada ítem de la orden guarda la imagen del producto y la orden guarda el método de pago (cuerpo opcional de POST /api/ordenes); ambos datos viajan en OrdenCreadaEvento hacia RabbitMQ.
