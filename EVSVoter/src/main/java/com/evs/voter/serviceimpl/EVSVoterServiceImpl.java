package com.evs.voter.serviceimpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
import com.evs.voter.model.EVSAdminParty;
import com.evs.voter.model.EVSVoter;
import com.evs.voter.model.EVSVoterCastVote;
import com.evs.voter.repository.EVSAdminElectionRepository;
import com.evs.voter.repository.EVSVoterCastVoteRepository;
import com.evs.voter.repository.EVSVoterRepository;
import com.evs.voter.service.EVSResultApiClient;
import com.evs.voter.service.EVSVoterService;
import com.evs.voter.utils.EVSVoterAdharCardUtils;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

/*
 * @Author ymarni
 */
@Service
public class EVSVoterServiceImpl implements EVSVoterService {

	@Autowired
	private EVSVoterRepository evsVoterRepository;
	@Autowired
	private EVSAdminElectionRepository evsAdminElectionRepository;
	@Autowired
	private EVSVoterCastVoteRepository evsVoterCastVoteRepository;
	@Autowired
	private EVSResultApiClient evsResultApiClient;
	
	@Override
	public ResponseEntity<String> registerAsNewVoter(String userName,
			String firstName,
			String lastName,
			String email,
			String gender,
			String dateOfBirth,
			int age,
			String contactNo,
			String address,
			String nationality,
			MultipartFile file) throws IOException {
		UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString().replaceAll("-", "");       
        String voterid = uuidString.substring(0,10);       
		if(age<18)
		{
			return new ResponseEntity<>("Your age is not sufficient for registering as new voter.",HttpStatus.BAD_REQUEST);
		}
		Optional<EVSVoter> optionalEvsVoter = evsVoterRepository.findByUserName(userName);
		if(!optionalEvsVoter.isEmpty())
		{
			return new ResponseEntity<>("This user name is already taken, please try another.",HttpStatus.BAD_REQUEST);
 
		}
		EVSVoter evsVoter = new EVSVoter();
		evsVoter.setVoterId(voterid);
		evsVoter.setUserName(userName);
		evsVoter.setFirstName(firstName);
		evsVoter.setLastName(lastName);
		evsVoter.setGender(gender);
		evsVoter.setEmail(email);
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(dateOfBirth, formatter);
		evsVoter.setDateOfBirth(date);
		evsVoter.setAge(age);
		evsVoter.setContactNo(contactNo);
		evsVoter.setAddress(address);
		evsVoter.setNationality(nationality);
		evsVoter.setEvsVoterStatus(EVSVoterConstants.STATUS_WHILE_REGISTERING_PENDING);
		evsVoter.setAadharCard(EVSVoterAdharCardUtils.compressImage(file.getBytes()));
		EVSVoter evsVoter2 = evsVoterRepository.save(evsVoter);
		if (evsVoter2 != null)
		{			
			return new ResponseEntity<>(String.format("Your new voter request has been registered with the voter id %s, please check the status after 2 working days.", voterid),HttpStatus.CREATED);
		}
		return new ResponseEntity<>("Not Successfull",HttpStatus.BAD_REQUEST);
	}
	
	public byte[] downloadImage(String fileName)
	{
		Optional<EVSVoter>  imageData = evsVoterRepository.findById(fileName);
		byte[] image = (EVSVoterAdharCardUtils.decompressImage(imageData.get().getAadharCard()));
		return image;
	}
	
	public ResponseEntity<String> checkStatus(String userName) {
		Optional<EVSVoter> evsVoter = evsVoterRepository.findByUserName(userName);
		if(evsVoter.isEmpty())
		{
			throw new NoVoterFoundException("Sorry we couldn't load any user with the username "+ userName);
		}
		else if (evsVoter.get().getEvsVoterStatus().equals(EVSVoterConstants.STATUS_WHILE_REGISTERING_PENDING)) {
			return new ResponseEntity<String>(String.format("Your voter id status is still in %s state, so please have patience.", EVSVoterConstants.STATUS_WHILE_REGISTERING_PENDING),HttpStatus.OK);
		}
		else if (evsVoter.get().getEvsVoterStatus().equals(EVSVoterConstants.STATUS_AFTER_REGISTERING_DECLINED)) {
			return new ResponseEntity<String>(String.format("Sorry to inform that your request for new voter id request is %s", EVSVoterConstants.STATUS_AFTER_REGISTERING_DECLINED),HttpStatus.OK);
		}
		return new ResponseEntity<String>(String.format("Your request for new voter has been %s", EVSVoterConstants.STATUS_AFTER_REGISTERING_APPROVED),HttpStatus.OK);
	}
	
