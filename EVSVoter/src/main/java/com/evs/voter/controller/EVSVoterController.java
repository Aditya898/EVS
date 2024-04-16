package com.evs.voter.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;

import com.evs.voter.dto.EVSAdminElectionDto;
import com.evs.voter.dto.EVSAdminPartyDto;
import com.evs.voter.dto.EVSVoterCastVoteDto;
import com.evs.voter.dto.EVSVoterDto;
import com.evs.voter.exception.AlreadyVoteCastedException;
import com.evs.voter.exception.DownloadFailedException;
import com.evs.voter.exception.ElectionCodeMisMatchException;
import com.evs.voter.exception.NoElectionDetailsFoundException;
import com.evs.voter.exception.NoPartyDetailsFoundException;
import com.evs.voter.exception.PartyCodeNotMatchingException;
import com.evs.voter.model.EVSVoter;
import com.evs.voter.model.EVSVoterCastVote;
import com.evs.voter.serviceimpl.EVSVoterServiceImpl;

@RestController
@RequestMapping(value = "voterAccess")
public class EVSVoterController {
	
	@Autowired
	private EVSVoterServiceImpl evsVoterServiceImpl;
	
	@PostMapping("registerAsNewVoter")
	public ResponseEntity<String> registerAsNewVoter(@RequestParam String userName,
			@RequestParam String firstName,
    		@RequestParam String lastName,
    		@RequestParam String gender,
    		@RequestParam String email,
    		@RequestParam String dateOfBirth,
    		@RequestParam int age,
    		@RequestParam String contactNo,
    		@RequestParam String address,
    		@RequestParam String nationality,
			@RequestParam("file") MultipartFile file) throws IOException
	{
		return evsVoterServiceImpl.registerAsNewVoter(userName,firstName,lastName,gender,email,dateOfBirth,age,
				contactNo,address,nationality,file);
		
	}
	@GetMapping("/downloadImage/{imageId}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable String imageId) {
        // Retrieve image data from MongoDB
        byte[] imageData = evsVoterServiceImpl.downloadImage(imageId);
        
        // Set content type as image/jpeg (or appropriate content type)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        
        // Set content length
        headers.setContentLength(imageData.length);
        
        // Return image data as response
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }
    @GetMapping("checkStatus/{userName}")
    public ResponseEntity<String> checkStatus(@PathVariable String userName)
    {
    	return evsVoterServiceImpl.checkStatus(userName);
    }
    @GetMapping("/downloadEVoterCard/{voterId}")
    public ResponseEntity<InputStreamResource> downloadEVoterCard(@PathVariable String voterId) throws DownloadFailedException
    {
    	ByteArrayInputStream voterCard = evsVoterServiceImpl.downloadEVoterCard(voterId);
    	HttpHeaders httpHeaders = new HttpHeaders();
    	httpHeaders.add("Content-Disposition","inline;file=lcwd.pdf");
    	return ResponseEntity.ok()
    			.headers(httpHeaders)
    			.contentType(MediaType.APPLICATION_PDF)
    			.body(new InputStreamResource(voterCard));    	
    }
    
    @GetMapping("getElection/{electionCode}")
	public ResponseEntity<EVSAdminElectionDto> getElectionBycode(@PathVariable String electionCode) throws NoElectionDetailsFoundException
	{
		return evsVoterServiceImpl.getElectionByCode(electionCode);
	}
    @PostMapping("castVote")
    public ResponseEntity<String> castVote(@RequestBody EVSVoterCastVoteDto evsVoterCastVoteDto) throws PartyCodeNotMatchingException, NoElectionDetailsFoundException, AlreadyVoteCastedException, ElectionCodeMisMatchException
    {
    	return evsVoterServiceImpl.castYourVote(evsVoterCastVoteDto.getVoterId(), evsVoterCastVoteDto.getElectionCode(), evsVoterCastVoteDto.getPartyCode());
    }
    
    @GetMapping("getResult/{electionCode}")
    public ResponseEntity<String> getResults(@PathVariable String electionCode)
    {
    	return evsVoterServiceImpl.checkResults(electionCode);
    }
   
}