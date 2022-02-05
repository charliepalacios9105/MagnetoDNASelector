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

/**
 * Clase de prueba para la clase de negocio {@link SequenceDNAUseCase}
 *
 * @author Carlos Alberto Manrique Palacios
 */
@ExtendWith(MockitoExtension.class)
class SequenceDNAUseCaseTest {

    @Mock
    private StatRepository statRepository;

    @Mock
    private SequenceDNARepository sequenceDNARepository;

    @InjectMocks
    private SequenceDNAUseCase sequenceDNAUseCase;


    /**
     * Prueba para la excepcion lanzada cuando la longitud del vector es menor al minimo
     * permitido
     */
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

    /**
     * Prueba para la excepcion lanzada cuando la longitud de algun elemento del vector
     * es digerente al del vector
     */
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

    /**
     * Prueba para la excepcion lanzada cuando existen caracteres no permitidos en algun
     * elemento del vector
     */
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

    /**
     * Prueba para cuando existe respuesta desde la funcionalidad de
     * busqueda de las cadenas
     *
     * @throws InvalidDNAException
     */
    @Test
    void validDNASequenceValidResSavedDNATest() throws InvalidDNAException {
        String[] dna = {"GGGGA", "CCTTA", "TAAAA", "ATGTG", "TGAGA"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        when(sequenceDNARepository.isMutantSavedDNA(dna)).thenReturn(false);
        Boolean res = sequenceDNAUseCase.isMutant(sequenceDNAModel);
        assertFalse(res);
    }

    /**
     * Prueba para cuando se ecuentran las cadenas en la dimension horizontal
     *
     * @throws InvalidDNAException
     */
    @Test
    void validDNASequenceHorizontalValidTest() throws InvalidDNAException {
        String[] dna = {"GGGGA", "CCTTA", "TAAAA", "ATGTG", "TGAGA"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        when(sequenceDNARepository.isMutantSavedDNA(dna)).thenReturn(null);
        Boolean res = sequenceDNAUseCase.isMutant(sequenceDNAModel);
        assertTrue(res);
    }

    /**
     * Prueba para cuando se ecuentran las cadenas en la dimension vertical
     *
     * @throws InvalidDNAException
     */
    @Test
    void validDNASequenceVerticaValidTest() throws InvalidDNAException {
        String[] dna = {"GCTAA", "GCTTA", "GACAA", "GTGTA", "TGAGA"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        when(sequenceDNARepository.isMutantSavedDNA(dna)).thenReturn(null);
        Boolean res = sequenceDNAUseCase.isMutant(sequenceDNAModel);
        assertTrue(res);
    }

    /**
     * Prueba para cuando se ecuentran las cadenas en la dimension diagonal
     *
     * @throws InvalidDNAException
     */
    @Test
    void validDNASequenceDiagonalValidTest() throws InvalidDNAException {
        String[] dna = {"GCTGA", "AGGTC", "CGGAC", "GTGGA", "TGAGA"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        when(sequenceDNARepository.isMutantSavedDNA(dna)).thenReturn(null);
        Boolean res = sequenceDNAUseCase.isMutant(sequenceDNAModel);
        assertTrue(res);
    }

    /**
     * Prueba para un vector que cumple la condicion del mutante
     *
     * @throws InvalidDNAException
     */
    @Test
    void validDNASequenceValidTest() throws InvalidDNAException {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        when(sequenceDNARepository.isMutantSavedDNA(dna)).thenReturn(null);
        Boolean res = sequenceDNAUseCase.isMutant(sequenceDNAModel);
        assertTrue(res);
    }

    /**
     * Prueba para un vector que no cumple la condicion del mutante
     *
     * @throws InvalidDNAException
     */
    @Test
    void validDNASequenceBigTest() throws InvalidDNAException {
        String[] dna = {"AATTAATTAA", "CTCTCTCTCT", "CGCGCGCGCG", "TCTCTCTCTC", "GAGAGAGAGA", "AATTAATTAA", "CTCTCTCTCT", "CGCGCGCGCG", "TCTCTCTCTC", "GAGAGAGAGA"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        when(sequenceDNARepository.isMutantSavedDNA(dna)).thenReturn(null);
        Boolean res = sequenceDNAUseCase.isMutant(sequenceDNAModel);
        assertFalse(res);
    }

    /**
     * Prueba verificar el retorno del metodo getStat()
     */
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