package com.camp.magnetodnaselector.domain.exception;

import java.io.IOException;

/**
 * Excepcion de negocio utilizada para las validaciones
 * de logitudes y caracteres para las seguencias de ADN
 * ingresadas en el aplicativo
 *
 * @author Carlos Alberto Manrique Palacios
 */
public class InvalidDNAException extends IOException {

    /**
     * Constructor para especificar el mensaje descriptivo
     * de la excepcion generada
     *
     * @param message El mensaje descriptivo para la excepcion
     */
    public InvalidDNAException(String message) {
        super(message);
    }

}
