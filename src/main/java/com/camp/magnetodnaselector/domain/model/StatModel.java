package com.camp.magnetodnaselector.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class StatModel {
    @JsonProperty("count_mutant_dna")
    private long countMutantDNA;
    @JsonProperty("count_human_dna")
    private long countHumanDNA;
    @JsonProperty("ratio")
    private double ratio;
}
