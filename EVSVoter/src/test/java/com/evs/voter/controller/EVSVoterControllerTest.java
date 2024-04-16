package com.evs.voter.controller;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.evs.voter.dto.EVSAdminElectionDto;
import com.evs.voter.dto.EVSVoterCastVoteDto;
import com.evs.voter.exception.AlreadyVoteCastedException;
import com.evs.voter.exception.DownloadFailedException;
import com.evs.voter.exception.ElectionCodeMisMatchException;
import com.evs.voter.exception.NoElectionDetailsFoundException;
import com.evs.voter.exception.NoVoterFoundException;
import com.evs.voter.exception.PartyCodeNotMatchingException;
import com.evs.voter.serviceimpl.EVSVoterServiceImpl;

/*
 * @author aditaitkar
 */

@ExtendWith(MockitoExtension.class)
public class EVSVoterControllerTest {

    @Mock
    private EVSVoterServiceImpl evsVoterService;

    @InjectMocks
    private EVSVoterController evsVoterController;

    @Test
    public void testRegisterAsNewVoter_SuccessfulRegistration() throws IOException {
        when(evsVoterService.registerAsNewVoter(any(), any(), any(), any(), any(), any(), anyInt(), any(), any(), any(), any()))
                .thenReturn(new ResponseEntity<>("Your new voter request has been registered", HttpStatus.CREATED));

        // Providing the date in the correct format
        String dateOfBirth = "01-01-2000";

        // Mocking file upload
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes());

        // Invoking the controller method with the corrected date format
        ResponseEntity<String> response = evsVoterController.registerAsNewVoter("testUser", "Adi", "Taitkar",
                "Male", "Adi@example.com", dateOfBirth, 25, "1234567890", "Address", "Nationality", file);

        // Verifying the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    /*
    @Test
    public void testRegisterAsNewVoter_UnderageRegistration() throws IOException {
        String dateOfBirth = "01-01-2010";
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes());
        ResponseEntity<String> response = evsVoterController.registerAsNewVoter("testUser", "Adi", "Taitkar",
                "Male", "Adi@example.com", dateOfBirth, 10, "1234567890", "Address", "Nationality", file);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("age is not sufficient"));
    }


*/

    @Test
    public void testCheckStatus_ValidUserName_Success() {
        when(evsVoterService.checkStatus(any())).thenReturn(new ResponseEntity<>("Voter status: Approved", HttpStatus.OK));
        ResponseEntity<String> response = evsVoterController.checkStatus("testUser");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Approved"));
    }

  

    @Test
    void testDownloadEVoterCard_DownloadFailedException() throws DownloadFailedException   {
        when(evsVoterService.downloadEVoterCard(any())).thenThrow(new DownloadFailedException("Download failed"));
        assertThrows(DownloadFailedException.class, () -> {
            evsVoterController.downloadEVoterCard("validVoterId");
        });
    }


    @Test
    void testDownloadEVoterCard_ValidVoterId_Success() throws DownloadFailedException {
        ByteArrayInputStream voterCardData = new ByteArrayInputStream("Voter card data".getBytes());
        when(evsVoterService.downloadEVoterCard(any())).thenReturn(voterCardData);
        ResponseEntity<InputStreamResource> response = evsVoterController.downloadEVoterCard("validVoterId");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

 

    @Test
    void testGetElectionByCode_InvalidElectionCode_NotFound() throws NoVoterFoundException, NoElectionDetailsFoundException {
        when(evsVoterService.getElectionByCode(any())).thenThrow(new NoVoterFoundException("No election found"));
        assertThrows(NoVoterFoundException.class, () -> {
            evsVoterController.getElectionBycode("invalidElectionCode");
        });
    }

    @Test
    void testCastVote_ValidVote_Success() throws PartyCodeNotMatchingException, NoVoterFoundException, AlreadyVoteCastedException, ElectionCodeMisMatchException, NoElectionDetailsFoundException {
        when(evsVoterService.castYourVote(any(), any(), any())).thenReturn(new ResponseEntity<>("Vote casted successfully", HttpStatus.OK));
        EVSVoterCastVoteDto voteDto = new EVSVoterCastVoteDto(1L, "voterId", "electionCode", "partyCode", 1L);
        ResponseEntity<String> response = evsVoterController.castVote(voteDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Vote casted successfully", response.getBody());
    }

    @Test
    void testCastVote_AlreadyVoteCasted_Exception() throws PartyCodeNotMatchingException, NoVoterFoundException, AlreadyVoteCastedException, ElectionCodeMisMatchException, NoElectionDetailsFoundException {
        when(evsVoterService.castYourVote(any(), any(), any())).thenThrow(new AlreadyVoteCastedException("Vote already casted"));
        EVSVoterCastVoteDto voteDto = new EVSVoterCastVoteDto(1L, "voterId", "electionCode", "partyCode", 1L);
        assertThrows(AlreadyVoteCastedException.class, () -> {
            evsVoterController.castVote(voteDto);
        });
    }

    

    @Test
    void testGetResults_ValidElectionCode_Found() {
        when(evsVoterService.checkResults(any())).thenReturn(new ResponseEntity<>("Election results", HttpStatus.OK));
        ResponseEntity<String> response = evsVoterController.getResults("E001");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Election results", response.getBody());
    }
    
    @Test
    public void testDownloadImage_ValidImageId_ReturnImageData() {
        // Mock image data
        byte[] imageData = "Image data".getBytes();
        when(evsVoterService.downloadImage(ArgumentMatchers.anyString())).thenReturn(imageData);
 
        // Invoking the controller method
        ResponseEntity<byte[]> response = evsVoterController.downloadImage("imageId");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(imageData, response.getBody());
    }
    
    
    @Test
    public void testGetElectionByCode_ElectionFound_ReturnElectionDto() throws NoElectionDetailsFoundException {
        // Mock an EVSAdminElectionDto
        EVSAdminElectionDto electionDto = new EVSAdminElectionDto();
        when(evsVoterService.getElectionByCode("validElectionCode")).thenReturn(new ResponseEntity<>(electionDto, HttpStatus.FOUND));
        // Invoke the controller method
        ResponseEntity<EVSAdminElectionDto> response = evsVoterController.getElectionBycode("validElectionCode");
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(electionDto, response.getBody());
    }

    @Test
    public void testGetElectionByCode_NoElectionDetailsFound_ReturnException() throws NoElectionDetailsFoundException {
        when(evsVoterService.getElectionByCode("invalidElectionCode")).thenThrow(new NoElectionDetailsFoundException("No election details found"));
        assertThrows(NoElectionDetailsFoundException.class, () -> {
            evsVoterController.getElectionBycode("invalidElectionCode");
        });
    }
}
