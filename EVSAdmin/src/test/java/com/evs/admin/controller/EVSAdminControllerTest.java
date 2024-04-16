package com.evs.admin.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
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
import com.evs.admin.serviceimpl.EVSAdminElectionServiceImpl;
/*
 * @author aditaitkar
 */

@SpringBootTest
public class EVSAdminControllerTest {

    @Mock
    private EVSAdminElectionServiceImpl electionService;

    @InjectMocks
    private EVSAdminController electionController;

    @Test
    public void testSayWelcome() {
        String expected = "Welcome to the Electronic Voting System";
        String result = electionController.sayWelcome();
        assertEquals(expected, result);
    }

    @Test
    public void testAddElection() throws ElectionCodeAlreadyPresent, NoPartyDetailsFoundException {
        EVSAdminElectionDto electionDto = new EVSAdminElectionDto();
        ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.CREATED).body("Election with the code CODE123 has been added successfully");
        when(electionService.addElection(electionDto)).thenReturn(expectedResponse);
        ResponseEntity<String> response = electionController.addElection(electionDto);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testDeleteElection() throws NoElectionDetailsFoundException {
        String electionCode = "CODE123";
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Election with code CODE123 has been successfully deleted");
        when(electionService.deleteElection(electionCode)).thenReturn(expectedResponse);
        ResponseEntity<String> response = electionController.deleteElection(electionCode);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testGetElectionByCode() throws NoElectionDetailsFoundException {
        String electionCode = "CODE123";
        EVSAdminElectionDto expectedDto = new EVSAdminElectionDto();
        ResponseEntity<EVSAdminElectionDto> expectedResponse = ResponseEntity.status(HttpStatus.FOUND).body(expectedDto);
        when(electionService.getElectionByCode(electionCode)).thenReturn(expectedResponse);
        ResponseEntity<EVSAdminElectionDto> response = electionController.getElectionBycode(electionCode);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testGetAllElections() {
        List<EVSAdminElectionDto> expectedList = new ArrayList<>();
        ResponseEntity<List<EVSAdminElectionDto>> expectedResponse = ResponseEntity.status(HttpStatus.FOUND).body(expectedList);
        when(electionService.getAllElections()).thenReturn(expectedResponse);
        ResponseEntity<List<EVSAdminElectionDto>> response = electionController.getAllElections();
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testUpdateElection() throws NoElectionDetailsFoundException, ElectionCodeMisMatchException, NoPartyDetailsFoundException {
        String electionCode = "CODE123";
        EVSAdminElectionDto electionDto = new EVSAdminElectionDto();
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Election with code CODE123 has been updated successfully");
        when(electionService.updateElection(electionDto, electionCode)).thenReturn(expectedResponse);
        ResponseEntity<String> response = electionController.updateElection(electionDto, electionCode);
        assertEquals(expectedResponse, response);
    }
}
