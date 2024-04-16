package com.evs.admin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * @author ymarni
 */
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class ElectionCodeMisMatchException extends Exception {

	public ElectionCodeMisMatchException(String message) {
		super(message);
		
	}

}
