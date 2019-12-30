package com.stg.campaignmgmt.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.stg.campaignmgmt.user.dto.LoginStatusDTO;
import com.stg.campaignmgmt.user.dto.UserDTO;
import com.stg.campaignmgmt.user.entity.User;
import com.stg.campaignmgmt.user.exception.InvalidCredentialException;
import com.stg.campaignmgmt.user.exception.InvalidUserRoleException;
import com.stg.campaignmgmt.user.exception.UserAlreadyPresentException;
import com.stg.campaignmgmt.user.exception.UserNotFoundException;
import com.stg.campaignmgmt.user.repository.UserRepository;

@Service
public class UserService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	UserRepository userRepository;
	@Autowired
	private Environment environment;

	public LoginStatusDTO login(UserDTO userDTO) throws InvalidCredentialException, UserNotFoundException {
		logger.info("In Service Login request for user {} with password {}", userDTO.getUserName(),
				userDTO.getPassword());
		User user = userRepository.findByUserName(userDTO.getUserName());
		if (user == null || user.getUserName().isEmpty()) {
			throw new UserNotFoundException(environment.getProperty("UserService.USER_NOT_FOUND"));
		}
		if (!((user != null) && (user.getPassword().equals(userDTO.getPassword())))) {
			throw new InvalidCredentialException(environment.getProperty("UserService.INVALID_CREDENTIALS"));
		}
		LoginStatusDTO loginStatusDTO = new LoginStatusDTO();
		loginStatusDTO.setUserRole(user.getUserRole());

		return loginStatusDTO;
	}

	public UserDTO createUser(UserDTO userDTO) throws UserAlreadyPresentException, InvalidUserRoleException {
		logger.info("Creation request for customer {}", userDTO);
		User user = userRepository.findByUserName(userDTO.getUserName());
		if (user == null || user.getUserName().isEmpty()) {
			user = userDTO.createEntity();
		} else {
			throw new UserAlreadyPresentException(environment.getProperty("UserService.USERID_PRESENT"));
		}

		if (!(user.getUserRole().equalsIgnoreCase(environment.getProperty("UserService.MARKETING_MANAGER"))
				|| user.getUserRole().equalsIgnoreCase(environment.getProperty("UserService.TEMPLATE_OWNER"))
				|| user.getUserRole().equalsIgnoreCase(environment.getProperty("UserService.CRM_USER")))) {
			throw new InvalidUserRoleException(environment.getProperty("UserService.INVALID_ROLE"));
		}
		User createdUser = userRepository.save(user);
		return UserDTO.valueOf(createdUser);
	}

	public UserDTO getUser(String userName) throws UserNotFoundException {
		logger.info("In Service Get request for user {} ", userName);
		User user = userRepository.findByUserName(userName);
		if (user == null || user.getUserName().isEmpty()) {
			throw new UserNotFoundException(environment.getProperty("UserService.USER_NOT_FOUND"));
		}
		return UserDTO.valueOf(user);
	}
}
