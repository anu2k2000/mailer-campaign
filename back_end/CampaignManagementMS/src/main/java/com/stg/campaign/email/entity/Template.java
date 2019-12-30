package com.stg.campaign.email.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Template {
	
    
	@Column(nullable = false, length = 50)
	@Id 
	@GeneratedValue
	Integer templateId;
	@Column( nullable = false, length = 50)
	String templateName;
	@Lob
	String sections;
	@Lob
	String  content;
	String dynamicParams;
	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	@Column(nullable = false, length = 100)
	String userName;
	

	public String getSections() {
		return sections;
	}


	public void setSections(String sections) {
		this.sections = sections;
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


	public Template() {
		super();
	}
	
	
}
