package com.evs.admin.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/*
 * @author ymarni
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(ElectionCodeAlreadyPresent.class)
	public ResponseEntity<ErrorDetails> handleElectionCodeAlreadyPresent(WebRequest webRequest,Exception exception)
	{
		ErrorDetails errorDetails = new ErrorDetails(
				LocalDateTime.now(),
				exception.getMessage(),
				webRequest.getDescription(false),
				"ALREADY REPORTED");
		return new ResponseEntity<ErrorDetails>(errorDetails,HttpStatus.ALREADY_REPORTED);
	}
	
	@ExceptionHandler(NoElectionDetailsFoundException.class)
	public ResponseEntity<ErrorDetails> handleNoElectionDetailsFoundException(WebRequest webRequest,Exception exception)
	{
		ErrorDetails errorDetails = new ErrorDetails(
				LocalDateTime.now(),
				exception.getMessage(),
				webRequest.getDescription(false),
				"NOT FOUND");
		return new ResponseEntity<ErrorDetails>(errorDetails,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(ElectionCodeMisMatchException.class)
	public ResponseEntity<ErrorDetails> handleElectionCodeMisMatchException(WebRequest webRequest,Exception exception)
	{
		ErrorDetails errorDetails = new ErrorDetails(
				LocalDateTime.now(),
				exception.getMessage(),
				webRequest.getDescription(false),
				"NOT ACCEPTABLE");
		return new ResponseEntity<ErrorDetails>(errorDetails,HttpStatus.NOT_ACCEPTABLE);
	}
	@ExceptionHandler(CandidateAlreadyPresentException.class)
	public ResponseEntity<ErrorDetails> handleCandidateAlreadyPresentException(WebRequest webRequest,Exception exception)
	{
		ErrorDetails errorDetails = new ErrorDetails(
				LocalDateTime.now(),
				exception.getMessage(),
				webRequest.getDescription(false),
				"ALREADY REPORTED");
		return new ResponseEntity<ErrorDetails>(errorDetails,HttpStatus.ALREADY_REPORTED);
	}
	@ExceptionHandler(NoCandidateFoundException.class)
	public ResponseEntity<ErrorDetails> handleNoCandidateFoundException(WebRequest webRequest,Exception exception)
	{
		ErrorDetails errorDetails = new ErrorDetails(
				LocalDateTime.now(),
				exception.getMessage(),
				webRequest.getDescription(false),
				"NOT FOUND");
		return new ResponseEntity<ErrorDetails>(errorDetails,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(CandidateCodeMisMatchException.class)
	public ResponseEntity<ErrorDetails> handleCandidateCodeMisMatchException(WebRequest webRequest,Exception exception)
	{
		ErrorDetails errorDetails = new ErrorDetails(
				LocalDateTime.now(),
				exception.getMessage(),
				webRequest.getDescription(false),
				"NOT ACCEPTABLE");
		return new ResponseEntity<ErrorDetails>(errorDetails,HttpStatus.NOT_ACCEPTABLE);
	}
	@ExceptionHandler(NoPartyDetailsFoundException.class)
	public ResponseEntity<ErrorDetails> handleNoPartyDetailsFoundException(WebRequest webRequest,Exception exception)
	{
		ErrorDetails errorDetails = new ErrorDetails(
				LocalDateTime.now(),
				exception.getMessage(),
				webRequest.getDescription(false),
				"NOT FOUND");
		return new ResponseEntity<ErrorDetails>(errorDetails,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(PartyCodeMismatchException.class)
	public ResponseEntity<ErrorDetails> handlePartyCodeMismatchException(WebRequest webRequest,Exception exception)
	{
		ErrorDetails errorDetails = new ErrorDetails(
				LocalDateTime.now(),
				exception.getMessage(),
				webRequest.getDescription(false),
				"NOT ACCEPTABLE");
		return new ResponseEntity<ErrorDetails>(errorDetails,HttpStatus.NOT_ACCEPTABLE);
	}
	@ExceptionHandler(PartyCodeAlreadyPresent.class)
	public ResponseEntity<ErrorDetails> handlePartyCodeAlreadyPresent(WebRequest webRequest,Exception exception)
	{
		ErrorDetails errorDetails = new ErrorDetails(
				LocalDateTime.now(),
				exception.getMessage(),
				webRequest.getDescription(false),
				"ALREADY REPORTED");
		return new ResponseEntity<ErrorDetails>(errorDetails,HttpStatus.ALREADY_REPORTED);
	}
	@ExceptionHandler(ElectionResultAlreadyDeclaredException.class)
	public ResponseEntity<ErrorDetails> handleElectionResultAlreadyDeclaredException(WebRequest webRequest,Exception exception)
	{
		ErrorDetails errorDetails = new ErrorDetails(
				LocalDateTime.now(),
				exception.getMessage(),
				webRequest.getDescription(false),
				"ALREADY REPORTED");
		return new ResponseEntity<ErrorDetails>(errorDetails,HttpStatus.ALREADY_REPORTED);
	}
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid (MethodArgumentNotValidException ex,
	HttpHeaders headers, HttpStatusCode status, WebRequest request) {
	
	Map<String, String> errors = new HashMap<>();
	List<ObjectError> errorList = ex.getBindingResult().getAllErrors();
	errorList.forEach((error) ->{
	
	String fieldName = ((FieldError) error).getField(); String message = error.getDefaultMessage(); errors.put(fieldName, message);
	});
	return new ResponseEntity<> (errors, HttpStatus.BAD_REQUEST);
}}
