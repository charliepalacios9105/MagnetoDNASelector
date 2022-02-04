package com.camp.magnetodnaselector.domain.usecase;

import com.camp.magnetodnaselector.domain.exception.InvalidDNAException;
import com.camp.magnetodnaselector.domain.model.SequenceDNAModel;
import com.camp.magnetodnaselector.domain.model.StatModel;
import com.camp.magnetodnaselector.domain.model.gateway.SequenceDNARepository;
import com.camp.magnetodnaselector.domain.model.gateway.StatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SequenceDNAUseCaseTest {

    @Mock
    private StatRepository statRepository;

    @Mock
    private SequenceDNARepository sequenceDNARepository;

    @InjectMocks
    private SequenceDNAUseCase sequenceDNAUseCase;


    @Test
    void validDNASequenceLessThanMinimumAllowedTest() {
        String[] dna = {"AGGCGAG", "CTGTGGA", "TTAAGTT"};
        String msg = "The length of the DNA string is less than the minimum allowed";
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        when(sequenceDNARepository.isMutantSavedDNA(dna)).thenReturn(null);

        Exception exception = assertThrows(InvalidDNAException.class, () -> {
            sequenceDNAUseCase.isMutant(sequenceDNAModel);
        });
        assertInstanceOf(InvalidDNAException.class, exception);
        assertEquals(msg, exception.getMessage());
    }

    @Test
    void validDNASequenceLengthIndividualStringDifferentDNAStringTest() {
        String[] dna = {"AGGC", "CTGTA", "TTAA", "TGAA"};
        String msg = "The length of an individual string is different than the DNA string";
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        when(sequenceDNARepository.isMutantSavedDNA(dna)).thenReturn(null);

        Exception exception = assertThrows(InvalidDNAException.class, () -> {
            sequenceDNAUseCase.isMutant(sequenceDNAModel);
        });
        assertInstanceOf(InvalidDNAException.class, exception);
        assertEquals(msg, exception.getMessage());
    }

    @Test
    void validDNASequenceHasInvalidCharactersTest() {
        String[] dna = {"AGGCA", "CCTTA", "TTATA", "1qWAQ", "TGAGA"};
        String msg = "The individual string has illegal characters";
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        when(sequenceDNARepository.isMutantSavedDNA(dna)).thenReturn(null);

        Exception exception = assertThrows(InvalidDNAException.class, () -> {
            sequenceDNAUseCase.isMutant(sequenceDNAModel);
        });
        assertInstanceOf(InvalidDNAException.class, exception);
        assertEquals(msg, exception.getMessage());
    }

    @Test
    void validDNASequenceValidResSavedDNATest() throws InvalidDNAException {
        String[] dna = {"GGGGA", "CCTTA", "TAAAA", "ATGTG", "TGAGA"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        when(sequenceDNARepository.isMutantSavedDNA(dna)).thenReturn(false);
        Boolean res = sequenceDNAUseCase.isMutant(sequenceDNAModel);
        assertFalse(res);
    }

    @Test
    void validDNASequenceHorizontalValidTest() throws InvalidDNAException {
        String[] dna = {"GGGGA", "CCTTA", "TAAAA", "ATGTG", "TGAGA"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        when(sequenceDNARepository.isMutantSavedDNA(dna)).thenReturn(null);
        Boolean res = sequenceDNAUseCase.isMutant(sequenceDNAModel);
        assertTrue(res);
    }

    @Test
    void validDNASequenceVerticaValidTest() throws InvalidDNAException {
        String[] dna = {"GCTAA", "GCTTA", "GACAA", "GTGTA", "TGAGA"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        when(sequenceDNARepository.isMutantSavedDNA(dna)).thenReturn(null);
        Boolean res = sequenceDNAUseCase.isMutant(sequenceDNAModel);
        assertTrue(res);
    }

    @Test
    void validDNASequenceDiagonalValidTest() throws InvalidDNAException {
        String[] dna = {"GCTGA", "AGGTC", "CGGAC", "GTGGA", "TGAGA"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        when(sequenceDNARepository.isMutantSavedDNA(dna)).thenReturn(null);
        Boolean res = sequenceDNAUseCase.isMutant(sequenceDNAModel);
        assertTrue(res);
    }

    @Test
    void validDNASequenceValidTest() throws InvalidDNAException {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        when(sequenceDNARepository.isMutantSavedDNA(dna)).thenReturn(null);
        Boolean res = sequenceDNAUseCase.isMutant(sequenceDNAModel);
        assertTrue(res);
    }

    @Test
    void validDNASequenceBigTest() throws InvalidDNAException {
        String[] dna = {"AATTAATTAA", "CTCTCTCTCT", "CGCGCGCGCG", "TCTCTCTCTC", "GAGAGAGAGA", "AATTAATTAA", "CTCTCTCTCT", "CGCGCGCGCG", "TCTCTCTCTC", "GAGAGAGAGA"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        when(sequenceDNARepository.isMutantSavedDNA(dna)).thenReturn(null);
        Boolean res = sequenceDNAUseCase.isMutant(sequenceDNAModel);
        assertFalse(res);
    }

    @Test
    void returnStatModelTest() {
        StatModel statModel = StatModel.builder()
                .countHumanDNA(3)
                .countMutantDNA(2)
                .ratio(3)
                .build();
        when(statRepository.getStat()).thenReturn(statModel);
        assertEquals(statModel, sequenceDNAUseCase.getStat());
    }

}