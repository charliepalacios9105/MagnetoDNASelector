package com.camp.magnetodnaselector.persitence.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("RequestSequences")
@Data
@Builder(toBuilder = true)
public class RequestSequence {
    private String[] sequence;
    private boolean mutant;

}
