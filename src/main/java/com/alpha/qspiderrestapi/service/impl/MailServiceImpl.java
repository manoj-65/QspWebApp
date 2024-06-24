package com.alpha.qspiderrestapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alpha.qspiderrestapi.service.EmailData;
import com.alpha.qspiderrestapi.service.MailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {

	@Autowired
	private JavaMailSender mailSender;

	@Async("async")
	public void sendMail(EmailData emailData) throws MessagingException {

		MimeMessage mail = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo("luvrishi64@gmail.com");
		helper.setSubject(emailData.getSubject());
		helper.setText(emailData.getContent());

		mailSender.send(mail);
	}
}
