package com.evs.admin.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.evs.admin.dto.EVSAdminCandidateDto;
import com.evs.admin.exception.CandidateAlreadyPresentException;
import com.evs.admin.exception.CandidateCodeMisMatchException;
import com.evs.admin.exception.NoCandidateFoundException;
import com.evs.admin.model.EVSAdminCandidate;
import com.evs.admin.model.EVSAdminParty;
import com.evs.admin.repository.EVSAdminCandidateRepository;
import com.evs.admin.repository.EVSAdminPartyRepository;
import com.evs.admin.service.EVSAdminCandidateService;

import jakarta.persistence.EntityNotFoundException;

/*
 * @author ymarni
 */

@Service
public class EVSAdminCandidateServiceImpl implements EVSAdminCandidateService {
	
	@Autowired
	private EVSAdminCandidateRepository evsAdminCandidateRepository;
	@Autowired
	private EVSAdminPartyRepository evsAdminPartyRepository;
	

	@Override
	public ResponseEntity<String> saveCandidate(EVSAdminCandidateDto evsAdminCandidateDto) {
		Optional<EVSAdminCandidate> optional = evsAdminCandidateRepository.findByCandidateId(evsAdminCandidateDto.getCandidateId());
		
		if(optional.isEmpty())
		{
			EVSAdminCandidate evsAdminCandidate = new EVSAdminCandidate();
			evsAdminCandidate.setCandidateId(evsAdminCandidateDto.getCandidateId());
			evsAdminCandidate.setName(evsAdminCandidateDto.getName());
			evsAdminCandidate.setAddress(evsAdminCandidateDto.getAddress());
			evsAdminCandidate.setAge(evsAdminCandidateDto.getAge());
			evsAdminCandidate.setGender(evsAdminCandidateDto.getGender());
			evsAdminCandidate.setConstituency(evsAdminCandidateDto.getConstituency());
			evsAdminCandidate.setContactNo(evsAdminCandidateDto.getContactNo());
			EVSAdminParty evsAdminParty = evsAdminPartyRepository.findByPartyCode((evsAdminCandidateDto.getEvsAdminParty()).getPartyCode())
			           .orElseThrow(() -> new EntityNotFoundException("EVSAdminParty not found with code: " + ( evsAdminCandidateDto.getEvsAdminParty()).getPartyCode()));
			 
			evsAdminCandidate.setEvsAdminParty(evsAdminParty);
			evsAdminCandidateRepository.save(evsAdminCandidate);
			return new ResponseEntity<String>(String.format("Candidate with the id %s has been successfully saved", evsAdminCandidateDto.getCandidateId()),HttpStatus.OK);
		}
		if (optional.isPresent()) {
			throw new CandidateAlreadyPresentException(String.format("Candidate with the id %s is already registered with us, please try to check", evsAdminCandidateDto.getCandidateId()));
		}
		return new ResponseEntity<>("Something went wrong while performing the operation, please come again after some time",HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateCandidate(EVSAdminCandidateDto evsAdminCandidateDto, String candidateId) throws NoCandidateFoundException, CandidateCodeMisMatchException {
		Optional<EVSAdminCandidate> optional = evsAdminCandidateRepository.findByCandidateId(candidateId);
		if(optional.isEmpty())
		{
			throw new NoCandidateFoundException(String.format("Couldn't find any candidate to update with the candidate id %s", candidateId));
		}
		if(!optional.get().getCandidateId().equals(evsAdminCandidateDto.getCandidateId()))
		{
			throw new CandidateCodeMisMatchException(String.format("Candidate code %s is not matching with the candidate code %s you are trying to update, please make sure you are updating the correct candidate", evsAdminCandidateDto.getCandidateId(),optional.get().getCandidateId()));
		}
		EVSAdminCandidate evsAdminCandidate = new EVSAdminCandidate();
		evsAdminCandidate.setId(optional.get().getId());
		evsAdminCandidate.setCandidateId(optional.get().getCandidateId());
		evsAdminCandidate.setName(optional.get().getName());
		evsAdminCandidate.setAddress(optional.get().getAddress());
		evsAdminCandidate.setAge(optional.get().getAge());
		evsAdminCandidate.setGender(optional.get().getGender());
		evsAdminCandidate.setConstituency(optional.get().getConstituency());
		evsAdminCandidate.setContactNo(optional.get().getContactNo());
		EVSAdminParty evsAdminParty = evsAdminPartyRepository.findByPartyCode((evsAdminCandidateDto.getEvsAdminParty()).getPartyCode())
		           .orElseThrow(() -> new EntityNotFoundException("EVSAdminParty not found with code: " + ( evsAdminCandidateDto.getEvsAdminParty()).getPartyCode()));
		evsAdminCandidate.setEvsAdminParty(evsAdminParty);
		evsAdminCandidateRepository.save(evsAdminCandidate);		
		return new ResponseEntity<String>(String.format("Candidate with id %s has been updated successfully", candidateId),HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> deleteCandidate(String candidateId) throws NoCandidateFoundException {
		Optional<EVSAdminCandidate> optional = evsAdminCandidateRepository.findByCandidateId(candidateId);
		if(optional.isEmpty())
		{
			throw new NoCandidateFoundException(String.format("Couldn't find any candidate to delete with the candidate id %s", candidateId));
		}
		optional.get().setEvsAdminParty(null); //setting this as null because we can't delete if we have relations associated
		evsAdminCandidateRepository.delete(optional.get());		
		return new ResponseEntity<>("Successfully deleted the candidate",HttpStatus.OK);
	}

	@Override
	public ResponseEntity<EVSAdminCandidateDto> getCandidate(String candidateId) throws NoCandidateFoundException {
		Optional<EVSAdminCandidate> optional = evsAdminCandidateRepository.findByCandidateId(candidateId);
		if(optional.isEmpty())
		{
			throw new NoCandidateFoundException(String.format("Couldn't find any candidate to delete with the candidate id %s", candidateId));
		}
		EVSAdminCandidateDto evsAdminCandidateDto = new EVSAdminCandidateDto();
		evsAdminCandidateDto.setId(optional.get().getId());
		evsAdminCandidateDto.setCandidateId(optional.get().getCandidateId());
		evsAdminCandidateDto.setName(optional.get().getName());
		evsAdminCandidateDto.setAddress(optional.get().getAddress());
		evsAdminCandidateDto.setAge(optional.get().getAge());
		evsAdminCandidateDto.setGender(optional.get().getGender());
		evsAdminCandidateDto.setConstituency(optional.get().getConstituency());
		evsAdminCandidateDto.setContactNo(optional.get().getContactNo());
		evsAdminCandidateDto.setEvsAdminParty(optional.get().getEvsAdminParty());		
		
		return new ResponseEntity<>(evsAdminCandidateDto,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<EVSAdminCandidateDto>> getAllCandidates() {
		List<EVSAdminCandidate> candidatesListFromDB = evsAdminCandidateRepository.findAll();
		List<EVSAdminCandidateDto> candidatesListToReturn = new ArrayList<>();
		candidatesListFromDB.forEach((candidate)->{
			EVSAdminCandidateDto evsAdminCandidateDto = new EVSAdminCandidateDto(candidate.getId(),
					candidate.getCandidateId(),
					candidate.getName(),
					candidate.getAddress(),
					candidate.getAge(),
					candidate.getGender(),
					candidate.getConstituency(),
					candidate.getContactNo(),
					candidate.getEvsAdminParty());
			candidatesListToReturn.add(evsAdminCandidateDto);
		});
		return ResponseEntity.ok().body(candidatesListToReturn);
	}
}
