package com.camp.magnetodnaselector.persitence.repository;

import com.camp.magnetodnaselector.persitence.entity.RequestSequence;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestSequenceRepository extends MongoRepository<RequestSequence, String> {


    Optional<RequestSequence> findBySequence(String[] sequence);

}
