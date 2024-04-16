package com.evs.admin.serviceimpl;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.evs.admin.dto.EVSAdminElectionDto;
import com.evs.admin.exception.ElectionCodeAlreadyPresent;
import com.evs.admin.exception.ElectionCodeMisMatchException;
import com.evs.admin.exception.NoElectionDetailsFoundException;
import com.evs.admin.exception.NoPartyDetailsFoundException;
import com.evs.admin.model.EVSAdminElection;
import com.evs.admin.model.EVSAdminParty;
import com.evs.admin.repository.EVSAdminElectionRepository;
import com.evs.admin.repository.EVSAdminPartyRepository;
import com.evs.admin.serviceimpl.EVSAdminElectionServiceImpl;


@SpringBootTest
public class EVSAdminElectionServiceImplTest {

    @Mock
    private EVSAdminElectionRepository electionRepository;

    @Mock
    private EVSAdminPartyRepository partyRepository;

    @InjectMocks
    private EVSAdminElectionServiceImpl electionService;
    
    
    
    @Test
    public void testAddElection_Success() throws ElectionCodeAlreadyPresent, NoPartyDetailsFoundException {
        // Mock data
        EVSAdminElectionDto electionDto = new EVSAdminElectionDto();
        electionDto.setElectionCode("CODE123");
        electionDto.setElectionName("Test Election");
        electionDto.setConstituency("Test Constituency");
        electionDto.setState("Test State");
        EVSAdminParty party1 = new EVSAdminParty();
        party1.setPartyCode("P1");
        EVSAdminParty party2 = new EVSAdminParty();
        party2.setPartyCode("P2");
        List<EVSAdminParty> parties = new ArrayList<>();
        parties.add(party1);
        parties.add(party2);
        electionDto.setEvsAdminParties(parties);

        // Mock repository
        when(electionRepository.findByElectionCode(anyString())).thenReturn(Optional.empty());
        when(partyRepository.findByPartyCode(anyString())).thenReturn(Optional.of(new EVSAdminParty()));

        // Test
        ResponseEntity<String> response = electionService.addElection(electionDto);

        // Verify
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Election with the code CODE123 has been added successfully", response.getBody());
        verify(electionRepository, times(1)).save(any(EVSAdminElection.class));
    }


  
    @Test
    public void testAddElection_ElectionCodeAlreadyPresent() throws ElectionCodeAlreadyPresent, NoPartyDetailsFoundException {
        EVSAdminElectionDto electionDto = new EVSAdminElectionDto();
        electionDto.setElectionCode("CODE123");
        when(electionRepository.findByElectionCode(anyString())).thenReturn(Optional.of(new EVSAdminElection()));
        assertThrows(ElectionCodeAlreadyPresent.class, () -> {
            electionService.addElection(electionDto);
        });
    }

    @Test
    public void testDeleteElection() throws NoElectionDetailsFoundException {
        String electionCode = "CODE123";
        EVSAdminElection election = new EVSAdminElection();
        when(electionRepository.findByElectionCode(anyString())).thenReturn(Optional.of(election));
        ResponseEntity<String> response = electionService.deleteElection(electionCode);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(String.format("Election with code %s has been successfully deleted", electionCode), response.getBody());
    }

    
    
    @Test
    public void testGetElectionByCode() throws NoElectionDetailsFoundException {
       
        String electionCode = "CODE123";
        EVSAdminElection election = new EVSAdminElection();
        election.setId(1L);
        election.setElectionCode(electionCode);
        when(electionRepository.findByElectionCode(electionCode)).thenReturn(Optional.of(election));

        
        ResponseEntity<EVSAdminElectionDto> response = electionService.getElectionByCode(electionCode);

        
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(election.getId(), response.getBody().getId());
        assertEquals(election.getElectionCode(), response.getBody().getElectionCode());
    }

