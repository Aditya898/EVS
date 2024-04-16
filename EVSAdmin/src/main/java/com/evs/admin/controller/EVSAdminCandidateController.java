package com.evs.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.evs.admin.dto.EVSAdminCandidateDto;
import com.evs.admin.exception.CandidateCodeMisMatchException;
import com.evs.admin.exception.NoCandidateFoundException;
import com.evs.admin.serviceimpl.EVSAdminCandidateServiceImpl;



@RestController
@RequestMapping("admin/candidate")
public class EVSAdminCandidateController {

	@Autowired
	private EVSAdminCandidateServiceImpl evsAdminCandidateServiceImpl;
	
	@PostMapping("saveCandidate")
	public ResponseEntity<String> addCandidate(@RequestBody EVSAdminCandidateDto evsAdminCandidateDto)
	{
		return evsAdminCandidateServiceImpl.saveCandidate(evsAdminCandidateDto);
	}
	@DeleteMapping("deleteCandidate/{candidateId}")
	public ResponseEntity<String> deleteCandidate(@PathVariable String candidateId) throws NoCandidateFoundException
	{
		return evsAdminCandidateServiceImpl.deleteCandidate(candidateId);
	}
	@GetMapping("getCandidate/{candidateId}")
	public ResponseEntity<EVSAdminCandidateDto> getCandidate(@PathVariable String candidateId) throws NoCandidateFoundException
	{
		return evsAdminCandidateServiceImpl.getCandidate(candidateId);
	}
	@GetMapping("getAllCandidates")
	public ResponseEntity<List<EVSAdminCandidateDto>> getAllCandidates()
	{
		return evsAdminCandidateServiceImpl.getAllCandidates();
	}
	@PutMapping("updateCandidate/{candidateId}")
	public ResponseEntity<String> updateCandidate(@RequestBody EVSAdminCandidateDto evsAdminCandidateDto, @PathVariable String candidateId) throws NoCandidateFoundException, CandidateCodeMisMatchException
	{
		return evsAdminCandidateServiceImpl.updateCandidate(evsAdminCandidateDto, candidateId);
	}
	
}
