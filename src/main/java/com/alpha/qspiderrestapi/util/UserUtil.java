package com.alpha.qspiderrestapi.util;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alpha.qspiderrestapi.entity.Contact;
import com.alpha.qspiderrestapi.entity.Email;
import com.alpha.qspiderrestapi.entity.User;

@Component
public class UserUtil {

	public List<Contact> mapPhoneNumberToUser(List<Contact> contacts, User user) {
		contacts.stream().forEach(contact -> contact.setUser(user));
		return contacts;
	}

	public List<Email> mapEmailToUser(List<Email> emails, User user) {
		emails.stream().forEach(email -> email.setUser(user));
		return emails;
	}

}
