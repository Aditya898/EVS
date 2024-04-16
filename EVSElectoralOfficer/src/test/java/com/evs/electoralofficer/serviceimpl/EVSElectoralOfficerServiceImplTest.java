package com.evs.electoralofficer.serviceimpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.evs.electoralofficer.dto.EVSVoterDto;
import com.evs.electoralofficer.exception.NoVoterFoundException;
import com.evs.electoralofficer.model.EVSVoter;
import com.evs.electoralofficer.repository.EVSVoterRepository;
import com.evs.electoralofficer.service.EVSElectoralOfficerApiClient;
import com.twilio.Twilio;
/*
 * @author aditaitkar
 */
@ExtendWith(MockitoExtension.class)
public class EVSElectoralOfficerServiceImplTest {

    @Mock
    private EVSElectoralOfficerApiClient electoralOfficerApiClient;

    @Mock
    private EVSVoterRepository evsVoterRepository;

    @InjectMocks
    private EVSElectoralOfficerServiceImpl electoralOfficerService;
    

 

    @Test
    public void testApproveOrRejectVoter_NoVoterFound_ExceptionThrown() {
        when(evsVoterRepository.findByVoterId("invalidVoterId")).thenReturn(Optional.empty());
        assertThrows(NoVoterFoundException.class, () -> {
            electoralOfficerService.approveOrRejectVoter("invalidVoterId");
        });
        verify(evsVoterRepository, never()).save(any()); 
        }
    
    
    @Test
    public void testApproveOrRejectVoter_VoterAlreadyApproved_ReturnsAlreadyApprovedResponse() {
        String voterId = "123";
        EVSVoter voter = new EVSVoter();
        voter.setEvsVoterStatus("APPROVED");
        when(evsVoterRepository.findByVoterId(voterId)).thenReturn(Optional.of(voter));
        ResponseEntity<String> response = electoralOfficerService.approveOrRejectVoter(voterId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Request is already Approved.", response.getBody());
    }
   
    
    
    @Test
    public void testApproveOrRejectVoter_NoVoterFound_ThrowsException() {
        String voterId = "123";
        when(evsVoterRepository.findByVoterId(voterId)).thenReturn(Optional.empty());
        assertThrows(NoVoterFoundException.class, () -> {
            electoralOfficerService.approveOrRejectVoter(voterId);
        });
    }
    

     
    
    @Test
    public void testGetAllVoters_NoVotersFound_ReturnsEmptyList() {
        when(evsVoterRepository.findAllByEvsVoterStatus("PENDING")).thenReturn(new ArrayList<>());
        List<EVSVoterDto> result = electoralOfficerService.getAllVoters();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAllVoters_VotersFound_ReturnsListOfVoterDtos() {
        List<EVSVoter> voters = new ArrayList<>();
        // Inserting voters list with some dummy voter objects
        EVSVoter voter1 = new EVSVoter();
        voter1.setId("1");
        // Set other properties
        voters.add(voter1);

        EVSVoter voter2 = new EVSVoter();
        voter2.setId("2");
        voters.add(voter2);
        when(evsVoterRepository.findAllByEvsVoterStatus("PENDING")).thenReturn(voters);
        List<EVSVoterDto> result = electoralOfficerService.getAllVoters();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size()); // Lets Assume we added 2 voters
        
    }

  
    
    @Test
    public void testGetVoter_VoterFound_Success() {
        EVSVoter voter = new EVSVoter();
        voter.setId("1");
        Optional<EVSVoter> optionalVoter = Optional.of(voter);
        when(evsVoterRepository.findByVoterId("validVoterId")).thenReturn(optionalVoter);
        ResponseEntity<EVSVoterDto> response = electoralOfficerService.getVoter("validVoterId");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetVoter_NoVoterFound_ExceptionThrown() {
        when(evsVoterRepository.findByVoterId("invalidVoterId")).thenReturn(Optional.empty());
        assertThrows(NoVoterFoundException.class, () -> {
            electoralOfficerService.getVoter("invalidVoterId");
        });
    }
    
    
    
    @Test
    public void testCheckStatus_ReturnsStatusFromApiClient() {
        String userName = "testUser";
        String expectedStatus = "ACTIVE";
        when(electoralOfficerApiClient.checkStatus(userName)).thenReturn(expectedStatus);
        String result = electoralOfficerService.checkStatus(userName);
        assertNotNull(result);
        assertEquals(expectedStatus, result);
    }


    
}

