package com.evs.admin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * @author ymarni
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoElectionDetailsFoundException extends Exception {	

	public NoElectionDetailsFoundException(String message) {
		super(message);
		
	}
	

}
