package com.evs.admin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.ALREADY_REPORTED)
public class CandidateAlreadyPresentException extends RuntimeException{

	public CandidateAlreadyPresentException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	
}
