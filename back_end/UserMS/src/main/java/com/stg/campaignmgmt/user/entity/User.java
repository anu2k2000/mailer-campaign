package com.stg.campaignmgmt.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {
	
    
	@Column(nullable = false, length = 50)
	String userName;
	@Column(nullable = false)
	@Id
	@GeneratedValue
	Integer userId;
	@Column(nullable = false, length = 50)
	String userRole;
	@Column(nullable = false, length = 50)
	String password;
	@Column(nullable = false, length = 50)
	String gender;
	@Column(nullable = false, length = 50)
	String firstName;
	@Column(nullable = false, length = 50)
	String lastName;
	@Column(nullable = false, length = 50)
	String emailId;
	@Column(nullable = false, length = 10)
	Integer age;


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public Integer getUserId() {
		return userId;
	}


	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	public String getUserRole() {
		return userRole;
	}


	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getEmailId() {
		return emailId;
	}


	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}


	public Integer getAge() {
		return age;
	}


	public void setAge(Integer age) {
		this.age = age;
	}


	public User() {
		super();
	}

}
