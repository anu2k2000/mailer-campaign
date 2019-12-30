package com.stg.campaignmgmt.user.exception;

@SuppressWarnings("serial")
public class UserAlreadyPresentException extends Exception {
	public UserAlreadyPresentException(String message) {
		super(message);
	}

}
