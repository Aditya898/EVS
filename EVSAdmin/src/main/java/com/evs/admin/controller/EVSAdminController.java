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

import com.evs.admin.dto.EVSAdminElectionDto;
import com.evs.admin.exception.ElectionCodeAlreadyPresent;
import com.evs.admin.exception.ElectionCodeMisMatchException;
import com.evs.admin.exception.NoElectionDetailsFoundException;
import com.evs.admin.exception.NoPartyDetailsFoundException;
import com.evs.admin.serviceimpl.EVSAdminElectionServiceImpl;


/*
 * @author ymarni
 */
@RestController
@RequestMapping("admin/election")
public class EVSAdminController {
	
	@Autowired
	private EVSAdminElectionServiceImpl evsAdminElectionServiceImpl;
	
	@GetMapping("sayWelcome")
	public String sayWelcome()
	{
		return "Welcome to the Electronic Voting System";
	}
	
	@PostMapping("addElection")
	public ResponseEntity<String> addElection(@RequestBody EVSAdminElectionDto evsAdminElectionDto) throws ElectionCodeAlreadyPresent, NoPartyDetailsFoundException
	{
		return evsAdminElectionServiceImpl.addElection(evsAdminElectionDto);
	}
	
	@DeleteMapping("deleteElection/{electionCode}")
	public ResponseEntity<String> deleteElection(@PathVariable String electionCode) throws NoElectionDetailsFoundException
	{
		return evsAdminElectionServiceImpl.deleteElection(electionCode);
	}
	
	@GetMapping("getElection/{electionCode}")
	public ResponseEntity<EVSAdminElectionDto> getElectionBycode(@PathVariable String electionCode) throws NoElectionDetailsFoundException
	{
		return evsAdminElectionServiceImpl.getElectionByCode(electionCode);
	}
	
	@GetMapping("getAllElections")
	public ResponseEntity<List<EVSAdminElectionDto>> getAllElections()
	{
		return evsAdminElectionServiceImpl.getAllElections();
	}
	
	@PutMapping("updateElection/{electionCode}")
	public ResponseEntity<String> updateElection(@RequestBody EVSAdminElectionDto evsAdminElectionDto,@PathVariable String electionCode) throws NoElectionDetailsFoundException, ElectionCodeMisMatchException, NoPartyDetailsFoundException
	{
		return evsAdminElectionServiceImpl.updateElection(evsAdminElectionDto,electionCode);
	}
	

}
