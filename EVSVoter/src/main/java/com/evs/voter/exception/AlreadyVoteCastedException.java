package com.evs.voter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.ALREADY_REPORTED)
public class AlreadyVoteCastedException extends Exception {

	
	public AlreadyVoteCastedException(String message) {
		super(message);
	}

	

}
