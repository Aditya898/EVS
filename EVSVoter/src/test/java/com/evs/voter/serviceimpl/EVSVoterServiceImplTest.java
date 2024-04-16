package com.evs.voter.serviceimpl;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.evs.voter.constants.EVSVoterConstants;
import com.evs.voter.dto.EVSAdminElectionDto;
import com.evs.voter.exception.AlreadyVoteCastedException;
import com.evs.voter.exception.DownloadFailedException;
import com.evs.voter.exception.ElectionCodeMisMatchException;
import com.evs.voter.exception.NoElectionDetailsFoundException;
import com.evs.voter.exception.NoVoterFoundException;
import com.evs.voter.exception.PartyCodeNotMatchingException;
import com.evs.voter.model.EVSAdminElection;
import com.evs.voter.model.EVSVoter;
import com.evs.voter.model.EVSVoterCastVote;
import com.evs.voter.repository.EVSAdminElectionRepository;
import com.evs.voter.repository.EVSVoterCastVoteRepository;
import com.evs.voter.repository.EVSVoterRepository;
import com.evs.voter.service.EVSResultApiClient;
import com.evs.voter.serviceimpl.EVSVoterServiceImpl;
/*
 * @author aditaitkar
 */
@ExtendWith(MockitoExtension.class)
public class EVSVoterServiceImplTest {

    @Mock
    private EVSVoterRepository evsVoterRepository;
    @Mock
    private EVSResultApiClient evsResultApiClient; 
    
    @Mock
    private EVSAdminElectionRepository evsAdminElectionRepository;
    
    @Mock
    private EVSVoterCastVoteRepository evsVoterCastVoteRepository;

    @InjectMocks
    private EVSVoterServiceImpl evsVoterService;

    @Test
    public void testCheckStatus_ValidUserName_Success() {
        String userName = "testuser";
        EVSVoter evsVoter = new EVSVoter();
        evsVoter.setEvsVoterStatus(EVSVoterConstants.STATUS_AFTER_REGISTERING_APPROVED);
        when(evsVoterRepository.findByUserName(userName)).thenReturn(Optional.of(evsVoter));

        ResponseEntity<String> response = evsVoterService.checkStatus(userName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains(EVSVoterConstants.STATUS_AFTER_REGISTERING_APPROVED));
    }

    @Test
    public void testCheckStatus_InvalidUserName_NotFound() {
        String userName = "nonexistentuser";
        when(evsVoterRepository.findByUserName(userName)).thenReturn(Optional.empty());

        assertThrows(NoVoterFoundException.class, () -> {
            evsVoterService.checkStatus(userName);
        });
    }

   
    @Test
    public void testDownloadEVoterCard_InvalidVoterId_NotFound() {
        String voterId = "nonexistentid";
        when(evsVoterRepository.findByVoterId(voterId)).thenReturn(Optional.empty());
        assertThrows(NoVoterFoundException.class, () -> {
            evsVoterService.downloadEVoterCard(voterId);
        }, "Expected NoVoterFoundException to be thrown");
    }
    
    
    @Test
    void testRegisterAsNewVoter_SuccessfulRegistration() throws IOException {
        // Mocking the repository's behavior
        when(evsVoterRepository.findByUserName(any())).thenReturn(Optional.empty());
        when(evsVoterRepository.save(any())).thenReturn(mock(EVSVoter.class));

        // Mocking file upload
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes());

        // Providing the date in the correct format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dateOfBirth = "01-01-2000";
        LocalDate parsedDateOfBirth = LocalDate.parse(dateOfBirth, formatter);

        // Invoking the service method with the corrected date format
        ResponseEntity<String> response = evsVoterService.registerAsNewVoter("testUser", "Adi", "Taitkar",
                "Adi@example.com", "Male", dateOfBirth, 25, "1234567890", "Address", "Nationality", file);

        // Verifying the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }


    @Test
    void testRegisterAsNewVoter_UnsuccessfulDueToDuplicateUsername() throws IOException {
        when(evsVoterRepository.findByUserName(any())).thenReturn(Optional.of(mock(EVSVoter.class)));
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes());

