package com.evs.admin.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.evs.admin.dto.EVSAdminElectionDto;
import com.evs.admin.exception.ElectionCodeAlreadyPresent;
import com.evs.admin.exception.ElectionCodeMisMatchException;
import com.evs.admin.exception.NoElectionDetailsFoundException;
import com.evs.admin.exception.NoPartyDetailsFoundException;
import com.evs.admin.model.EVSAdminElection;
import com.evs.admin.model.EVSAdminParty;
import com.evs.admin.repository.EVSAdminElectionRepository;
import com.evs.admin.repository.EVSAdminPartyRepository;
import com.evs.admin.service.EVSAdminElectionService;

/*
 * @author ymarni
 */

@Service
public class EVSAdminElectionServiceImpl implements EVSAdminElectionService {
	
	@Autowired
	private EVSAdminElectionRepository evsAdminElectionRepository;
	@Autowired
	private EVSAdminPartyRepository evsAdminPartyRepository;

	@Override
	public ResponseEntity<String> addElection(EVSAdminElectionDto evsAdminElectionDto) throws ElectionCodeAlreadyPresent, NoPartyDetailsFoundException{
		Optional<EVSAdminElection> evsAdminElection = evsAdminElectionRepository.findByElectionCode(evsAdminElectionDto.getElectionCode());
		if(evsAdminElection.isEmpty())
		{
			EVSAdminElection evsAdminElection2 = new EVSAdminElection();
			evsAdminElection2.setElectionCode(evsAdminElectionDto.getElectionCode());
			evsAdminElection2.setElectionName(evsAdminElectionDto.getElectionName());
			evsAdminElection2.setConstituency(evsAdminElectionDto.getConstituency());
			evsAdminElection2.setState(evsAdminElectionDto.getState());
			List<EVSAdminParty> partiesList = evsAdminElectionDto.getEvsAdminParties(); //getting this list of parties here and checking whether they are present in db and adding them. Also same as in updateElection method.
			List<EVSAdminParty> partiesListToBeSaved = new ArrayList<>();
			for(EVSAdminParty evsAdminParty1 : partiesList)
			{
				EVSAdminParty evsAdminParty = evsAdminPartyRepository.findByPartyCode(evsAdminParty1.getPartyCode()).orElseThrow(()->new NoPartyDetailsFoundException("no party found with code"));
				partiesListToBeSaved.add(evsAdminParty);
			}			
			evsAdminElection2.setEvsAdminParties(partiesListToBeSaved);
			evsAdminElectionRepository.save(evsAdminElection2);
			return new ResponseEntity<>("Election with the code " + evsAdminElectionDto.getElectionCode() +" has been added successfully",HttpStatus.CREATED);
		}
		throw new ElectionCodeAlreadyPresent("Election with the code " + evsAdminElectionDto.getElectionCode() +" has already been registered, try using different code");

	}

	@Override
	public ResponseEntity<String> deleteElection(String electionCode) throws NoElectionDetailsFoundException {
		Optional<EVSAdminElection> optionalEvsAdminElection = evsAdminElectionRepository.findByElectionCode(electionCode);
		if(optionalEvsAdminElection.isEmpty())
		{
			throw new NoElectionDetailsFoundException("No election details found with election code "+electionCode);
	
		}
		evsAdminElectionRepository.delete(optionalEvsAdminElection.get());
		return new ResponseEntity<>(String.format("Election with code %s has been successfully deleted", electionCode),HttpStatus.OK);
	}

	@Override
	public ResponseEntity<EVSAdminElectionDto> getElectionByCode(String electionCode) throws NoElectionDetailsFoundException {
		Optional<EVSAdminElection> optionalEvsAdminElection = evsAdminElectionRepository.findByElectionCode(electionCode);
		if(optionalEvsAdminElection.isEmpty())
		{
			throw new NoElectionDetailsFoundException("No election details found with election code "+electionCode);
	
		}
		EVSAdminElection evsAdminElection = optionalEvsAdminElection.get();
		EVSAdminElectionDto evsAdminElectionDto = new EVSAdminElectionDto(evsAdminElection.getId(),
				evsAdminElection.getElectionCode(),
				evsAdminElection.getElectionName(),
				evsAdminElection.getState(),
				evsAdminElection.getConstituency(),
				evsAdminElection.getEvsAdminParties());
		return new ResponseEntity<>(evsAdminElectionDto,HttpStatus.FOUND);
	}

	public ResponseEntity<List<EVSAdminElectionDto>> getAllElections() {
		List<EVSAdminElection> evsAdminElections = evsAdminElectionRepository.findAll();
		List<EVSAdminElectionDto> list = new ArrayList<>();
		evsAdminElections.forEach(evsAdminelection->
		{
			
			EVSAdminElectionDto evsAdminelectionDto = new EVSAdminElectionDto(evsAdminelection.getId(),
					evsAdminelection.getElectionCode(),
					evsAdminelection.getElectionName(),
					evsAdminelection.getState(),
					evsAdminelection.getConstituency(),
					evsAdminelection.getEvsAdminParties());
			list.add(evsAdminelectionDto);
		});
		return new ResponseEntity<>(list,HttpStatus.FOUND);
	}

	public ResponseEntity<String> updateElection(EVSAdminElectionDto evsAdminElectionDto, String electionCode) throws NoElectionDetailsFoundException, ElectionCodeMisMatchException, NoPartyDetailsFoundException {
		Optional<EVSAdminElection> optionalEvsAdminElection = evsAdminElectionRepository.findByElectionCode(electionCode);
		if (optionalEvsAdminElection.isEmpty()) {
			throw new NoElectionDetailsFoundException("No election details found with election code "+electionCode);
		}
		if(evsAdminElectionDto.getElectionCode().equals((optionalEvsAdminElection.get().getElectionCode())))
		{
			EVSAdminElection evsAdminElection = optionalEvsAdminElection.get();			
			evsAdminElection.setElectionName(evsAdminElectionDto.getElectionName());
			evsAdminElection.setState(evsAdminElectionDto.getState());
			evsAdminElection.setConstituency(evsAdminElectionDto.getConstituency());
			List<EVSAdminParty> partiesList = evsAdminElectionDto.getEvsAdminParties();
			List<EVSAdminParty> partiesListToBeSaved = new ArrayList<>();
			for(EVSAdminParty evsAdminParty1 : partiesList)
			{
				EVSAdminParty evsAdminParty = evsAdminPartyRepository.findByPartyCode(evsAdminParty1.getPartyCode()).orElseThrow(()->new NoPartyDetailsFoundException("no party found with code"));
				partiesListToBeSaved.add(evsAdminParty);
			}
			evsAdminElection.setEvsAdminParties(partiesListToBeSaved);
			evsAdminElectionRepository.save(evsAdminElection);
			return new ResponseEntity<>(String.format("Election with code %s has been updated successfully", electionCode),HttpStatus.OK);
		}
		throw new ElectionCodeMisMatchException("Election code which you are trying to update is invalid");
	}
	

}
