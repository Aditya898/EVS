package com.evs.admin.serviceimpl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.evs.admin.dto.EVSAdminPartyDto;
import com.evs.admin.exception.PartyCodeAlreadyPresent;
import com.evs.admin.exception.PartyCodeMismatchException;
import com.evs.admin.exception.NoPartyDetailsFoundException;
import com.evs.admin.model.EVSAdminParty;
import com.evs.admin.repository.EVSAdminCandidateRepository;
import com.evs.admin.repository.EVSAdminPartyRepository;
import com.evs.admin.serviceimpl.EVSAdminPartyServiceImpl;


@ExtendWith(MockitoExtension.class)
public class EVSAdminPartyServiceImplTest {

    @Mock
    private EVSAdminPartyRepository partyRepository;

    @Mock
    private EVSAdminCandidateRepository candidateRepository;

    @InjectMocks
    private EVSAdminPartyServiceImpl partyService;

    private EVSAdminPartyDto partyDto;
    private EVSAdminParty existingParty;
    private List<EVSAdminParty> partyList;

    @BeforeEach
    public void setUp() {
        partyDto = new EVSAdminPartyDto(1L, "BJP", "Bharatiya Janata Party", "Narendra Modi", "Lotus", true, false);
        existingParty = new EVSAdminParty(1L, "BJP", "Bharatiya Janata Party", "Narendra Modi", "Lotus", true, false);
        partyList = new ArrayList<>();
        partyList.add(existingParty);
    }

    @Test
    public void testAddParty_Success() throws PartyCodeAlreadyPresent {
        when(partyRepository.findByPartyCode("BJP")).thenReturn(Optional.empty());
        ResponseEntity<String> response = partyService.addParty(partyDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Party with the code BJP has been added successfully", response.getBody());
    }

    @Test
    public void testAddParty_PartyCodeAlreadyPresent() {
        when(partyRepository.findByPartyCode("BJP")).thenReturn(Optional.of(existingParty));
        assertThrows(PartyCodeAlreadyPresent.class, () -> partyService.addParty(partyDto));
    }

    @Test
    public void testDeleteParty_Success() throws NoPartyDetailsFoundException {
        when(partyRepository.findByPartyCode("BJP")).thenReturn(Optional.of(existingParty));
        ResponseEntity<String> response = partyService.deleteParty("BJP");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Party with code BJP has been successfully deleted", response.getBody());
    }

    @Test
    public void testDeleteParty_NoPartyDetailsFound() {
        when(partyRepository.findByPartyCode("BJP")).thenReturn(Optional.empty());
        assertThrows(NoPartyDetailsFoundException.class, () -> partyService.deleteParty("BJP"));
    }

    @Test
    public void testGetPartyByCode_Success() throws NoPartyDetailsFoundException {
        when(partyRepository.findByPartyCode("BJP")).thenReturn(Optional.of(existingParty));
        ResponseEntity<EVSAdminPartyDto> response = partyService.getPartyByCode("BJP");
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(partyDto, response.getBody());
    }

    @Test
    public void testGetPartyByCode_NoPartyDetailsFound() {
        when(partyRepository.findByPartyCode("BJP")).thenReturn(Optional.empty());
        assertThrows(NoPartyDetailsFoundException.class, () -> partyService.getPartyByCode("BJP"));
    }

    @Test
    public void testGetAllParties() {
        when(partyRepository.findAll()).thenReturn(partyList);
        ResponseEntity<List<EVSAdminPartyDto>> response = partyService.getAllParties();
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testUpdateParty_Success() throws NoPartyDetailsFoundException, PartyCodeMismatchException {
        when(partyRepository.findByPartyCode("BJP")).thenReturn(Optional.of(existingParty));
        ResponseEntity<String> response = partyService.updateParty(partyDto, "BJP");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Party with code BJP has been updated successfully", response.getBody());
    }

    @Test
    public void testUpdateParty_NoPartyDetailsFound() {
        when(partyRepository.findByPartyCode("BJP")).thenReturn(Optional.empty());
        assertThrows(NoPartyDetailsFoundException.class, () -> partyService.updateParty(partyDto, "BJP"));
    }


}
