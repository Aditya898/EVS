package com.evs.admin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class CandidateCodeMisMatchException extends Exception {

	public CandidateCodeMisMatchException(String message) {
		super(message);
	
	}

}
