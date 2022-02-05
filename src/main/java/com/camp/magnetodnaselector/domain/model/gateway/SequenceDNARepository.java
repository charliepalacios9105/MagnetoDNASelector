package com.camp.magnetodnaselector.domain.model.gateway;

import com.camp.magnetodnaselector.domain.model.SequenceDNAModel;

/**
 * Interface para los metodos de negocio relacionados con
 * la clase del dominio {@link SequenceDNAModel}
 *
 * @author Carlos Alberto Manrique Palacios
 */
public interface SequenceDNARepository {

    /**
     * En su especificacion debe determinar si la cadena de ADN
     * ingresada por parametro esta almacenada en algun medio de persistencia
     * y en tal caso retorar falso o verdadero para determinar si cumple con
     * las condiciones de mutante
     *
     * @param dna cadena de AND que se debe buscar
     * @return si se encuentra la cadena guardada el valor que determina si es mutante,
     * en otro caso null
     */
    Boolean isMutantSavedDNA(String[] dna);

    /**
     * En su implementacion debe guardar en algun medio de persistencia las
     * secuencia de ADN y el booleano que determina si es un mutante
     *
     * @param sequenceDNAModel La cadena de AND a guardar
     * @param mutant           Campo que determina si la cadena a
     *                         guadar cumple con la condicion del mutante
     */
    void saveDNA(SequenceDNAModel sequenceDNAModel, boolean mutant);

}
