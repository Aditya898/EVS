package com.evs.voter.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.evs.voter.dto.EVSAdminElectionDto;
import com.evs.voter.dto.EVSVoterDto;
import com.evs.voter.exception.DownloadFailedException;
import com.evs.voter.exception.NoElectionDetailsFoundException;
import com.evs.voter.model.EVSVoter;

public interface EVSVoterService {


	ResponseEntity<String> registerAsNewVoter(String userName, String firstName, String lastName,String gender, String email, String dateOfBirth, int age,
			String contactNo, String address, String nationality, MultipartFile file) throws IOException;
	public ResponseEntity<String> checkStatus(String userName);
	public ByteArrayInputStream downloadEVoterCard(String voterId) throws DownloadFailedException;
	ResponseEntity<EVSAdminElectionDto> getElectionByCode(String electionCode) throws NoElectionDetailsFoundException;
}
