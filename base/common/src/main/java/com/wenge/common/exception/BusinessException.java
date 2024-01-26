package com.wenge.common.exception;

public class BusinessException extends BaseException {

	private static final long serialVersionUID = 6576594359325328012L;

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, String[] args) {
		super(message, args);
	}

	public BusinessException(String message, String code) {
		super(message, code);
	}
}
