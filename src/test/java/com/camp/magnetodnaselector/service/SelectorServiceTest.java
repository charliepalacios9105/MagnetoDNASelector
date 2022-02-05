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

/**
 * Pruebas para la clase {@link SelectorService}
 * Se anota con @SpringBootTest para crear un contexto de prueba
 * de SpringBoot para la clase especifica
 *
 * @author Carlos Alberto Manrique Palacios
 */
@SpringBootTest(classes = {SelectorService.class})
class SelectorServiceTest {

    @MockBean
    private SequenceDNAUseCase sequenceDNAUseCase;

    @Autowired
    private SelectorService selectorService;

    /**
     * Varifica el correcto llamado al objecto inyectado por el contenedor
     * sequenceDNAUseCase que para la prueba sera un Mock
     * <p>
     * En este caso realiza la ejecucion del metodo isMutant y
     * se simula una ejecucion normal con un respuesta true desde el Mock
     *
     * @throws InvalidDNAException
     */
    @Test
    void isMutantTest() throws InvalidDNAException {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        when(sequenceDNAUseCase.isMutant(sequenceDNAModel)).thenReturn(true);
        assertTrue(selectorService.isMutant(sequenceDNAModel));
        verify(sequenceDNAUseCase).isMutant(sequenceDNAModel);
    }

    /**
     * Varifica el correcto llamado al objecto inyectado por el contenedor
     * sequenceDNAUseCase que para la prueba sera un Mock
     * <p>
     * En este caso realiza la ejecucion del metodo isMutant y
     * se simula que se lanza una excepcion  el Mock
     *
     * @throws InvalidDNAException
     */
    @Test
    void isMutantThrowExceptionTest() throws InvalidDNAException {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        when(sequenceDNAUseCase.isMutant(sequenceDNAModel)).thenThrow(new InvalidDNAException("InvalidDNAException"));
        assertThrows(InvalidDNAException.class, () -> selectorService.isMutant(sequenceDNAModel));
        verify(sequenceDNAUseCase).isMutant(sequenceDNAModel);
    }

    /**
     * Varifica el correcto llamado al objecto inyectado por el contenedor
     * sequenceDNAUseCase que para la prueba sera un Mock
     * <p>
     * En este caso realiza la ejecucion del metodo findStats y
     * se simula una ejecucion normal con una respuesta del un objeto de la
     * clase {@link StatModel} desde el Mock
     */
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