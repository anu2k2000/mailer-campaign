package com.stg.campaign.email.dto;

import java.util.HashMap;
import java.util.List;

public class SendEmailDTO {

	Integer templateId;
	HashMap<String, String> paramValues;
	List<String> userNames;
	List<String> dynamicParams;
	String htmlContent;
	HashMap<String, UserDTO> userDTOs;

	public SendEmailDTO() {
		super();
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public HashMap<String, String> getParamValues() {
		return paramValues;
	}

	public void setParamValues(HashMap<String, String> paramValues) {
		this.paramValues = paramValues;
	}

	public List<String> getUserNames() {
		return userNames;
	}

	public void setUserNames(List<String> userNames) {
		this.userNames = userNames;
	}

	public List<String> getDynamicParams() {
		return dynamicParams;
	}

	public void setDynamicParams(List<String> dynamicParams) {
		this.dynamicParams = dynamicParams;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public HashMap<String, UserDTO> getUserDTOs() {
		return userDTOs;
	}

	public void setUserDTOs(HashMap<String, UserDTO> userDTOs) {
		this.userDTOs = userDTOs;
	}

}
