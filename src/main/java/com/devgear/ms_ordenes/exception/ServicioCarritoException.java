package com.devgear.ms_ordenes.exception;

// ms-carrito no respondió, está caído, o dio un error inesperado al consultarlo.
public class ServicioCarritoException extends RuntimeException {
    public ServicioCarritoException(String message) {
        super(message);
    }
}