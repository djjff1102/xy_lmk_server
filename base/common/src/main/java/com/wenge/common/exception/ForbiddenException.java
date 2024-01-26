package com.wenge.common.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseException {

	private static final long serialVersionUID = -1869954642901382881L;

	public ForbiddenException() {
		super(HttpStatus.FORBIDDEN.getReasonPhrase());
	}

}
