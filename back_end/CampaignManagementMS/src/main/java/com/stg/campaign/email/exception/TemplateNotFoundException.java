package com.stg.campaign.email.exception;
public class TemplateNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public TemplateNotFoundException(String message) {
		super(message);
	}
}