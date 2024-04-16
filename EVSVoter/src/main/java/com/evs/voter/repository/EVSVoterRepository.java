package com.evs.voter.repository;


import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.evs.voter.model.EVSVoter;

@Repository
public interface EVSVoterRepository extends MongoRepository<EVSVoter, String> {

	Optional<EVSVoter> findByUserName(String userName);

	Optional<EVSVoter> findByVoterId(String voterId);
	
}
