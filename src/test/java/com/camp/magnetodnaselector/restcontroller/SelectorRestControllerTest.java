package com.camp.magnetodnaselector.restcontroller;

import com.camp.magnetodnaselector.config.ExceptionHandlerConfig;
import com.camp.magnetodnaselector.domain.model.SequenceDNAModel;
import com.camp.magnetodnaselector.domain.model.StatModel;
import com.camp.magnetodnaselector.service.SelectorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas para la clase {@link SelectorRestController}
 * Se anota con @WebMvcTest para crear un contexto de prueba
 * de SpringBoot para la clase que esta anotada con @RestController
 * <p>
 * Se importa la clase de configuracion {@link ExceptionHandlerConfig} para
 * hacer pruebas del lanzamiento de excepciones
 *
 * @author Carlos Alberto Manrique Palacios
 */
@WebMvcTest(SelectorRestController.class)
@Import(ExceptionHandlerConfig.class)
class SelectorRestControllerTest {

    /**
     * Objeto que permite la simulacion de peticiones http
     */
    @Autowired
    private MockMvc mvc;

    @MockBean
    private SelectorService selectorService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
    }

    /**
     * Prueba para el serivicio de verificación del aplicativo
     *
     * @throws Exception
     */
    @Test
    void getHealthMsgTest() throws Exception {
        mvc.perform(get("/selector/health"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("The DNA selector is online"));
    }

    /**
     * Prueba para el servicio get de estadisticas
     * Se simula una ejecucion normal del servicio
     *
     * @throws Exception
     */
    @Test
    void getStatTest() throws Exception {
        StatModel statModel = StatModel.builder()
                .countHumanDNA(40)
                .countMutantDNA(100)
                .build();
        when(selectorService.findStats()).thenReturn(statModel);
        mvc.perform(get("/selector/stats"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(statModel)));
    }

    /**
     * Prueba para el servicio post de la funcionalidad isMutant
     * Se simula una ejecucion normal del servicio con un respuesta true del
     * Mock del servicio, por lo que el codigo esperado de la respuesta sera un 200
     *
     * @throws Exception
     */
    @Test
    void isMutantTrueTest() throws Exception {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();

        when(selectorService.isMutant(sequenceDNAModel)).thenReturn(true);
        mvc.perform(post("/selector/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sequenceDNAModel)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(emptyString()));
    }

    /**
     * Prueba para el servicio post de la funcionalidad isMutant
     * Se simula una ejecucion normal del servicio con un respuesta false del
     * Mock del servicio, por lo que el codigo esperado de la respuesta sera un 403
     *
     * @throws Exception
     */
    @Test
    void isMutantFalseTest() throws Exception {
        String[] dna = {"ATGGAA", "AGGTGC", "CTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();

        when(selectorService.isMutant(sequenceDNAModel)).thenReturn(false);
        mvc.perform(post("/selector/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sequenceDNAModel)))
                .andDo(print()).andExpect(status().isForbidden())
                .andExpect(content().string(emptyString()));
    }

    /**
     * Prueba para el servicio post de la funcionalidad isMutant
     * Se simula una ejecucion donde no se ingreso nada en el body de la solicitud
     * lo que lanza una HttpMessageNotReadableException por lo que el codigo
     * esperado de las respuesta sera un 400
     *
     * @throws Exception
     */
    @Test
    void isMutantExceptionTest() throws Exception {
        when(selectorService.isMutant(any())).thenReturn(false);
        mvc.perform(post("/selector/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(("HttpMessageNotReadableException"))));
    }

    /**
     * Prueba para el servicio post de la funcionalidad isMutant
     * Se simula una ejecucion donde al ejecutar la funcionalidad isMutant
     * se lanza una excepcion no esperada por lo que el codigo
     * esperado de las respuesta sera un 500
     *
     * @throws Exception
     */
    @Test
    void isMutantErrorExceptionTest() throws Exception {
        String[] dna = {"ATGGAA", "AGGTGC", "CTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        SequenceDNAModel sequenceDNAModel = SequenceDNAModel.builder().dna(dna).build();
        when(selectorService.isMutant(any())).then(as -> 1 / 0 == 1);
        mvc.perform(post("/selector/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sequenceDNAModel)))
                .andDo(print()).andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString(("ArithmeticException"))));
    }

}