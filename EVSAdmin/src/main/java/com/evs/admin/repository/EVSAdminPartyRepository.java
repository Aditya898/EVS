package com.evs.admin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evs.admin.model.EVSAdminParty;
/*
 * @author aditaitkar
 */

@Repository
public interface EVSAdminPartyRepository extends JpaRepository<EVSAdminParty, Long> {
    Optional<EVSAdminParty> findByPartyCode(String partyCode);
    
}
