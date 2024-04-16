package com.evs.admin.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.evs.admin.dto.EVSAdminPartyDto;
import com.evs.admin.exception.PartyCodeAlreadyPresent;
import com.evs.admin.exception.PartyCodeMismatchException;
import com.evs.admin.exception.NoPartyDetailsFoundException;
/*
 * @author aditaitkar
 */

public interface EVSAdminPartyService {
    ResponseEntity<String> addParty(EVSAdminPartyDto evsAdminPartyDto) throws PartyCodeAlreadyPresent;
    ResponseEntity<String> deleteParty(String partyCode) throws NoPartyDetailsFoundException;
    ResponseEntity<EVSAdminPartyDto> getPartyByCode(String partyCode) throws NoPartyDetailsFoundException;
    ResponseEntity<List<EVSAdminPartyDto>> getAllParties();
    ResponseEntity<String> updateParty(EVSAdminPartyDto evsAdminPartyDto, String partyCode) throws NoPartyDetailsFoundException, PartyCodeMismatchException;
}
