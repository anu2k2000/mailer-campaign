package com.stg.campaign.email.controller;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.stg.campaign.email.dto.EmailDTO;
import com.stg.campaign.email.dto.SendEmailDTO;
import com.stg.campaign.email.dto.TemplateDTO;
import com.stg.campaign.email.dto.UserDTO;
import com.stg.campaign.email.exception.InvalidCategoryException;
import com.stg.campaign.email.exception.InvalidInputException;
import com.stg.campaign.email.exception.InvalidStatusException;
import com.stg.campaign.email.exception.TemplateNotFoundException;
import com.stg.campaign.email.service.EmailService;

@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:2500"})
@RestController
@RequestMapping("emails")
public class EmailController {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	EmailService emailService;

	@Value("${user.uri}")
	String userUri;

	@RequestMapping(value = "/sendEmail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String sendEmail(@RequestBody SendEmailDTO sendEmailDTO) throws Exception {
		List<String> userNames = sendEmailDTO.getUserNames();
		UserDTO userDTO = null;
		HashMap<String, UserDTO> userDTOs = new HashMap<String, UserDTO>();
		for (String userName : userNames) {
			userDTO = new RestTemplate().getForObject(userUri + userName, UserDTO.class);
			userDTOs.put(userName, userDTO);
		}
		sendEmailDTO.setUserDTOs(userDTOs);
		// emailService.sendEmail(sendEmailDTO);
		emailService.sendSGEmail(sendEmailDTO);
		return "Email Sent!";

	}

	// Login
	@RequestMapping(value = "/getAllEmails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<EmailDTO> viewEmails() {
		logger.info("Fetching all defects");
		return emailService.getAllEmails();
	}

	@RequestMapping(value = "/addEmail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public EmailDTO addEmail(@RequestBody EmailDTO emailDTO)
			throws InvalidInputException, InvalidCategoryException, InvalidStatusException {
		logger.info("Creation request for email {}", emailDTO);
		return emailService.createEmail(emailDTO);
	}

	@RequestMapping(value = "/addTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public TemplateDTO addTemplate(@RequestBody TemplateDTO templateDTO) throws InvalidInputException, JSONException {
		logger.info("Creation request for email {}", templateDTO);
		return emailService.createTemplate(templateDTO);
	}

	@RequestMapping(value = "/updateTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public TemplateDTO updateTemplate(@RequestBody TemplateDTO templateDTO)
			throws InvalidInputException, TemplateNotFoundException, JSONException {
		logger.info("Creation request for email {}", templateDTO);
		return emailService.updateTemplate(templateDTO);
	}

	@RequestMapping(value = "/getTemplate/{templateId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public TemplateDTO getUser(@PathVariable Long templateId) throws TemplateNotFoundException {
		logger.info("Fetching template");
		return emailService.getTemplate(templateId);
	}

	// Login
	@RequestMapping(value = "/getTemplates", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<TemplateDTO> getTemplates() {
		logger.info("Fetching all templates");
		return emailService.getTemplates();
	}

	/*
	 * @RequestMapping(value = "/updateDefect/{defectId}", method =
	 * RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE) public
	 * EmailDTO closeDefect(@PathVariable Long defectId) throws
	 * DefectNotFoundException { logger.info("Request to close defect {}",
	 * defectId); return defectService.closeDefect(defectId); }
	 */
}
