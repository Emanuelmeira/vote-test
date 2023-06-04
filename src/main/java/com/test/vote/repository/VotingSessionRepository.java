package com.test.vote.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.test.vote.domain.VotingSession;

@Repository
public interface VotingSessionRepository extends MongoRepository<VotingSession, String> {

}
