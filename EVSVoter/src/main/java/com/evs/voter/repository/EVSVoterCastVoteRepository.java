package com.evs.voter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evs.voter.model.EVSVoterCastVote;

@Repository
public interface EVSVoterCastVoteRepository extends JpaRepository<EVSVoterCastVote, Long> {

	Optional<EVSVoterCastVote> findByVoterIdAndElectionCode(String voterId, String electionCode);

	EVSVoterCastVote findByElectionCodeAndPartyCode(String electionCode, String partyCode);

	EVSVoterCastVote findByElectionCodeAndPartyCodeAndVoterId(String electionCode, String partyCode, String voterId);

	List<EVSVoterCastVote> findAllByElectionCodeAndPartyCode(String electionCode, String partyCode);

}
