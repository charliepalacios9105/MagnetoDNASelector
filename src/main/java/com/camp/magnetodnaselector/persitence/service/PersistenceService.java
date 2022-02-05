package com.camp.magnetodnaselector.persitence.service;

import com.camp.magnetodnaselector.domain.model.SequenceDNAModel;
import com.camp.magnetodnaselector.domain.model.StatModel;
import com.camp.magnetodnaselector.domain.model.gateway.SequenceDNARepository;
import com.camp.magnetodnaselector.domain.model.gateway.StatRepository;
import com.camp.magnetodnaselector.persitence.entity.RequestSequence;
import com.camp.magnetodnaselector.persitence.pojo.AggResultDNACount;
import com.camp.magnetodnaselector.persitence.repository.RequestSequenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * Servicio para la capa de persistencia el es la esceficicion de las
 * interface de negocio
 *
 * @author Carlos Alberto Manrique Palacios
 */
@RequiredArgsConstructor
@Service
public class PersistenceService implements SequenceDNARepository, StatRepository {

    /**
     * Objeto que implementa las funcionalidades del respositorio de
     * persistencia para MongoDB, inyectado por el contenedor
     */
    private final RequestSequenceRepository requestSequenceRepository;

    /**
     * Objeto que implementa las funcionalidades generales para MongoDB,
     * inyectado por el contenedor
     */
    private final MongoTemplate mongoTemplate;

    /**
     * Este metodo realiza una tarea de agregacion a la base de datos
     * MongoDB, la cual cuenta la cantidad de registros y agrupa por
     * el booleano mutant.
     * <p>
     * Hace la coversion de datos a objetos por medio de la clase {@link AggResultDNACount},
     * por lo que se espera como respuesta un listado de maximo 2 registros de objetos de dicha
     * clase
     *
     * @return Listado de objetos de la clase {@link AggResultDNACount} que contiene el resultado de la agregacion
     */
    private List<AggResultDNACount> findStats() {
        Aggregation agg = newAggregation(
                group("mutant").count().as("total"),
                project("total").and("mutant").previousOperation());
        AggregationResults<AggResultDNACount> groupResults
                = mongoTemplate.aggregate(agg, RequestSequence.class, AggResultDNACount.class);
        return groupResults.getMappedResults();
    }

    /**
     * Verifica si el vector ingresado por parametro ya se encuentra registrado en
     * la base de datos y en tal caso retorna el valor del campo mutant guardado previamente,
     * en caso contrario retorna null
     *
     * @param dna cadena de ADN que se debe buscar
     * @return null si no existe un registro con esa cadena, en caso contrario retorna el valor del campo mutant
     */
    @Override
    public Boolean isMutantSavedDNA(String[] dna) {
        RequestSequence requestSequence = requestSequenceRepository.findBySequence(dna).orElse(null);
        return requestSequence != null ? requestSequence.isMutant() : null;
    }

    /**
     * Crea un objeto de la calse {@link SequenceDNAModel} el cual persiste
     * en la base de datos MongoDB con los campos de la secuencia y el
     * booleano ingresado por parametro que determina si cumple la condicion
     * del mutante
     *
     * @param sequenceDNAModel La cadena de ADN a guardar
     * @param mutant           Campo booleano que determina si la cadena cumple la condicion del mutante o no
     */
    @Override
    public void saveDNA(SequenceDNAModel sequenceDNAModel, boolean mutant) {
        requestSequenceRepository.save(RequestSequence.builder()
                .sequence(sequenceDNAModel.getDna())
                .mutant(mutant).build());
    }


    /**
     * Ha el llamado al metodo {@link #findStats()} y a partir del
     * resultado creara el objeto de la clase {@link StatModel} el cual
     * contiene los valores especificacdos por las reglas de negocio para el
     * servicio de estadistica
     *
     * @return la instancia de la clase {@link StatModel} que contiene los datos
     * solitcitados para el servicio de estadistica
     */
    @Override
    public StatModel getStat() {
        List<AggResultDNACount> stats = findStats();
        StatModel statModel = StatModel.builder().build();
        for (AggResultDNACount ar : stats) {
            if (ar.isMutant()) {
                statModel.setCountMutantDNA(ar.getTotal());
            } else {
                statModel.setCountHumanDNA(ar.getTotal());
            }
        }
        calRatio(statModel);
        return statModel;
    }

    /**
     * Calculo del campo ratio, el cual segun la especificacion sera:
     * cantidadMuntantes/cantidadHumanos
     *
     * @param statModel Objecto al cual se le debe calcular el valor del ratio
     */
    @Override
    public void calRatio(StatModel statModel) {
        if (statModel.getCountHumanDNA() > 0 && statModel.getCountMutantDNA() > 0) {
            statModel.setRatio(Math.round((((double) statModel.getCountMutantDNA() / (double) statModel.getCountHumanDNA()) * 100d)) / 100d);
        }
    }
}
