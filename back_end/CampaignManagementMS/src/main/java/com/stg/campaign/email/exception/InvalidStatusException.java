package com.stg.campaign.email.exception;

public class InvalidStatusException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidStatusException(String message) {
		super(message);
	}
}