package com.camp.magnetodnaselector.persitence.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AggResultDNACount {

    private boolean mutant;
    private long total;

}
