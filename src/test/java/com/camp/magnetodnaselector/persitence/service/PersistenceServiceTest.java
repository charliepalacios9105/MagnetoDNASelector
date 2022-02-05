package com.camp.magnetodnaselector.persitence.service;

import com.camp.magnetodnaselector.domain.model.SequenceDNAModel;
import com.camp.magnetodnaselector.domain.model.StatModel;
import com.camp.magnetodnaselector.persitence.entity.RequestSequence;
import com.camp.magnetodnaselector.persitence.repository.RequestSequenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas para la clase {@link PersistenceService}
 * Se anota con @SpringBootTest para crear un contexto de prueba
 * de SpringBoot.
 * <p>
 * Por defecto la configuracion de spring al implementar la
 * libreria flapdoodle.embed.mongo en test, el contexto de prueba carga
 * una instancia de mongoDB en memoria lo que permite la realizacion de pruebas
 * sobre un contexto de persistencia similar al de los ambientes productivos
 *
 * @author Carlos Alberto Manrique Palacios
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class PersistenceServiceTest {

    @Autowired
    private PersistenceService persistenceService;

    @Autowired
    private RequestSequenceRepository requestSequenceRepository;

    /**
     * Varifica el correcto guardado de un objeto de la clase
     * {@link RequestSequence} como una coleccion del documento anotado
     * en dicha clase
     */
    @Test
    void saveDNATest() {
        requestSequenceRepository.deleteAll();
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        persistenceService.saveDNA(sequenceDNAModel, true);
        RequestSequence requestSequence = requestSequenceRepository.findBySequence(dna).get();
        assertEquals(Arrays.toString(dna), Arrays.toString(requestSequence.getSequence()));
    }

    /**
     * Varifica la busqueda de una coleccion por medio de un secuencia dada
     * al no existir dicha coleccion en la secuencia se espera un null
     */
    @Test
    void isSavedDNANullTest() {
        String[] dna = {"ATGTGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        Boolean res = persistenceService.isMutantSavedDNA(dna);
        assertNull(res);
    }

    /**
     * Varifica la busqueda de una coleccion por medio de un secuencia dada
     * al existir dicha secuencia y tener un valor true en el campo mutant se espera
     * un true en la asercion
     */
    @Test
    void isSavedDNATrueTest() {
        requestSequenceRepository.deleteAll();
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        persistenceService.saveDNA(sequenceDNAModel, true);
        Boolean res = persistenceService.isMutantSavedDNA(dna);
        assertTrue(res);
    }

    /**
     * Varifica la busqueda las estadisitcas para el caso donde solo hay
     * un mutante registrado, se espera la correcta ejecucion en base de datos
     * y un ratio de 0
     */
    @Test
    void getStatOneMutantTest() {
        requestSequenceRepository.deleteAll();
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        persistenceService.saveDNA(sequenceDNAModel, true);
        StatModel stat = persistenceService.getStat();
        assertEquals(0, stat.getRatio());
        assertEquals(0, stat.getCountHumanDNA());
        assertEquals(1, stat.getCountMutantDNA());
    }

    /**
     * Varifica la busqueda las estadisitcas para el caso donde solo hay
     * un humano registrado, se espera la correcta ejecucion en base de datos
     * y un ratio de 0
     */
    @Test
    void getStatOneHumanTest() {
        requestSequenceRepository.deleteAll();
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        persistenceService.saveDNA(sequenceDNAModel, false);
        StatModel stat = persistenceService.getStat();
        assertEquals(0, stat.getRatio());
        assertEquals(1, stat.getCountHumanDNA());
        assertEquals(0, stat.getCountMutantDNA());
    }

    /**
     * Varifica la busqueda las estadisitcas para el caso donde no hay
     * datos registrados, se espera la correcta ejecucion en base de datos
     * y un ratio de 0 y conteos de 0
     */
    @Test
    void getStatNoDataTest() {
        requestSequenceRepository.deleteAll();
        StatModel stat = persistenceService.getStat();
        assertEquals(0, stat.getRatio());
        assertEquals(0, stat.getCountHumanDNA());
        assertEquals(0, stat.getCountMutantDNA());
    }

    /**
     * Varifica la busqueda las estadisitcas para el caso donde
     * se ingresan previamente 140 colecciones de las cuales 100 son
     * de humanos y 40 de mutantes, se espera la correcta ejecucion
     * en base de datos y un ratio de 0,4 y conteos 100 humanos y 40 mutantes
     */
    @Test
    void getStatTest() {
        requestSequenceRepository.deleteAll();
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        for (int i = 0; i < 140; i++) {
            persistenceService.saveDNA(sequenceDNAModel, i >= 100);
        }
        StatModel stat = persistenceService.getStat();
        assertEquals(0.4, stat.getRatio());
        assertEquals(100, stat.getCountHumanDNA());
        assertEquals(40, stat.getCountMutantDNA());
    }


}