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

import com.evs.admin.dto.EVSAdminPartyDto;
import com.evs.admin.exception.PartyCodeAlreadyPresent;
import com.evs.admin.exception.PartyCodeMismatchException;
import com.evs.admin.exception.NoPartyDetailsFoundException;
import com.evs.admin.serviceimpl.EVSAdminPartyServiceImpl;

/*
 * @author aditaitkar
 */

@RestController
@RequestMapping("admin/party")
public class EVSAdminPartyController {

    @Autowired
    private EVSAdminPartyServiceImpl evsAdminPartyServiceImpl;

    @PostMapping("addParty")
    public ResponseEntity<String> addParty(@RequestBody EVSAdminPartyDto evsAdminPartyDto) throws PartyCodeAlreadyPresent {
        return evsAdminPartyServiceImpl.addParty(evsAdminPartyDto);
    }

    @DeleteMapping("deleteParty/{partyCode}")
    public ResponseEntity<String> deleteParty(@PathVariable String partyCode) throws NoPartyDetailsFoundException {
        return evsAdminPartyServiceImpl.deleteParty(partyCode);
    }

    @GetMapping("getParty/{partyCode}")
    public ResponseEntity<EVSAdminPartyDto> getPartyByCode(@PathVariable String partyCode) throws NoPartyDetailsFoundException {
        return evsAdminPartyServiceImpl.getPartyByCode(partyCode);
    }

    @GetMapping("getAllParties")
    public ResponseEntity<List<EVSAdminPartyDto>> getAllParties() {
        return evsAdminPartyServiceImpl.getAllParties();
    }

    @PutMapping("updateParty/{partyCode}")
    public ResponseEntity<String> updateParty(@RequestBody EVSAdminPartyDto evsAdminPartyDto, @PathVariable String partyCode) throws NoPartyDetailsFoundException, PartyCodeMismatchException {
        System.out.println(evsAdminPartyDto.isNationalParty());
        System.out.println(evsAdminPartyDto.isStateParty());
    	return evsAdminPartyServiceImpl.updateParty(evsAdminPartyDto, partyCode);
    }
}
