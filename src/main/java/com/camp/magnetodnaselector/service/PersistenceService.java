package com.camp.magnetodnaselector.service;

import com.camp.magnetodnaselector.domain.model.SequenceDNAModel;
import com.camp.magnetodnaselector.domain.model.StatModel;
import com.camp.magnetodnaselector.domain.model.gateway.SequenceDNARepository;
import com.camp.magnetodnaselector.domain.model.gateway.StatRepository;
import com.camp.magnetodnaselector.persitence.entity.RequestSequence;
import com.camp.magnetodnaselector.persitence.pojo.AggResultDNACount;
import com.camp.magnetodnaselector.persitence.repository.RequestSequenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@RequiredArgsConstructor
@Service
public class PersistenceService implements SequenceDNARepository, StatRepository {

    private final RequestSequenceRepository requestSequenceRepository;
    private final MongoTemplate mongoTemplate;

    private List<AggResultDNACount> findStats() {
        Aggregation agg = newAggregation(
                group("mutant").count().as("total"),
                project("total").and("mutant").previousOperation()

        );
        AggregationResults<AggResultDNACount> groupResults
                = mongoTemplate.aggregate(agg, RequestSequence.class, AggResultDNACount.class);
        List<AggResultDNACount> result = groupResults.getMappedResults();
        result.stream().collect(
                Collectors.toMap(AggResultDNACount::isMutant, AggResultDNACount::getTotal));
        return result;
    }

    public Boolean isMutantSavedDNA(String[] dna) {
        RequestSequence requestSequence = requestSequenceRepository.findBySequence(dna).orElse(null);
        return requestSequence != null ? requestSequence.isMutant() : null;
    }

    public void saveDNA(SequenceDNAModel sequenceDNAModel, boolean mutant) {
        requestSequenceRepository.save(RequestSequence.builder()
                .sequence(sequenceDNAModel.getDna())
                .mutant(mutant).build());
    }


    @Override
    public StatModel getStat() {
        List<AggResultDNACount> stats = findStats();
        StatModel statModel = new StatModel();
        stats.stream().forEach(ar -> {
            if (ar.isMutant()) {
                statModel.setCountMutantDNA(ar.getTotal());
            } else {
                statModel.setCountHumanDNA(ar.getTotal());
            }
        });
        return statModel;
    }
}
