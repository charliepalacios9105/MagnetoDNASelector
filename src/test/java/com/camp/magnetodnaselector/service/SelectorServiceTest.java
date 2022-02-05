package com.camp.magnetodnaselector.service;

import com.camp.magnetodnaselector.domain.exception.InvalidDNAException;
import com.camp.magnetodnaselector.domain.model.SequenceDNAModel;
import com.camp.magnetodnaselector.domain.model.StatModel;
import com.camp.magnetodnaselector.domain.usecase.SequenceDNAUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {SelectorService.class})
class SelectorServiceTest {

    @MockBean
    private SequenceDNAUseCase sequenceDNAUseCase;

    @Autowired
    private SelectorService selectorService;

    @Test
    void isMutantTest() throws InvalidDNAException {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        when(sequenceDNAUseCase.isMutant(sequenceDNAModel)).thenReturn(true);
        assertTrue(selectorService.isMutant(sequenceDNAModel));
        verify(sequenceDNAUseCase).isMutant(sequenceDNAModel);
    }

    @Test
    void isMutantThrowExceptionTest() throws InvalidDNAException {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        when(sequenceDNAUseCase.isMutant(sequenceDNAModel)).thenThrow(new InvalidDNAException("InvalidDNAException"));
        assertThrows(InvalidDNAException.class, () -> selectorService.isMutant(sequenceDNAModel));
        verify(sequenceDNAUseCase).isMutant(sequenceDNAModel);
    }

    @Test
    void isFindTest() {
        StatModel statModel = StatModel.builder()
                .countHumanDNA(3)
                .countMutantDNA(2)
                .ratio(3)
                .build();
        when(selectorService.findStats()).thenReturn(statModel);
        assertEquals(statModel, selectorService.findStats());
        verify(sequenceDNAUseCase).getStat();
    }

}