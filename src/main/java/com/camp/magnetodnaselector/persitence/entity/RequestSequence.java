package com.camp.magnetodnaselector.persitence.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Objeto modelo de persistencia, almcacena el vector secuencia de ADN
 * y la respuesta booleana de si es o no mutante
 *
 * @author Carlos Alberto Manrique Palacios
 */
@Document("RequestSequences")
@Data
@Builder(toBuilder = true)
public class RequestSequence {

    /**
     * Secuencia de ADN que se almacena
     */
    private String[] sequence;

    /**
     * Booleano que determina si la cadena validada pertenece a un muntante o no
     */
    private boolean mutant;

}
