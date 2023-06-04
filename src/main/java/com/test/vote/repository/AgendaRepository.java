package com.test.vote.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.test.vote.domain.Agenda;

@Repository
public interface AgendaRepository extends MongoRepository<Agenda, String> {

}
