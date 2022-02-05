package com.camp.magnetodnaselector.domain.model.gateway;

import com.camp.magnetodnaselector.domain.model.StatModel;

/**
 * Interface para los metodos de negocio relacionados con
 * la clase del dominio {@link StatModel}
 *
 * @author Carlos Alberto Manrique Palacios
 */
public interface StatRepository {

    /**
     * En su implementacion debe retornar un objeto de la
     * clase {@link StatModel}
     *
     * @return una instancia de la clase {@link StatModel}
     */
    StatModel getStat();

    /**
     * Es su implementacion debe calcular el valor del atributo
     * ratio al objeto statModel ingresado por parametro.
     *
     * @param statModel Objecto al cual se le debe calcular el valor del ratio
     */
    void calRatio(StatModel statModel);

}
