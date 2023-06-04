package com.test.vote.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.test.vote.domain.Vote;

@Repository
public class VoteRepositoryImpl {

	private final MongoOperations mongoOperations;
	@Autowired
	public VoteRepositoryImpl(final MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}

	public long countByFields(String field1Value, String field2Value) {
		Query query = new Query();
		query.addCriteria(Criteria.where("idAgenda").is(field1Value).and("voteEnum").is(field2Value));

		return mongoOperations.count(query, Vote.class);
	}
}
