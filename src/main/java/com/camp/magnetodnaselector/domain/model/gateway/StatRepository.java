package com.camp.magnetodnaselector.domain.model.gateway;

import com.camp.magnetodnaselector.domain.model.StatModel;

public interface StatRepository {

    StatModel getStat();

    void calRatio(StatModel statModel);

}
