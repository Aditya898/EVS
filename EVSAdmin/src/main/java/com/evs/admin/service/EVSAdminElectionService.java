package com.evs.admin.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.evs.admin.dto.EVSAdminElectionDto;
import com.evs.admin.exception.ElectionCodeAlreadyPresent;
import com.evs.admin.exception.ElectionCodeMisMatchException;
import com.evs.admin.exception.NoElectionDetailsFoundException;
import com.evs.admin.exception.NoPartyDetailsFoundException;

/*
 * @author ymarni
 */
public interface EVSAdminElectionService {
	ResponseEntity<String> addElection(EVSAdminElectionDto evsAdminElectionDto) throws ElectionCodeAlreadyPresent, NoPartyDetailsFoundException;
	ResponseEntity<String> deleteElection(String electionCode) throws NoElectionDetailsFoundException;
	ResponseEntity<EVSAdminElectionDto> getElectionByCode(String electionCode) throws NoElectionDetailsFoundException;
	ResponseEntity<List<EVSAdminElectionDto>> getAllElections();
	ResponseEntity<String> updateElection(EVSAdminElectionDto evsAdminElectionDto, String electionCode) throws NoElectionDetailsFoundException, ElectionCodeMisMatchException, NoPartyDetailsFoundException;
	
}
