package com.camp.magnetodnaselector.restcontroller;


import com.camp.magnetodnaselector.domain.model.SequenceDNAModel;
import com.camp.magnetodnaselector.service.SelectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("selector")
@RequiredArgsConstructor
public class SelectorRestController {

    private final SelectorService selectorService;

    @GetMapping("/health")
    public String getHealthMsg() {
        return "The DNA selector is online";
    }

    @PostMapping(value = "/mutant")
    public ResponseEntity isMutant(@RequestBody SequenceDNAModel sequenceDNAModel) {
        return selectorService.isMutant(sequenceDNAModel) ?
                new ResponseEntity(HttpStatus.OK) :
                new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    @GetMapping(value = "/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getStat() {
        return new ResponseEntity(selectorService.findStats(), HttpStatus.OK);
    }

}
