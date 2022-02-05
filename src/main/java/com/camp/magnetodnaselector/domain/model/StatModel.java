package com.camp.magnetodnaselector.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Modelo de negocio para representar las estadisiticas
 * de registros de cadenas de ADN almacenadas en algun
 * elemento de persistencia
 *
 * @author Carlos Alberto Manrique Palacios
 */
@Data
@Builder(toBuilder = true)
public class StatModel {

    /**
     * Cantidad de registros que cumplen con la condicion de ADN mutante
     */
    @JsonProperty("count_mutant_dna")
    private long countMutantDNA;

    /**
     * Cantidad de registros que no cumplen con la condicion de ADN mutante
     */
    @JsonProperty("count_human_dna")
    private long countHumanDNA;

    /**
     * Relacion entre las cantidades de mutantes y no mutantes
     */
    @JsonProperty("ratio")
    private double ratio;
}
