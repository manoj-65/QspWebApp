package com.alpha.qspiderrestapi.service;

import org.springframework.stereotype.Service;

import com.alpha.qspiderrestapi.entity.FeedBack;

import jakarta.mail.MessagingException;

@Service
public interface MailService {

	public void sendMail(FeedBack feedback) throws MessagingException;
}
