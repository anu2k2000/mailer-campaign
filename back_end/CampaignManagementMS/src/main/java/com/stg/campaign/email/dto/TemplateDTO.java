package com.stg.campaign.email.dto;


import com.stg.campaign.email.entity.Template;

public class TemplateDTO {

	Integer templateId;
	String templateName;
	String content;
	String sections;
	String dynamicParams;
	String userName;
	

	public TemplateDTO() {
		super();
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public Integer getTemplateId() {
		return templateId;
	}



	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}



	public String getTemplateName() {
		return templateName;
	}



	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}



	public String getDynamicParams() {
		return dynamicParams;
	}



	public void setDynamicParams(String dynamicParams) {
		this.dynamicParams = dynamicParams;
	}







	public String  getSections() {
		return sections;
	}



	public void setSections(String  sections) {
		this.sections = sections;
	}



	// Converts Entity into DTO
	public static TemplateDTO valueOf(Template template){
		TemplateDTO templateDTO = new TemplateDTO();
		templateDTO.setContent(template.getContent());
		templateDTO.setDynamicParams(template.getDynamicParams());
		templateDTO.setTemplateId(template.getTemplateId());
		templateDTO.setTemplateName(template.getTemplateName());
		templateDTO.setSections(template.getSections());
		templateDTO.setUserName(template.getUserName());
		return templateDTO;
	}

	// Converts DTO into Entity
	public Template createEntity() {
		Template template = new Template();
		template.setContent(this.getContent());
		template.setDynamicParams(this.getDynamicParams());
		template.setTemplateId(this.getTemplateId());
		template.setTemplateName(this.getTemplateName());
		template.setSections(this.sections);
		template.setUserName(this.getUserName());
		return template;
	}
}
