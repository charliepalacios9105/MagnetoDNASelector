package com.camp.magnetodnaselector.persitence.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * Objeto plano auxiliar para almacenar el resultado
 * de la agregacion de conteo para la cantidad de registros
 * por el campo booleano que determina si la cadena pertenece a un
 * mutante
 *
 * @author Carlos Alberto Manrique Palacios
 */
@Data
@Builder(toBuilder = true)
public class AggResultDNACount {

    /**
     * Valor de booleano y campo de agrupamiento
     */
    private boolean mutant;

    /**
     * Guarda el conteo de los totales segun el grupo denerado por {@link #mutant}
     */
    private long total;

}