    @Test
    public void testGetAllElections() {
        // Mock data
        List<EVSAdminElection> elections = new ArrayList<>();
        EVSAdminElection election1 = new EVSAdminElection();
        election1.setId(1L);
        election1.setElectionCode("CODE123");
        EVSAdminElection election2 = new EVSAdminElection();
        election2.setId(2L);
        election2.setElectionCode("CODE456");
        elections.add(election1);
        elections.add(election2);
        when(electionRepository.findAll()).thenReturn(elections);

        ResponseEntity<List<EVSAdminElectionDto>> response = electionService.getAllElections();
        
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(election1.getId(), response.getBody().get(0).getId());
        assertEquals(election1.getElectionCode(), response.getBody().get(0).getElectionCode());
        assertEquals(election2.getId(), response.getBody().get(1).getId());
        assertEquals(election2.getElectionCode(), response.getBody().get(1).getElectionCode());
    }

    
    @Test
    public void testUpdateElection_Success() throws NoElectionDetailsFoundException, ElectionCodeMisMatchException, NoPartyDetailsFoundException {
        
        EVSAdminElectionDto electionDto = new EVSAdminElectionDto();
        electionDto.setElectionCode("CODE123");
        electionDto.setElectionName("Updated Election Name");
        electionDto.setConstituency("Updated Constituency");
        electionDto.setState("Updated State");
        EVSAdminParty party1 = new EVSAdminParty();
        party1.setPartyCode("P1");
        EVSAdminParty party2 = new EVSAdminParty();
        party2.setPartyCode("P2");
        List<EVSAdminParty> parties = new ArrayList<>();
        parties.add(party1);
        parties.add(party2);
        electionDto.setEvsAdminParties(parties);

        
        EVSAdminElection existingElection = new EVSAdminElection();
        existingElection.setElectionCode("CODE123");
        when(electionRepository.findByElectionCode(anyString())).thenReturn(Optional.of(existingElection));
        when(partyRepository.findByPartyCode(anyString())).thenReturn(Optional.of(new EVSAdminParty()));

        
        ResponseEntity<String> response = electionService.updateElection(electionDto, "CODE123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Election with code CODE123 has been updated successfully", response.getBody());
        verify(electionRepository).save(any(EVSAdminElection.class));
    }

 /*   
    @Test
    public void testUpdateElection_NoElectionDetailsFound() {
        EVSAdminElectionDto electionDto = new EVSAdminElectionDto();
        electionDto.setElectionCode("CODE123");
        when(electionRepository.findByElectionCode(anyString())).thenReturn(Optional.empty());
        assertThrows(NoElectionDetailsFoundException.class, () -> {
            electionService.updateElection(electionDto, "CODE123");
        });
        verify(electionRepository, never()).save(any(EVSAdminElection.class));
    }
    
 */
    
  /* 
    @Test
    public void testUpdateElection_ElectionCodeMisMatch() {
        // Mock data
        EVSAdminElectionDto electionDto = new EVSAdminElectionDto();
        electionDto.setElectionCode("CODE123");

        // Mock repository
        EVSAdminElection existingElection = new EVSAdminElection();
        existingElection.setElectionCode("DIFFERENT_CODE");
        when(electionRepository.findByElectionCode(anyString())).thenReturn(Optional.of(existingElection));

        // Test and Verify
        assertThrows(ElectionCodeMisMatchException.class, () -> {
            electionService.updateElection(electionDto, "CODE123");
        });
        verify(electionRepository, never()).save(any(EVSAdminElection.class));
    }
    
    */
    
    /*
    @Test
    public void testUpdateElection_NoPartyDetailsFound() {
        // Mock data
        EVSAdminElectionDto electionDto = new EVSAdminElectionDto();
        electionDto.setElectionCode("CODE123");
        EVSAdminParty party1 = new EVSAdminParty();
        party1.setPartyCode("P1");
        List<EVSAdminParty> parties = new ArrayList<>();
        parties.add(party1);
        electionDto.setEvsAdminParties(parties);

        // Mock repository
        EVSAdminElection existingElection = new EVSAdminElection();
        existingElection.setElectionCode("CODE123");
        when(electionRepository.findByElectionCode(anyString())).thenReturn(Optional.of(existingElection));
        when(partyRepository.findByPartyCode(anyString())).thenReturn(Optional.empty());

        // Test and Verify
        assertThrows(NoPartyDetailsFoundException.class, () -> {
            electionService.updateElection(electionDto, "CODE123");
        });
        verify(electionRepository, never()).save(any(EVSAdminElection.class));
    }
    
    */
   
}

