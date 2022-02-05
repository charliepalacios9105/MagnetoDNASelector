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


@ExtendWith(SpringExtension.class)
@SpringBootTest
class PersistenceServiceTest {

    @Autowired
    private PersistenceService persistenceService;

    @Autowired
    private RequestSequenceRepository requestSequenceRepository;

    @Test
    void saveDNATest() {
        requestSequenceRepository.deleteAll();
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        persistenceService.saveDNA(sequenceDNAModel, true);
        RequestSequence requestSequence = requestSequenceRepository.findBySequence(dna).get();
        assertEquals(Arrays.toString(dna), Arrays.toString(requestSequence.getSequence()));
    }

    @Test
    void isSavedDNANullTest() {
        String[] dna = {"ATGTGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        Boolean res = persistenceService.isMutantSavedDNA(dna);
        assertNull(res);
    }

    @Test
    void isSavedDNATrueTest() {
        requestSequenceRepository.deleteAll();
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        persistenceService.saveDNA(sequenceDNAModel, true);
        Boolean res = persistenceService.isMutantSavedDNA(dna);
        assertTrue(res);
    }

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

    @Test
    void getStatNoDataTest() {
        requestSequenceRepository.deleteAll();
        StatModel stat = persistenceService.getStat();
        assertEquals(0, stat.getRatio());
        assertEquals(0, stat.getCountHumanDNA());
        assertEquals(0, stat.getCountMutantDNA());
    }

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