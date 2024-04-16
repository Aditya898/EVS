package com.evs.admin.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.evs.admin.dto.EVSAdminCandidateDto;
import com.evs.admin.exception.CandidateCodeMisMatchException;
import com.evs.admin.exception.NoCandidateFoundException;


/*
 * @author ymarni
 */

public interface EVSAdminCandidateService {

	ResponseEntity<String> saveCandidate(EVSAdminCandidateDto evsAdminCandidateDto);
	ResponseEntity<String> updateCandidate(EVSAdminCandidateDto evsAdminCandidateDto,String candidateId) throws NoCandidateFoundException, CandidateCodeMisMatchException;
	ResponseEntity<String> deleteCandidate(String candidateId) throws NoCandidateFoundException;
	ResponseEntity<EVSAdminCandidateDto> getCandidate(String candidateId) throws NoCandidateFoundException;
	ResponseEntity<List<EVSAdminCandidateDto>> getAllCandidates();
//	ResponseEntity<List<EVSAdminCandidateDto>> getAllCandidatesByPartyCode(String partyCode);
}
