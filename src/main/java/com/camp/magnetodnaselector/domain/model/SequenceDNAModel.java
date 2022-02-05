package com.camp.magnetodnaselector.domain.model;

import lombok.Builder;
import lombok.Data;

/**
 * Modelo de negocio para almacenar la cadena de ADN.
 *
 * @author Carlos Alberto Manrique Palacios
 */
@Data
@Builder(toBuilder = true)
public class SequenceDNAModel {

    /**
     * Cadena de ADN representada como un vector de Strind
     */
    private String[] dna;

}
