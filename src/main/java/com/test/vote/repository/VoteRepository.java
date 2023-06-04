package com.test.vote.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.test.vote.domain.Vote;

@Repository
public interface VoteRepository extends MongoRepository<Vote, String> {
    Optional<Vote> findFirstByIdAssociateAndIdAgenda(String idAssociate, String idAgenda);
}
