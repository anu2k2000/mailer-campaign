package com.stg.campaign.email.dto;

import com.stg.campaign.email.entity.Email;

public class EmailDTO {

	String emailId;
	Integer templateId;
	String name;
	String age;
	String token;
	String status;

	

	public EmailDTO() {
		super();
	}


	public String getEmailId() {
		return emailId;
	}


	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}


	public Integer getTemplateId() {
		return templateId;
	}


	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getAge() {
		return age;
	}


	public void setAge(String age) {
		this.age = age;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	// Converts Entity into DTO
	public static EmailDTO valueOf(Email email) {
		EmailDTO emailDTO = new EmailDTO();
		emailDTO.setAge(email.getAge());
		emailDTO.setEmailId(email.getEmailId());
		emailDTO.setName(email.getName());
		emailDTO.setStatus(email.getStatus());
		emailDTO.setTemplateId(email.getTemplateId());
		emailDTO.setToken(email.getToken());
		return emailDTO;
	}

	// Converts DTO into Entity
	public Email createEntity() {
		Email email = new Email();
		email.setAge(this.getAge());
		email.setEmailId(this.getEmailId());
		email.setName(this.getName());
		email.setStatus(this.getStatus());
		email.setTemplateId(this.getTemplateId());
		email.setToken(this.getToken());
		return email;
	}
}