	public ByteArrayInputStream downloadEVoterCard(String voterId) throws DownloadFailedException
	{
		Optional<EVSVoter> optionalEvsVoter = evsVoterRepository.findByVoterId(voterId);
		if(optionalEvsVoter.isEmpty())
		{
			throw new NoVoterFoundException("No voter found with the given id");
		}
		else if(optionalEvsVoter.get().getEvsVoterStatus().equals(EVSVoterConstants.STATUS_AFTER_REGISTERING_APPROVED))
		{
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			Document document = new Document();
			PdfWriter.getInstance(document, outputStream);
			document.open();
			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD,25);
			String title = "ELECTION COMMISSION OF INDIA";
			Paragraph titleParagraph = new Paragraph(title,titleFont);
			titleParagraph.setAlignment(Element.ALIGN_CENTER);
			document.add(titleParagraph);
			
			Font voteridFont = FontFactory.getFont(FontFactory.TIMES_ROMAN,16);
			String 	vId = "VoterId : " + optionalEvsVoter.get().getVoterId();
			Paragraph voterIdParagraph = new Paragraph(vId,voteridFont);
			voterIdParagraph.setAlignment(Element.ALIGN_LEFT);
			document.add(voterIdParagraph);
			
			Font nameFont = FontFactory.getFont(FontFactory.TIMES_ROMAN,16);
			String name = "Name : " + optionalEvsVoter.get().getFirstName() + optionalEvsVoter.get().getLastName();
			Paragraph nameParagraph = new Paragraph(name,nameFont);
			nameParagraph.setAlignment(Element.ALIGN_LEFT);
			document.add(nameParagraph);
			
			Font dateOfBirthFont = FontFactory.getFont(FontFactory.TIMES_ROMAN,16);
			String dateOfBirth = "Date of birth : " + optionalEvsVoter.get().getDateOfBirth();
			Paragraph dateOfBirthParagraph = new Paragraph(dateOfBirth,dateOfBirthFont);
			dateOfBirthParagraph.setAlignment(Element.ALIGN_LEFT);
			document.add(dateOfBirthParagraph);
			
			Font genderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN,16);
			String gender = "Gender : " + optionalEvsVoter.get().getGender();
			Paragraph genderParagraph = new Paragraph(gender,genderFont);
			genderParagraph.setAlignment(Element.ALIGN_LEFT);
			document.add(genderParagraph);
			
			Font addressFont = FontFactory.getFont(FontFactory.TIMES_ROMAN,16);
			String address = "Address : " + optionalEvsVoter.get().getAddress();
			Paragraph addressParagraph = new Paragraph(address,addressFont);
			addressParagraph.setAlignment(Element.ALIGN_LEFT);
			document.add(addressParagraph);
			
