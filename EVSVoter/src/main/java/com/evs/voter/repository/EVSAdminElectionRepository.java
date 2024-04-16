package com.evs.voter.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evs.voter.model.EVSAdminElection;

/*
 * @author ymarni
 */
@Repository
public interface EVSAdminElectionRepository extends JpaRepository<EVSAdminElection, Long> {
	Optional<EVSAdminElection> findByElectionCode(String electionCode);
}
