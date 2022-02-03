package com.camp.magnetodnaselector.domain.model.gateway;

import com.camp.magnetodnaselector.domain.model.SequenceDNAModel;

public interface SequenceDNARepository {

    Boolean isMutantSavedDNA(String[] dna);

    void saveDNA(SequenceDNAModel sequenceDNAModel, boolean mutant);

}