			document.close();
			return new ByteArrayInputStream(outputStream.toByteArray());
		}
		throw new DownloadFailedException("Download Failed");
	}

	@Override
	public ResponseEntity<EVSAdminElectionDto> getElectionByCode(String electionCode) throws NoElectionDetailsFoundException {
		Optional<EVSAdminElection> optionalEvsAdminElection = evsAdminElectionRepository.findByElectionCode(electionCode);
		if(optionalEvsAdminElection.isEmpty())
		{
			throw new NoElectionDetailsFoundException("No election details found with election code "+electionCode);
	
		}
		EVSAdminElection evsAdminElection = optionalEvsAdminElection.get();
		EVSAdminElectionDto evsAdminElectionDto = new EVSAdminElectionDto(evsAdminElection.getId(),
				evsAdminElection.getElectionCode(),
				evsAdminElection.getElectionName(),
				evsAdminElection.getState(),
				evsAdminElection.getConstituency(),
				evsAdminElection.getEvsAdminParties());
		return new ResponseEntity<>(evsAdminElectionDto,HttpStatus.FOUND);
	}
	
	public ResponseEntity<String> castYourVote(String voterId,String electionCode,String partyCode) throws PartyCodeNotMatchingException,NoElectionDetailsFoundException, AlreadyVoteCastedException, ElectionCodeMisMatchException
	{
		Optional<EVSVoter> voterOptional = evsVoterRepository.findByVoterId(voterId);
		if(voterOptional.isPresent())
		{			
		
			if(voterOptional.get().getEvsVoterStatus().equals(EVSVoterConstants.STATUS_AFTER_REGISTERING_APPROVED))
			{
				Optional<EVSVoterCastVote> optional = evsVoterCastVoteRepository.findByVoterIdAndElectionCode(voterId,electionCode);
				if (optional.isPresent()) {
					throw new AlreadyVoteCastedException("You have already casted your vote.");			
				}
				else {
					Optional<EVSAdminElection> evsAdminElectionOptional = evsAdminElectionRepository.findByElectionCode(electionCode);
					if (evsAdminElectionOptional.isEmpty()) {
						throw new NoElectionDetailsFoundException("No election found");
					}
					else if (!evsAdminElectionOptional.get().getElectionCode().equals(electionCode)) {
						throw new ElectionCodeMisMatchException("Election code you entered doesn't exists. Please verify it");
					}
					EVSVoterCastVote evsVoterCastVote = new EVSVoterCastVote();
					
					List<EVSAdminParty> listOfParties = evsAdminElectionOptional.get().getEvsAdminParties();
					boolean isPartyPresent = false;
					for(int i=0;i<listOfParties.size();i++)
					{
						if(listOfParties.get(i).getPartyCode().equals(partyCode))
						{
							isPartyPresent = true;
						}
					}
					if(isPartyPresent)
					{
						List<EVSVoterCastVote> evsVoterCastVote2 = evsVoterCastVoteRepository.findAllByElectionCodeAndPartyCode(electionCode,partyCode);
						EVSVoterCastVote evsVoterCastVoteWithVoterID = evsVoterCastVoteRepository.findByElectionCodeAndPartyCodeAndVoterId(electionCode,partyCode,voterId);
						if(evsVoterCastVoteWithVoterID==null)
						{
							
							evsVoterCastVote.setVoterId(voterId);
							evsVoterCastVote.setElectionCode(electionCode);
							evsVoterCastVote.setPartyCode(partyCode);
							if(evsVoterCastVote2.isEmpty())
							{
								evsVoterCastVote.setNoOfVotes(1);
							}
							else {
								evsVoterCastVote.setNoOfVotes((evsVoterCastVote2.get(evsVoterCastVote2.size()-1)).getNoOfVotes()+1);
							}
							evsVoterCastVoteRepository.save(evsVoterCastVote);
							return new ResponseEntity<>("Your vote has been successfully casted.",HttpStatus.OK);
						}
						else {
							evsVoterCastVote.setVoterId(voterId);
							evsVoterCastVote.setElectionCode(electionCode);
							evsVoterCastVote.setPartyCode(partyCode);
							if(evsVoterCastVote2.isEmpty())
							{
								evsVoterCastVote.setNoOfVotes(1);
							}
							else {
								evsVoterCastVote.setNoOfVotes((evsVoterCastVote2.get(evsVoterCastVote2.size()-1)).getNoOfVotes()+1);
							}
							evsVoterCastVoteRepository.save(evsVoterCastVote);
							return new ResponseEntity<>("Your vote has been successfully casted.",HttpStatus.OK);
						}
						
					}
					else
					{
						throw new PartyCodeNotMatchingException(String.format("The party %s you wish to cast your vote is not participating in this election.", partyCode));
					}
				}
			}
			else {
				return new ResponseEntity<>("Your voter request is still in pending case, so you dont have right to cast your vote",HttpStatus.NON_AUTHORITATIVE_INFORMATION);
			}
		}
		else 
		{
			throw new NoVoterFoundException(String.format("No voter found with voter id %s", voterId));
		}
	}
	
	public ResponseEntity<String> checkResults(String electionCode)
	{
		return evsResultApiClient.checkResults(electionCode);
	}

	
	
}
