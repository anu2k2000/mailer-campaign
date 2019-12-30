package com.stg.campaignmgmt.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.stg.campaignmgmt.user.dto.LoginStatusDTO;
import com.stg.campaignmgmt.user.dto.UserDTO;
import com.stg.campaignmgmt.user.exception.InvalidCredentialException;
import com.stg.campaignmgmt.user.exception.InvalidUserRoleException;
import com.stg.campaignmgmt.user.exception.UserAlreadyPresentException;
import com.stg.campaignmgmt.user.exception.UserNotFoundException;
import com.stg.campaignmgmt.user.service.UserService;

@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:2500"})
@RestController
@RequestMapping("users")
public class UserController {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserService userService;

	// Login
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public LoginStatusDTO login(@RequestBody UserDTO userDTO) throws InvalidCredentialException, UserNotFoundException {
		logger.info("Login request for user {} with password {}", userDTO.getUserName(), userDTO.getPassword());
		LoginStatusDTO loginStatusDTO = userService.login(userDTO);
		logger.info("Login request complete {}", loginStatusDTO.getUserRole());
		return loginStatusDTO;
	}

	@RequestMapping(value = "/addUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public UserDTO createCustomer(@RequestBody UserDTO userDTO)
			throws UserAlreadyPresentException, InvalidUserRoleException {
		logger.info("Creation request for user {}", userDTO);
		return userService.createUser(userDTO);
	}

	@RequestMapping(value = "/getUser/{userName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDTO getUser(@PathVariable String userName) throws UserNotFoundException {
		logger.info("Fetching user");
		return userService.getUser(userName);
	}
}
