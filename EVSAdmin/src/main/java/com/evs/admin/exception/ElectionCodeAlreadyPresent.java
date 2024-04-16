package com.evs.admin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * @author ymarni
 */
@ResponseStatus(value = HttpStatus.ALREADY_REPORTED)
public class ElectionCodeAlreadyPresent extends Exception {

	public ElectionCodeAlreadyPresent(String message) {
		super(message);
		
	}

}
