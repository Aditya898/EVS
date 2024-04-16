package com.evs.admin.serviceimpl;
/*
 * @author aditaitkar
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.evs.admin.dto.EVSAdminPartyDto;
import com.evs.admin.exception.PartyCodeAlreadyPresent;
import com.evs.admin.exception.PartyCodeMismatchException;
import com.evs.admin.exception.NoPartyDetailsFoundException;
import com.evs.admin.model.EVSAdminCandidate;
import com.evs.admin.model.EVSAdminParty;
import com.evs.admin.repository.EVSAdminCandidateRepository;
import com.evs.admin.repository.EVSAdminPartyRepository;
import com.evs.admin.service.EVSAdminPartyService;

@Service
public class EVSAdminPartyServiceImpl implements EVSAdminPartyService {

    @Autowired
    private EVSAdminPartyRepository evsAdminPartyRepository;
    @Autowired
    private EVSAdminCandidateRepository evsAdminCandidateRepository;

    @Override
    public ResponseEntity<String> addParty(EVSAdminPartyDto evsAdminPartyDto) throws PartyCodeAlreadyPresent {
        Optional<EVSAdminParty> evsAdminParty = evsAdminPartyRepository.findByPartyCode(evsAdminPartyDto.getPartyCode());
        if (evsAdminParty.isEmpty()) {
            EVSAdminParty party = new EVSAdminParty(evsAdminPartyDto.getId(), 
            		evsAdminPartyDto.getPartyCode(), 
            		evsAdminPartyDto.getPartyName(), 
            		evsAdminPartyDto.getLeader(), 
            		evsAdminPartyDto.getSymbol(),
            		evsAdminPartyDto.isNationalParty(),
            		evsAdminPartyDto.isStateParty());
            evsAdminPartyRepository.save(party);
            return new ResponseEntity<>("Party with the code " + evsAdminPartyDto.getPartyCode() + " has been added successfully", HttpStatus.CREATED);
        }
        throw new PartyCodeAlreadyPresent("Party with the code " + evsAdminPartyDto.getPartyCode() + " has already been registered, try using a different code");
    }

    @Override
    public ResponseEntity<String> deleteParty(String partyCode) throws NoPartyDetailsFoundException {
        Optional<EVSAdminParty> optionalEvsAdminParty = evsAdminPartyRepository.findByPartyCode(partyCode);
        if (optionalEvsAdminParty.isEmpty()) {
            throw new NoPartyDetailsFoundException("No party details found with party code " + partyCode);
        }
        List<EVSAdminCandidate> candidatesList = evsAdminCandidateRepository.findByEvsAdminParty_PartyCode("BJP");
        candidatesList.forEach((candidate)->{
        	candidate.setEvsAdminParty(null);
        });
        evsAdminPartyRepository.delete(optionalEvsAdminParty.get());
        return new ResponseEntity<>(String.format("Party with code %s has been successfully deleted", partyCode), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EVSAdminPartyDto> getPartyByCode(String partyCode) throws NoPartyDetailsFoundException {
        Optional<EVSAdminParty> optionalEvsAdminParty = evsAdminPartyRepository.findByPartyCode(partyCode);
        if (optionalEvsAdminParty.isEmpty()) {
            throw new NoPartyDetailsFoundException("No party details found with party code " + partyCode);
        }
        EVSAdminParty party = optionalEvsAdminParty.get();
        EVSAdminPartyDto partyDto = new EVSAdminPartyDto(party.getId(), party.getPartyCode(), party.getPartyName(), party.getLeader(), party.getSymbol(),party.isNationalParty(),party.isStateParty());
        return new ResponseEntity<>(partyDto, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<List<EVSAdminPartyDto>> getAllParties() {
        List<EVSAdminParty> parties = evsAdminPartyRepository.findAll();
        List<EVSAdminPartyDto> partyDtos = new ArrayList<>();
        parties.forEach((party) -> {
            EVSAdminPartyDto partyDto = new EVSAdminPartyDto(party.getId(), party.getPartyCode(), party.getPartyName(), party.getLeader(), party.getSymbol(),party.isNationalParty(),party.isStateParty());
            partyDtos.add(partyDto);
        });
        return new ResponseEntity<>(partyDtos, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<String> updateParty(EVSAdminPartyDto evsAdminPartyDto, String partyCode) throws NoPartyDetailsFoundException, PartyCodeMismatchException {
        Optional<EVSAdminParty> optionalEvsAdminParty = evsAdminPartyRepository.findByPartyCode(partyCode);
        if (optionalEvsAdminParty.isEmpty()) {
            throw new NoPartyDetailsFoundException("No party details found with party code " + partyCode);
        }
        if (!evsAdminPartyDto.getPartyCode().equals(optionalEvsAdminParty.get().getPartyCode())) {
            throw new PartyCodeMismatchException("The party code you are trying to update is invalid");
        }
        EVSAdminParty party = optionalEvsAdminParty.get();
        party.setPartyName(evsAdminPartyDto.getPartyName());
        party.setLeader(evsAdminPartyDto.getLeader());
        party.setSymbol(evsAdminPartyDto.getSymbol());
        party.setNationalParty(evsAdminPartyDto.isNationalParty());
        party.setStateParty(evsAdminPartyDto.isStateParty());
        evsAdminPartyRepository.save(party);
        return new ResponseEntity<>(String.format("Party with code %s has been updated successfully", partyCode), HttpStatus.OK);
    }
}
