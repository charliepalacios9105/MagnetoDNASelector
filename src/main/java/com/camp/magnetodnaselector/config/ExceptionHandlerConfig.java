package com.camp.magnetodnaselector.config;

import com.camp.magnetodnaselector.config.model.ErrorModel;
import com.camp.magnetodnaselector.domain.exception.InvalidDNAException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlerConfig {

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
