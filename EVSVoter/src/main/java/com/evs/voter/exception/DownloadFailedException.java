package com.evs.voter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FAILED_DEPENDENCY)
public class DownloadFailedException extends Exception {	

	public DownloadFailedException(String message) {
		super(message);
		
	}

	

}
