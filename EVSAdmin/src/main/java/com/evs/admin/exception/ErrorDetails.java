package com.evs.admin.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @author ymarni
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
	private LocalDateTime timeStamp;
	private String message;
	private String path;
	private String errorCode;
	

}
