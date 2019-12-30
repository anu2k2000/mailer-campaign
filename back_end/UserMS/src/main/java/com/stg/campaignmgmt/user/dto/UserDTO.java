package com.stg.campaignmgmt.user.dto;

import com.stg.campaignmgmt.user.entity.User;

public class UserDTO {

	String userName;
	Integer userId;
	String userRole;
	String password;
	String firstName;
	String lastName;
	Integer age;
	String gender;
	String emailId;

	public UserDTO() {
		super();
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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	// Converts Entity into DTO
	public static UserDTO valueOf(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setAge(user.getAge());
		userDTO.setEmailId(user.getEmailId());
		userDTO.setPassword(user.getPassword());
		userDTO.setUserName(user.getUserName());
		userDTO.setUserRole(user.getUserRole());
		userDTO.setGender(user.getGender());
		userDTO.setFirstName(user.getFirstName());
		userDTO.setLastName(user.getLastName());
		userDTO.setUserId(user.getUserId());
		return userDTO;
	}

	// Converts DTO into Entity
	public User createEntity() {
		User user = new User();
		user.setAge(this.getAge());
		user.setEmailId(this.getEmailId());
		user.setPassword(this.getPassword());
		user.setUserName(this.getUserName());
		user.setUserRole(this.getUserRole());
		user.setUserId(this.getUserId());
		user.setFirstName(this.getFirstName());
		user.setLastName(this.getLastName());
		user.setGender(this.getGender());
		return user;
	}
}
