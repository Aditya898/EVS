package com.evs.voter.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NoVoterFoundException.class)
	public ResponseEntity<ErrorDetails> handleNoVoterFoundException(WebRequest webRequest,Exception exception)
	{
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
				exception.getMessage(),
				webRequest.getDescription(false),
				"NOT FOUND");
		return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DownloadFailedException.class)
	public ResponseEntity<ErrorDetails> handleDownloadFailedExceptionn(WebRequest webRequest,Exception exception)
	{
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
				exception.getMessage(),
				webRequest.getDescription(false),
				"FAILED TO DOWNLOAD");
		return new ResponseEntity<>(errorDetails,HttpStatus.FAILED_DEPENDENCY);
	}
	@ExceptionHandler(AlreadyVoteCastedException.class)
	public ResponseEntity<ErrorDetails> handleAlreadyVoteCastedException(WebRequest webRequest,Exception exception)
	{
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
				exception.getMessage(),
				webRequest.getDescription(false),
				"ALREADY CASTED");
		return new ResponseEntity<>(errorDetails,HttpStatus.ALREADY_REPORTED);
	}
	@ExceptionHandler(ElectionCodeMisMatchException.class)
	public ResponseEntity<ErrorDetails> handleElectionCodeMisMatchException(WebRequest webRequest,Exception exception)
	{
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
				exception.getMessage(),
				webRequest.getDescription(false),
				"ELECTION CODE MISMATCH");
		return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(PartyCodeNotMatchingException.class)
	public ResponseEntity<ErrorDetails> handlePartyCodeNotMatchingException(WebRequest webRequest,Exception exception)
	{
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
				exception.getMessage(),
				webRequest.getDescription(false),
				"PARTY CODE MISMATCH");
		return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
	}

}
