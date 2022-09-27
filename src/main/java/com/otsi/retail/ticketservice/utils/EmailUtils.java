package com.otsi.retail.ticketservice.utils;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.otsi.retail.ticketservice.exceptions.RegAppException;

@Component
public class EmailUtils {

	@Value("${spring.mail.username}")
	String fromEmail;

	@Autowired
	private JavaMailSender mailSender;

	public boolean sendEmail(String subject, String body, String to) {
		boolean isMailSent = false;
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setFrom(fromEmail);
			mimeMessageHelper.setText("text/html", body);
			mailSender.send(mimeMessageHelper.getMimeMessage());
			isMailSent = true;
		} catch (Exception e) {
			throw new RegAppException(e.getMessage());
		}
		return isMailSent;
	}
}