package com.stg.campaign.email.exception;
public class InvalidUserRoleException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidUserRoleException(String message) {
		super(message);
	}
}