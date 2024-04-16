package com.evs.admin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.ALREADY_REPORTED)
public class PartyCodeAlreadyPresent extends Exception {

    public PartyCodeAlreadyPresent(String message) {
        super(message);
    }

}
