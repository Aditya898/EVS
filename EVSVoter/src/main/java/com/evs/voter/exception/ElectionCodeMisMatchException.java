package com.evs.voter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ElectionCodeMisMatchException extends Exception {
	
	public ElectionCodeMisMatchException(String message) {
		super(message);
	}


}
