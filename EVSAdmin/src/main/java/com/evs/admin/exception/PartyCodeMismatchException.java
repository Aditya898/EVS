package com.evs.admin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class PartyCodeMismatchException extends Exception {

    public PartyCodeMismatchException(String message) {
        super(message);
    }

}
