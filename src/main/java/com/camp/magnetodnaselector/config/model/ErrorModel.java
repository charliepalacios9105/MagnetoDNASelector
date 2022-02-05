package com.camp.magnetodnaselector.config.model;

import lombok.Builder;
import lombok.Data;

/**
 * Objeto de transferencia para mostar los posibles errores
 * en el cliente consumidor de servicios.
 * <p>
 *
 * @author Carlos Alberto Manrique Palacios
 */
@Data
@Builder(toBuilder = true)
public class ErrorModel {

    /**
     * Representacion de la hora y fecha en milisegundos del retorno de la error
     */
    private long timestamp;

    /**
     * Tipo de excepcion o error
     */
    private String type;

    /**
     * Mensaje mostrado al usuario con la descripcion del error
     */
    private String message;
}
