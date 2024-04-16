package com.evs.admin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evs.admin.model.EVSAdminCandidate;

/*
 * @author ymarni
 */
@Repository
public interface EVSAdminCandidateRepository extends JpaRepository<EVSAdminCandidate, Long> {
	
	Optional<EVSAdminCandidate> findByCandidateId(String candidateId);

	List<EVSAdminCandidate> findByEvsAdminParty_PartyCode(String string);
}
