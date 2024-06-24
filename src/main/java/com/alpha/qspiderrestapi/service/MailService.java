package com.alpha.qspiderrestapi.service;

import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;

@Service
public interface MailService {

	public void sendMail(EmailData emailData) throws MessagingException;
}
