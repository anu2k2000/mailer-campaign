package com.stg.campaign.email.exception;
public class InvalidCategoryException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidCategoryException(String message) {
		super(message);
	}
}