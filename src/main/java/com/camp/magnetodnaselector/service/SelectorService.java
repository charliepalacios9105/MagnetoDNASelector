package com.camp.magnetodnaselector.service;

import com.camp.magnetodnaselector.domain.exception.InvalidDNAException;
import com.camp.magnetodnaselector.domain.model.SequenceDNAModel;
import com.camp.magnetodnaselector.domain.model.StatModel;
import com.camp.magnetodnaselector.domain.usecase.SequenceDNAUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SelectorService {

    private final SequenceDNAUseCase sequenceDNAUseCase;

    public boolean isMutant(SequenceDNAModel sequenceDNAModel) throws InvalidDNAException {
        return sequenceDNAUseCase.isMutant(sequenceDNAModel);
    }

    public StatModel findStats() {
        return sequenceDNAUseCase.getStat();
    }
}
