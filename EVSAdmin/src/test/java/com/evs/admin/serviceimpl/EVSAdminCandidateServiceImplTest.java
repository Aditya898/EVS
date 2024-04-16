package com.evs.admin.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import com.evs.admin.dto.EVSAdminCandidateDto;
import com.evs.admin.exception.CandidateAlreadyPresentException;
import com.evs.admin.exception.CandidateCodeMisMatchException;
import com.evs.admin.exception.NoCandidateFoundException;
import com.evs.admin.model.EVSAdminCandidate;
import com.evs.admin.model.EVSAdminParty;
import com.evs.admin.repository.EVSAdminCandidateRepository;
import com.evs.admin.repository.EVSAdminPartyRepository;
import com.evs.admin.serviceimpl.EVSAdminCandidateServiceImpl;
/*
 * @author aditaitkar
 */

@SpringBootTest 
public class EVSAdminCandidateServiceImplTest {

    @Mock
    private EVSAdminCandidateRepository candidateRepository;

    @Mock
    private EVSAdminPartyRepository partyRepository;

    @InjectMocks
    private EVSAdminCandidateServiceImpl candidateService;

    

    
    @Test
    public void testSaveCandidate_AlreadyPresent() {
        EVSAdminCandidateDto candidateDto = new EVSAdminCandidateDto();
        candidateDto.setCandidateId("CAND123");
        when(candidateRepository.findByCandidateId(anyString())).thenReturn(Optional.of(new EVSAdminCandidate()));
        assertThrows(CandidateAlreadyPresentException.class, () -> candidateService.saveCandidate(candidateDto));
    }

   
    @Test
    public void testUpdateCandidate_NoCandidateFound() {
        EVSAdminCandidateDto candidateDto = new EVSAdminCandidateDto();
        when(candidateRepository.findByCandidateId(anyString())).thenReturn(Optional.empty());
        assertThrows(NoCandidateFoundException.class, () -> candidateService.updateCandidate(candidateDto, "CAND123"));
    }

    //works
    @Test
    public void testDeleteCandidate_Success() throws NoCandidateFoundException {
        EVSAdminCandidate candidate = new EVSAdminCandidate();
        candidate.setCandidateId("CAND123");
        when(candidateRepository.findByCandidateId(anyString())).thenReturn(Optional.of(candidate));
        ResponseEntity<String> response = candidateService.deleteCandidate("CAND123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    
    @Test
    public void testDeleteCandidate_NoCandidateFound() {
        when(candidateRepository.findByCandidateId(anyString())).thenReturn(Optional.empty());
        assertThrows(NoCandidateFoundException.class, () -> candidateService.deleteCandidate("CAND123"));
    }

    @Test
    public void testGetCandidate_Success() throws NoCandidateFoundException {
        EVSAdminCandidate candidate = new EVSAdminCandidate();
        candidate.setCandidateId("CAND123");
        when(candidateRepository.findByCandidateId(anyString())).thenReturn(Optional.of(candidate));
        ResponseEntity<EVSAdminCandidateDto> response = candidateService.getCandidate("CAND123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetCandidate_NoCandidateFound() {
        when(candidateRepository.findByCandidateId(anyString())).thenReturn(Optional.empty());
        assertThrows(NoCandidateFoundException.class, () -> candidateService.getCandidate("CAND123"));
    }

   
    @Test
    public void testGetAllCandidates() {
        List<EVSAdminCandidate> candidates = new ArrayList<>();
        candidates.add(new EVSAdminCandidate());
        when(candidateRepository.findAll()).thenReturn(candidates);
        ResponseEntity<List<EVSAdminCandidateDto>> response = candidateService.getAllCandidates();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
