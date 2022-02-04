package com.camp.magnetodnaselector.config.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ErrorModel {

    private long timestamp;
    private String type;
    private String message;
}
