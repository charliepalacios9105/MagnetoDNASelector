package com.camp.magnetodnaselector.config;

import com.camp.magnetodnaselector.config.model.ErrorModel;
import com.camp.magnetodnaselector.domain.exception.InvalidDNAException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Clase de configuracion para lanazadores de excepciones al
 * cliente REST.
 * <p>
 * Cada metodo implementado se puede configurar para que en el
 * momento en que la aplicacion lanza un tipo de excepcion al cliente
 * se intercepta y se le da el un tratamiento especifico.
 *
 * @author Carlos Alberto Manrique Palacios
 */
@ControllerAdvice
public class ExceptionHandlerConfig {

    /**
     * Lanzador para los tipos de excepcion {@link InvalidDNAException}
     * y {@link HttpMessageNotReadableException}.
     * <p>
     * Retorna un objeto {@link ResponseEntity} que contiene una instancia
     * de la clase {@link ErrorModel}, la cual contiene los datos de la
     * fecha y hora representada en milisegundos, el tipo de excepcion
     * la cual sera el nombre de la clase de la excepcion y el mensaje de la excepcion.
     * <p>
     * El codigo de la respuesta http sera 400 indicando que es un problema de request
     *
     * @param request objecto de la solictud realizada
     * @param ex      excepcion interceptada por el lanzador
     * @return objeto {@link ResponseEntity} con codigo de respuesta http 400
     */
    @ExceptionHandler({InvalidDNAException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorModel> handleInvalidDNAException(
            HttpServletRequest request,
            Exception ex) {
        ErrorModel errorModel = ErrorModel.builder()
                .timestamp(System.currentTimeMillis())
                .type(ex.getClass().getSimpleName())
                .message(ex.getMessage()).build();
        return new ResponseEntity<>(errorModel, HttpStatus.BAD_REQUEST);
    }

    /**
     * Lanzador para los tipos de excepciones general que no sean los interceptados
     * en el metodo {@link #handleInvalidDNAException(HttpServletRequest, Exception)}
     * <p>
     * Retorna un objeto {@link ResponseEntity} que contiene una instancia
     * de la clase {@link ErrorModel}, la cual contiene los datos de la
     * fecha y hora representada en milisegundos, el tipo de excepcion
     * la cual sera el nombre de la clase de la excepcion y el mensaje de la excepcion.
     * <p>
     * El codigo de la respuesta http sera 500 indicando que para cualquier otro tipo
     * de excepcion se asume como un internal server error
     *
     * @param request objecto de la solictud realizada
     * @param ex      excepcion interceptada por el lanzador
     * @return objeto {@link ResponseEntity} con codigo de respuesta http 500
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorModel> handleGeneralException(
            HttpServletRequest request,
            Exception ex) {
        ErrorModel errorModel = ErrorModel.builder()
                .timestamp(System.currentTimeMillis())
                .type(ex.getClass().getSimpleName())
                .message(ex.getMessage()).build();
        return new ResponseEntity<>(errorModel, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
