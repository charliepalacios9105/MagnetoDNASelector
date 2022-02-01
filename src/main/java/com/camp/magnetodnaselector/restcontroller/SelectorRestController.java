package com.camp.magnetodnaselector.restcontroller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("selector")
public class SelectorRestController {


    @GetMapping("/health")
    public String getHealthMsg(){
        return "The DNA selector is online";
    }
}
