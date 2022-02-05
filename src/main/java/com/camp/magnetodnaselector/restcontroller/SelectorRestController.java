package com.camp.magnetodnaselector.restcontroller;


import com.camp.magnetodnaselector.domain.exception.InvalidDNAException;
import com.camp.magnetodnaselector.domain.model.SequenceDNAModel;
import com.camp.magnetodnaselector.domain.model.StatModel;
import com.camp.magnetodnaselector.service.SelectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Clase controladora de servicios REST donde se encuentra la exposicion de las
 * funcionalidades solicitadas para ser consumidas a traves de metodos HTTP
 *
 * @author Carlos Alberto Manrique Palacios
 */
@RestController
@RequestMapping("selector")
@RequiredArgsConstructor
public class SelectorRestController {

    /**
     * Objecto de llamado de funcionalidades
     */
    private final SelectorService selectorService;

    /**
     * Endpoint de prueba para verificar si el aplicativo
     * esta en linea. Se debe consumir desde el navegador con
     * selector/health y mostrara el texto retornado
     *
     * @return texto ques se mostrara en el navegador al hacer la peticion
     */
    @GetMapping("/health")
    public String getHealthMsg() {
        return "The DNA selector is online";
    }

    /**
     * Metodo que expone por POST la funcionalidad de isMutant,
     * recibe por parametro el objeto modelo de negocio que contiene la sequencia
     * que se debe evaluar y retorna un cuerpo vacio.
     * <p>
     * El que determina si la cadena pertenece o no a un mutante es el codigo
     * de estado de la pecicion el cual segun las especificaciones es 200 para true
     * y 403 para false
     *
     * @param sequenceDNAModel Objeto de negocio contenedor del vector de strings que se debe evaluar
     * @return Objecto de la clases {@link ResponseEntity} con el codigo correspondiente segun
     * la validacion de la cadena
     * @throws InvalidDNAException Excepcion lanzada desde la funcionalidad
     *                             isMutant en caso se exisitir alguna inconsistencia en en vector
     */
    @PostMapping(value = "/mutant")
    public ResponseEntity<Object> isMutant(@RequestBody SequenceDNAModel sequenceDNAModel) throws InvalidDNAException {
        return selectorService.isMutant(sequenceDNAModel) ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    /**
     * Metodo que expone por POST la funcionalidad de getStat,
     * retorna un objecto de la clases {@link ResponseEntity} que contiene el
     * objeto de negocio de la clase {@link StatModel} el cual es representado en
     * formato JSON al usuario
     *
     * @return Objecto de la clases {@link ResponseEntity} con el objeto de negocio
     * que tiene los conteos solicitados en las especificaiones del problema
     */
    @GetMapping(value = "/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatModel> getStat() {
        return new ResponseEntity<>(selectorService.findStats(), HttpStatus.OK);
    }

}
