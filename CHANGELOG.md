* v1.0.0: Inicialización del microservicio de órdenes.
* v1.0.1: Corrección de la dependencia Jackson para el convertidor JSON de RabbitMQ.
* v1.0.2: Corrige el MessageConverter de RabbitMQ (Jackson2JsonMessageConverter estaba deprecado y roto en Spring Boot 4 / Spring AMQP 4, que usan Jackson 3 por defecto). Reordena crearOrden: la publicación del evento ahora es crítica y va antes de vaciar el carrito; si vaciar el carrito falla, ya no bloquea ni la creación de la orden ni la publicación del evento.
* v1.0.3: agrega docker-compose.yml para despliegue en AWS (MySQL + RabbitMQ + app).