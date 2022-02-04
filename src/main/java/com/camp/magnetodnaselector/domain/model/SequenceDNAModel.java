package com.camp.magnetodnaselector.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class SequenceDNAModel {

    private String[] dna;

}
