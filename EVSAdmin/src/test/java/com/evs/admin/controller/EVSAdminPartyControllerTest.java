package com.evs.admin.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.evs.admin.dto.EVSAdminPartyDto;
import com.evs.admin.exception.NoPartyDetailsFoundException;
import com.evs.admin.exception.PartyCodeAlreadyPresent;
import com.evs.admin.exception.PartyCodeMismatchException;
import com.evs.admin.serviceimpl.EVSAdminPartyServiceImpl;

/*
 * @author aditaitkar
 */


public class EVSAdminPartyControllerTest {

    @Mock
    private EVSAdminPartyServiceImpl evsAdminPartyService;

    @InjectMocks
    private EVSAdminPartyController evsAdminPartyController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddParty() throws PartyCodeAlreadyPresent {
        EVSAdminPartyDto partyDto = new EVSAdminPartyDto();
        when(evsAdminPartyService.addParty(partyDto)).thenReturn(new ResponseEntity<>("Party added", HttpStatus.CREATED));

        ResponseEntity<String> response = evsAdminPartyController.addParty(partyDto);

        assert(response.getStatusCodeValue() == HttpStatus.CREATED.value());
        verify(evsAdminPartyService).addParty(partyDto);
    }

    @Test
    public void testDeleteParty() throws NoPartyDetailsFoundException {
        String partyCode = "partyCode";
        when(evsAdminPartyService.deleteParty(partyCode)).thenReturn(new ResponseEntity<>("Party deleted", HttpStatus.OK));

        ResponseEntity<String> response = evsAdminPartyController.deleteParty(partyCode);

        assert(response.getStatusCodeValue() == HttpStatus.OK.value());
        verify(evsAdminPartyService).deleteParty(partyCode);
    }

    @Test
    public void testGetPartyByCode() throws NoPartyDetailsFoundException {
        String partyCode = "partyCode";
        EVSAdminPartyDto partyDto = new EVSAdminPartyDto();
        when(evsAdminPartyService.getPartyByCode(partyCode)).thenReturn(new ResponseEntity<>(partyDto, HttpStatus.FOUND));

        ResponseEntity<EVSAdminPartyDto> response = evsAdminPartyController.getPartyByCode(partyCode);

        assert(response.getStatusCodeValue() == HttpStatus.FOUND.value());
        verify(evsAdminPartyService).getPartyByCode(partyCode);
    }

    @Test
    public void testGetAllParties() {
        List<EVSAdminPartyDto> partyDtos = new ArrayList<>();
        when(evsAdminPartyService.getAllParties()).thenReturn(new ResponseEntity<>(partyDtos, HttpStatus.FOUND));

        ResponseEntity<List<EVSAdminPartyDto>> response = evsAdminPartyController.getAllParties();

        assert(response.getStatusCodeValue() == HttpStatus.FOUND.value());
        verify(evsAdminPartyService).getAllParties();
    }
    
   
   

    @Test
    public void testUpdateParty() throws NoPartyDetailsFoundException, PartyCodeMismatchException {
        String partyCode = "partyCode";
        EVSAdminPartyDto partyDto = new EVSAdminPartyDto();
        when(evsAdminPartyService.updateParty(partyDto, partyCode)).thenReturn(new ResponseEntity<>("Party updated", HttpStatus.OK));

        ResponseEntity<String> response = evsAdminPartyController.updateParty(partyDto, partyCode);

        assert(response.getStatusCodeValue() == HttpStatus.OK.value());
        verify(evsAdminPartyService).updateParty(partyDto, partyCode);
    }
}