        ResponseEntity<String> response = evsVoterService.registerAsNewVoter("testUser", "Adi", "Taitkar",
                "Adi@example.com", "Male", "2000-01-01", 25, "1234567890", "Address", "Nationality", file);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    
   
    
    @Test
    void testRegisterAsNewVoter_UnsuccessfulDueToInvalidFileUpload() throws IOException {
    	String dateOfBirth = "01-01-2000";
        when(evsVoterRepository.findByUserName(any())).thenReturn(Optional.empty());
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "".getBytes());
        ResponseEntity<String> response = evsVoterService.registerAsNewVoter("testUser", "Adi", "Taitkar",
                "Adi@example.com", "Male", dateOfBirth, 25, "1234567890", "Address", "Nationality", file);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    void testRegisterAsNewVoter_UnsuccessfulDueToUnderage() throws IOException {
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes());
        ResponseEntity<String> response = evsVoterService.registerAsNewVoter("testUser", "Adi", "Taitkar",
                "Adi@example.com", "Male", "2010-01-01", 12, "1234567890", "Address", "Nationality", file);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    

    @Test
    void testDownloadEVoterCard_InvalidVoterId_ThrowsException() {
        String voterId = "nonexistentid";
        when(evsVoterRepository.findByVoterId(voterId)).thenReturn(Optional.empty());

        assertThrows(NoVoterFoundException.class, () -> {
            evsVoterService.downloadEVoterCard(voterId);
        });
    }

    

    

    @Test
    void testCastYourVote_UnsuccessfulDueToNoVoterFound() {
        String voterId = "nonexistentid";
        String electionCode = "ELECTION_CODE";
        String partyCode = "PARTY_CODE";
        when(evsVoterRepository.findByVoterId(voterId)).thenReturn(Optional.empty());

        assertThrows(NoVoterFoundException.class, () -> {
            evsVoterService.castYourVote(voterId, electionCode, partyCode);
        });
    }

    
    @Test
    public void testGetElectionByCode_ExistingCode_Success() throws NoElectionDetailsFoundException {
        when(evsAdminElectionRepository.findByElectionCode(anyString())).thenReturn(Optional.ofNullable(new EVSAdminElection()));
        ResponseEntity<EVSAdminElectionDto> response = evsVoterService.getElectionByCode("existingCode");
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
    }
    
    @Test
    public void testGetElectionByCode_NonExistingCode_ThrowsException() {
        when(evsAdminElectionRepository.findByElectionCode(anyString())).thenReturn(Optional.empty());
        assertThrows(NoElectionDetailsFoundException.class, () -> {
            evsVoterService.getElectionByCode("nonExistingCode");
        });
    }
    
    
    //Download Voter Card:
    
    @Test
    void testDownloadEVoterCard_SuccessfulDownload() throws IOException, DownloadFailedException {
        // Mocking the repository 
        EVSVoter evsVoter = new EVSVoter();
        evsVoter.setEvsVoterStatus("APPROVED"); // Setting the status as approved as download should be successfull
        evsVoter.setAadharCard(new byte[]{1, 2, 3}); // Mocking voter card data
        when(evsVoterRepository.findByVoterId(any())).thenReturn(Optional.of(evsVoter));

        // Invoke the service method
        ByteArrayInputStream voterCard = evsVoterService.downloadEVoterCard("validVoterId");

        // Verifying the results
        assertNotNull(voterCard); //verifies that the ByteArrayInputStream returned by the service method is not null, indicating that the download operation was successful and a stream object was created.
        assertTrue(voterCard.available() > 0); // Check if data is present in the stream
    }
   

    @Test
    void testDownloadEVoterCard_VoterNotApproved_ThrowsException() {
        EVSVoter evsVoter = new EVSVoter();
        evsVoter.setEvsVoterStatus("PENDING");
        when(evsVoterRepository.findByVoterId(any())).thenReturn(Optional.of(evsVoter));


        assertThrows(DownloadFailedException.class, () -> {
            evsVoterService.downloadEVoterCard("validVoterId");
        });
    }

    
    //Cast Your Vote
    
    @Test
    void testCastYourVote_VoterStatusNotApproved_ReturnsNonAuthoritativeInformation() throws PartyCodeNotMatchingException, NoElectionDetailsFoundException, AlreadyVoteCastedException, ElectionCodeMisMatchException {
        EVSVoter voter = new EVSVoter();
        voter.setEvsVoterStatus(EVSVoterConstants.STATUS_WHILE_REGISTERING_PENDING);
        when(evsVoterRepository.findByVoterId(anyString())).thenReturn(Optional.of(voter));

        ResponseEntity<String> response = evsVoterService.castYourVote("voterId", "electionCode", "partyCode");

        assertEquals(HttpStatus.NON_AUTHORITATIVE_INFORMATION, response.getStatusCode());
        assertTrue(response.getBody().contains("pending"));
    }


    
    
    
    
    
}
