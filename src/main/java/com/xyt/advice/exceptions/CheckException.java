package com.xyt.advice.exceptions;

import lombok.Data;

@Data
public class CheckException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 出错的字段
	 */
	private String location;
	/**
	 * 出错原因
	 */
	private String reason;
	
	
	public CheckException() {
		super();
	}

	public CheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CheckException(String message, Throwable cause) {
		super(message, cause);
	}

	public CheckException(String message) {
		super(message);
	}

	public CheckException(Throwable cause) {
		super(cause);
	}

	public CheckException(String location, String reason) {
		super();
		this.location = location;
		this.reason = reason;
	}
	
}
