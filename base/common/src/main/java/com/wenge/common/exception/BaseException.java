package com.wenge.common.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
	private static final long serialVersionUID = 6064901160073757283L;

	private String code;

	private String[] args;

	public BaseException(String message) {
		super(message);
	}

	public BaseException(String message, String[] args) {
		super(message);
		this.args = args;
	}

	public BaseException(String message, String code) {
		super(message);
		this.code = code;
	}

//	@Override
//	public String getMessage() {
//		return MessageUtil.getMessage(super.getMessage(), this.args);
//	}
}
