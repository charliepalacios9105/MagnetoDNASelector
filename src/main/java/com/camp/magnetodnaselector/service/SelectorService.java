package com.camp.magnetodnaselector.service;

import com.camp.magnetodnaselector.domain.exception.InvalidDNAException;
import com.camp.magnetodnaselector.domain.model.SequenceDNAModel;
import com.camp.magnetodnaselector.domain.model.StatModel;
import com.camp.magnetodnaselector.domain.usecase.SequenceDNAUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Clase de servicio que sirve como punto de entrada para las funcionalidades
 * relacionadas con los objetos de negocio.
 * <p>
 * Contiene las llamadas a los metodos que ejecutan la validacion del vector de String que
 * contiene las secuencias de ADN y de la consulta a las estadisticas
 *
 * @author Carlos Alberto Manrique Palacios
 */
@Service
@RequiredArgsConstructor
public class SelectorService {

    /**
     * Objecto de la clase de negocio {@link SequenceDNAUseCase}
     * inyectado por el contenedor
     */
    private final SequenceDNAUseCase sequenceDNAUseCase;

    /**
     * Hace el llamado al metodo {@link SequenceDNAUseCase#isMutant(SequenceDNAModel)} que
     * contiene toda la logica necesaria para determinar si el vector ingresado cumple con la condicion
     * del mutante
     *
     * @param sequenceDNAModel Objeto de negocio contenedor del vector de strings que se debe evaluar
     * @return Objecto de la clases {@link ResponseEntity} con el codigo correspondiente segun
     * la validacion de la cadena
     * @throws InvalidDNAException Excepcion lanzada desde la funcionalidad
     *                             isMutant en caso se exisitir alguna inconsistencia en en vector
     */
    public boolean isMutant(SequenceDNAModel sequenceDNAModel) throws InvalidDNAException {
        return sequenceDNAUseCase.isMutant(sequenceDNAModel);
    }

    /**
     * Hace el llamado al metodo {@link SequenceDNAUseCase#getStat()} el cual
     * hace el llamado a la logica necesaria para retornar el objeto con la estadistica solicitada
     *
     * @return Objecto  de negocio que tiene los conteos solicitados en las especificaiones del problema
     */
    public StatModel findStats() {
        return sequenceDNAUseCase.getStat();
    }
}
