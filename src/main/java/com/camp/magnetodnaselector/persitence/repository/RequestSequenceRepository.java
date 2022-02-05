package com.camp.magnetodnaselector.persitence.repository;

import com.camp.magnetodnaselector.persitence.entity.RequestSequence;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de datos para la clase {@link RequestSequence}
 * <p>
 * Ya que la implementacion de persistencia se hace por medio de una
 * base de datos mongoDB extiende a {@link MongoRepository}
 *
 * @author Carlos Alberto Manrique Palacios
 */
@Repository
public interface RequestSequenceRepository extends MongoRepository<RequestSequence, String> {

    /**
     * Metodo de busqueda para la clase {@link RequestSequence} por el atributo
     * String[] sequence, el cual mongoDB tiene la ventaja de guardar como un
     * vector y de igual manera permitir su busqueda como uno
     *
     * @param sequence secuencia de busqueda
     * @return Optional que contiene el objecto RequestSequence con la
     * sequencia ingresada por parametro
     */
    Optional<RequestSequence> findBySequence(String[] sequence);

}
