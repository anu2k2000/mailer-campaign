package com.stg.campaign.email.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import javax.mail.internet.MimeMessage;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.sendgrid.Attachments;
import com.sendgrid.Content;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.stg.campaign.email.dto.EmailDTO;
import com.stg.campaign.email.dto.SendEmailDTO;
import com.stg.campaign.email.dto.TemplateDTO;
import com.stg.campaign.email.dto.UserDTO;
import com.stg.campaign.email.entity.Email;
import com.stg.campaign.email.entity.Template;
import com.stg.campaign.email.exception.InvalidCategoryException;
import com.stg.campaign.email.exception.InvalidInputException;
import com.stg.campaign.email.exception.InvalidStatusException;
import com.stg.campaign.email.exception.TemplateNotFoundException;
import com.stg.campaign.email.repository.EmailRepository;
import com.stg.campaign.email.repository.TemplateRepository;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

@Service
public class EmailService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	EmailRepository emailRepository;
	@Autowired
	TemplateRepository templateRepository;
	@Value("${spring.sendgrid.api-key}")
	String sendGridAPIKey;

	@Autowired
	private JavaMailSender sender;

	@Autowired
	private Configuration freemarkerConfig;

	@Autowired
	private Environment environment;

	@Autowired
	private SendGrid sendGridClient;

	// Fetches all email details
	public List<EmailDTO> getAllEmails() {

		List<Email> emails = emailRepository.findAll();
		List<EmailDTO> emailDTOs = new ArrayList<EmailDTO>();

		for (Email email : emails) {
			EmailDTO emailDTO = EmailDTO.valueOf(email);
			emailDTOs.add(emailDTO);
		}

		logger.info("Email details : {}", emailDTOs);
		return emailDTOs;
	}

	// Fetches all template details
	public List<TemplateDTO> getTemplates() {

		List<Template> templates = templateRepository.findAll();
		List<TemplateDTO> templateDTOs = new ArrayList<TemplateDTO>();

		for (Template template : templates) {
			TemplateDTO templateDTO = TemplateDTO.valueOf(template);
			templateDTOs.add(templateDTO);
		}

		logger.info("Template details : {}", templateDTOs);
		return templateDTOs;
	}

	// Fetch specific email details
	public EmailDTO getSpecificEmail(Long emailId) {
		logger.info("Email details : {}", emailId);
		return EmailDTO.valueOf(emailRepository.findById(emailId.intValue()).get());
	}

	private Template getTemplate(Integer templateId) throws TemplateNotFoundException {
		Template template = null;

		try {
			template = templateRepository.findById(templateId.intValue()).get();
		} catch (NoSuchElementException noSuchElementException) {
			throw new TemplateNotFoundException(environment.getProperty("EmailService.TEMPLATE_NOT_FOUND"));
		}

		return template;
	}

	public TemplateDTO getTemplate(Long templateId) throws TemplateNotFoundException {

		return TemplateDTO.valueOf(getTemplate(new Integer(templateId.intValue())));
	}

	// Sends Email
	public void sendEmail(SendEmailDTO sendEmailDTO) throws Exception {
		MimeMessage message = sender.createMimeMessage();

		// Enable the multipart flag!
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/");
		Template template = getTemplate(sendEmailDTO.getTemplateId());
		List<String> userNames = sendEmailDTO.getUserNames();
		HashMap<String, UserDTO> userDTOs = sendEmailDTO.getUserDTOs();
		UserDTO userDTO = null;

		for (String userName : userNames) {

			userDTO = userDTOs.get(userName);
			helper.setTo(userDTO.getEmailId());
			String mailbody = template.getContent();
			BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/templates/main.ftl"));
			writer.write(mailbody);
			writer.close();
			freemarker.template.Template ftlTemplate = freemarkerConfig.getTemplate("main.ftl");

			String params = template.getDynamicParams();
			System.out.println("**************" + template.getContent() + template.getDynamicParams());
			String[] dynamicParams = params.split(":");
			System.out.println("**************" + dynamicParams);

			// ClassPathResource file = new ClassPathResource("cat.jpg");
			// helper.addInline("id101", file);

			HashMap<String, String> paramValues = sendEmailDTO.getParamValues();

			System.out.println("++++++++++" + paramValues);
			/*
			 * JSONObject jObject = new JSONObject(sendEmailDTO.getParamValues());
			 * Iterator<?> keys = jObject.keys();
			 * 
			 * while( keys.hasNext() ){ String key = (String)keys.next();
			 * System.out.println("++"+key); String value = jObject.getString(key);
			 * System.out.println("++"+value); paramValues.put(key, value);
			 * 
			 * }
			 */
			HashMap<String, Object> model = new HashMap<String, Object>();
			for (String dynamicParam : dynamicParams) {
				String paramValue = paramValues.get(dynamicParam);
				if (!paramValue.endsWith(".jpg")) {

					model.put(dynamicParam, paramValue);
				}
			}
			String text = FreeMarkerTemplateUtils.processTemplateIntoString(ftlTemplate, model);
			helper.setText(text, true);
			helper.setSubject(template.getTemplateName());

			for (String dynamicParam : dynamicParams) {
				System.out.println("**************" + dynamicParam);
				String paramValue = paramValues.get(dynamicParam);
				System.out.println("**************" + paramValue);
				if (paramValue.endsWith(".jpg")) {
					ClassPathResource image = new ClassPathResource(paramValue);
					helper.addInline(dynamicParam, image);
				} /*
					 * else { InputStream stream = new
					 * ByteArrayInputStream(paramValues.get(dynamicParam).getBytes(StandardCharsets.
					 * UTF_8)); helper.addInline(dynamicParam, new InputStreamResource(stream),
					 * "text/plain; charset=" + StandardCharsets.UTF_8); //StringWriter writer = new
					 * StringWriter(); //Velocity.evaluate(context, writer, "EvalError",
					 * helper.getMimeMessage()); // message.setText(writer.toString(), true);
					 * 
					 * }
					 */
			}

			sender.send(message);
		}

	}

	public void sendSGOEmail(SendEmailDTO sendEmailDTO)
			throws TemplateException, TemplateNotFoundException, IOException {

		List<String> userNames = sendEmailDTO.getUserNames();
		HashMap<String, UserDTO> userDTOs = sendEmailDTO.getUserDTOs();
		freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/");
		UserDTO userDTO = null;
		String toEmailAddress = null;
		String fromEmailAddress = environment.getProperty("EmailService.FROM_EMAIL");
		Mail mail = null;
		String mailbody = sendEmailDTO.getHtmlContent();
		BufferedWriter writer = new BufferedWriter(
				new FileWriter("D:\\SpringArtifacts\\SpringBegininning\\DefectMS\\src\\main\\resources\\main.ftl"));
		writer.write(mailbody);
		writer.close();
		freemarker.template.Template ftlTemplate = freemarkerConfig.getTemplate("main.ftl");
		String text = null;
		for (String userName : userNames) {

			userDTO = userDTOs.get(userName);
			toEmailAddress = userDTO.getEmailId();
			HashMap<String, Object> model = new HashMap<String, Object>();
			for (String dynamicParam : sendEmailDTO.getDynamicParams()) {

				switch (dynamicParam) {

				case "user":
					model.put("user", userDTO.getUserName());
					break;
				case "age":
					model.put("age", userDTO.getAge());
					break;
				case "gender":
					model.put("gender", userDTO.getGender());
					break;
				case "role":
					model.put("role", userDTO.getUserRole());
					break;

				}
			}
			text = FreeMarkerTemplateUtils.processTemplateIntoString(ftlTemplate, model);
			System.out.println("toEmailAddress++++++" + toEmailAddress);
			mail = new Mail(new com.sendgrid.Email(fromEmailAddress), "Campaign Management",
					new com.sendgrid.Email(toEmailAddress), new Content("text/html", text));
			mail.setReplyTo(new com.sendgrid.Email(fromEmailAddress));

			Request request = new Request();

			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			System.out.println("request+++++++++++++++++" + request.getBody());
			Response response = this.sendGridClient.api(request);
			System.out.println("response___________" + response.getBody());

		}
	}

	public void sendSGEmail(SendEmailDTO sendEmailDTO)
			throws TemplateException, TemplateNotFoundException, IOException {

		List<String> userNames = sendEmailDTO.getUserNames();
		HashMap<String, UserDTO> userDTOs = sendEmailDTO.getUserDTOs();
		freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/");
		UserDTO userDTO = null;
		String toEmailAddress = null;
		String fromEmailAddress = environment.getProperty("EmailService.FROM_EMAIL");
		Mail mail = null;
		//String mailbody = sendEmailDTO.getHtmlContent();

		BufferedWriter writer = new BufferedWriter(new FileWriter("/main.ftl"));
		BufferedReader reader = new BufferedReader(
				new FileReader("D:\\SpringArtifacts\\Makethon\\email_template\\email-template1-3.html" ));
		// BufferedReader reader = new BufferedReader(new
		// FileReader("D:\\mail\\email-template1-4.html")); writer.write(mailbody);
		//reader.close();
		//writer.close();
		freemarker.template.Template ftlTemplate = freemarkerConfig.getTemplate("main.ftl");
		String text="";
		for (String userName : userNames) {

			userDTO = userDTOs.get(userName);
			toEmailAddress = userDTO.getEmailId();
			HashMap<String, Object> model = new HashMap<String, Object>();
			List<String> dynamicParams = sendEmailDTO.getDynamicParams();

			if (null != dynamicParams) {
				for (String dynamicParam : sendEmailDTO.getDynamicParams()) {

					switch (dynamicParam) {

					case "user":
						model.put("user", userDTO.getUserName());
						break;
					case "age":
						model.put("age", userDTO.getAge());
						break;
					case "gender":
						model.put("gender", userDTO.getGender());
						break;
					case "role":
						model.put("role", userDTO.getUserRole());
						break;

					}
				}

			}

		//text = FreeMarkerTemplateUtils.processTemplateIntoString(ftlTemplate, model);
			String strCurrentLine = "";
			while ((strCurrentLine = reader.readLine()) != null) {
				System.out.println("strCurrentLine" + strCurrentLine);
				text = text + strCurrentLine;
			}
			reader.close();
			mail = new Mail(new com.sendgrid.Email(fromEmailAddress), "CampaignMailer",
					new com.sendgrid.Email(toEmailAddress), new Content("text/html", text));
			mail.setReplyTo(new com.sendgrid.Email(fromEmailAddress));
			Attachments attachments = new Attachments();
			File file =  new File("D:\\SpringArtifacts\\Makethon\\email_template\\assets\\img\\myntra-logo.png");
			FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            String encodedfile = Base64.getEncoder().encodeToString(bytes);
			attachments.setContent(encodedfile);
			attachments.setContentId("myimagecid");
			attachments.setFilename("myntra-logo.png");
			attachments.setDisposition("inline");
		    mail.addAttachments(attachments);
			//mail.addCustomArg("myimagecid", encodedfile);
		//	Content content = new Content();
		//	content.setType("myimagecid");
		//	content.setValue(encodedfile);
		//	mail.addContent(content );
			Request request = new Request();

			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			System.out.println("request+++++++++++++++++" + request.getBody());
			this.sendGridClient.api(request);
			fileInputStreamReader.close();

		}
	}

	/*
	 * public EmailDTO closeEmail(Long emailId) throws EmailNotFoundException {
	 * logger.info("Email to close : {}", emailId); try {
	 * emailRepository.findById(emailId.intValue()).get(); } catch
	 * (NoSuchElementException noSuchElementException) { throw new
	 * EmailNotFoundException(environment.getProperty(
	 * "EmailService.EMAIL_NOT_FOUND")); }
	 * emailRepository.updateEmail(environment.getProperty("EmailService.CLOSED") ,
	 * environment.getProperty("EmailService.CHANGE_STATUS_NAP"), emailId); return
	 * getSpecificEmail(emailId); }
	 */

	public EmailDTO createEmail(EmailDTO emailDTO)
			throws InvalidInputException, InvalidCategoryException, InvalidStatusException {
		logger.info("Creation request for email {}", emailDTO);
		if (null == emailDTO) {
			throw new InvalidInputException(environment.getProperty("EmailService.INVALID_INPUT"));
		}
		String status = emailDTO.getStatus();
		if (null == status) {
			emailDTO.setStatus(environment.getProperty("EmailService.NEW"));
		} else if (!status.equalsIgnoreCase(environment.getProperty("EmailService.NEW"))) {
			throw new InvalidStatusException(environment.getProperty("EmailService.INVALID_STATUS"));
		}

		Email email = emailDTO.createEntity();
		Email createdEmail = emailRepository.save(email);
		return EmailDTO.valueOf(createdEmail);
	}

	public TemplateDTO createTemplate(TemplateDTO templateDTO) throws InvalidInputException, JSONException {
		logger.info("Creation request for email {}", templateDTO);
		if (null == templateDTO) {
			throw new InvalidInputException(environment.getProperty("EmailService.INVALID_INPUT"));
		}

		Template template = templateDTO.createEntity();
		Template createdTemplate = templateRepository.save(template);
		return TemplateDTO.valueOf(createdTemplate);
	}

	public TemplateDTO updateTemplate(TemplateDTO templateDTO)
			throws InvalidInputException, TemplateNotFoundException, JSONException {
		logger.info("Creation request for email {}", templateDTO);
		if (null == templateDTO) {
			throw new InvalidInputException(environment.getProperty("EmailService.INVALID_INPUT"));
		}

		Template template = getTemplate(templateDTO.getTemplateId());
		if (null != templateDTO.getContent()) {
			template.setContent(templateDTO.getContent());
		}
		if (null != templateDTO.getDynamicParams()) {
			template.setDynamicParams(templateDTO.getDynamicParams());
		}
		if (null != templateDTO.getTemplateName()) {
			template.setTemplateName(templateDTO.getTemplateName());
		}
		if (null != templateDTO.getSections()) {
			template.setSections(templateDTO.getSections().toString());
		}

		Template updateTemplate = templateRepository.save(template);
		return TemplateDTO.valueOf(updateTemplate);
	}

}
