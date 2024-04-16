package com.evs.admin.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.evs.admin.exception.CandidateCodeMisMatchException;

import com.evs.admin.controller.EVSAdminCandidateController;
import com.evs.admin.dto.EVSAdminCandidateDto;
import com.evs.admin.exception.NoCandidateFoundException;
import com.evs.admin.serviceimpl.EVSAdminCandidateServiceImpl;
/*
 * @author aditaitkar
 */

public class EVSAdminCandidateControllerTest {

    @Mock
    private EVSAdminCandidateServiceImpl evsAdminCandidateService;

    @InjectMocks
    private EVSAdminCandidateController evsAdminCandidateController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddCandidate() {
        EVSAdminCandidateDto candidateDto = new EVSAdminCandidateDto();
        when(evsAdminCandidateService.saveCandidate(candidateDto)).thenReturn(new ResponseEntity<>("Candidate added", HttpStatus.OK));

        ResponseEntity<String> response = evsAdminCandidateController.addCandidate(candidateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Candidate added", response.getBody());
    }


    @Test
    public void testGetCandidate() throws NoCandidateFoundException {
        String candidateId = "1";
        EVSAdminCandidateDto candidateDto = new EVSAdminCandidateDto();
        when(evsAdminCandidateService.getCandidate(candidateId)).thenReturn(new ResponseEntity<>(candidateDto, HttpStatus.OK));

        ResponseEntity<EVSAdminCandidateDto> response = evsAdminCandidateController.getCandidate(candidateId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(candidateDto, response.getBody());
    }

    @Test
    public void testGetAllCandidates() {
        List<EVSAdminCandidateDto> candidateDtos = new ArrayList<>();
        when(evsAdminCandidateService.getAllCandidates()).thenReturn(new ResponseEntity<>(candidateDtos, HttpStatus.OK));

        ResponseEntity<List<EVSAdminCandidateDto>> response = evsAdminCandidateController.getAllCandidates();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(candidateDtos, response.getBody());
    }

    
    @Test
    public void testDeleteCandidate() throws NoCandidateFoundException {
        String candidateId = "1";
        doReturn(new ResponseEntity<>("Candidate deleted", HttpStatus.OK))
            .when(evsAdminCandidateService).deleteCandidate(candidateId);
        ResponseEntity<String> response = evsAdminCandidateController.deleteCandidate(candidateId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Candidate deleted", response.getBody());
    }
    
    
    @Test
    public void testUpdateCandidate() throws NoCandidateFoundException, CandidateCodeMisMatchException {
        String candidateId = "1";
        EVSAdminCandidateDto updatedDto = new EVSAdminCandidateDto();
        updatedDto.setCandidateId(candidateId);
        doReturn(new ResponseEntity<>("Candidate updated", HttpStatus.OK))
            .when(evsAdminCandidateService).updateCandidate(updatedDto, candidateId);
        ResponseEntity<String> response = evsAdminCandidateController.updateCandidate(updatedDto, candidateId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Candidate updated", response.getBody());
    }
    
}
