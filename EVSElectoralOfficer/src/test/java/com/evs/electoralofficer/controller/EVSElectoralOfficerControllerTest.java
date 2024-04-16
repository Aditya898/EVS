package com.evs.electoralofficer.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.evs.electoralofficer.dto.EVSVoterDto;
import com.evs.electoralofficer.serviceimpl.EVSElectoralOfficerServiceImpl;
/*
 * @author aditaitkar
 */

@ExtendWith(MockitoExtension.class)
public class EVSElectoralOfficerControllerTest {

    @Mock
    private EVSElectoralOfficerServiceImpl electoralOfficerService;

    @InjectMocks
    private EVSElectoralOfficerController electoralOfficerController;

    @Test
    public void testApproveOrDeclineVoterRequest_ValidVoterId_Success() {
        String voterId = "validVoterId";
        when(electoralOfficerService.approveOrRejectVoter(voterId)).thenReturn(new ResponseEntity<>("Request is already Approved.", HttpStatus.OK));
        ResponseEntity<String> response = electoralOfficerController.approveOrDeclineVoterRequest(voterId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Request is already Approved.", response.getBody());
    }

    @Test
    public void testGetAllPendingVoters_NoPendingVoters_EmptyListReturned() {
        when(electoralOfficerService.getAllVoters()).thenReturn(new ArrayList<>());
        List<EVSVoterDto> result = electoralOfficerController.getAllPendingVoters();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetVoter_ValidVoterId_Success() {
        String voterId = "validVoterId";
        EVSVoterDto voterDto = new EVSVoterDto();
        when(electoralOfficerService.getVoter(voterId)).thenReturn(new ResponseEntity<>(voterDto, HttpStatus.OK));
        ResponseEntity<EVSVoterDto> response = electoralOfficerController.getVoter(voterId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
       
    }

    @Test
    public void testCheckStatus_ValidUserName_Success() {
        String userName = "validUserName";
        String expectedStatus = "ACTIVE"; 
        when(electoralOfficerService.checkStatus(userName)).thenReturn(expectedStatus);
        String status = electoralOfficerController.checkStatus(userName);
        assertEquals(expectedStatus, status);
    }


}
