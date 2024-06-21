package com.alpha.qspiderrestapi.util;

import org.springframework.stereotype.Component;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ValidatePhoneNumber {

	public boolean isValidPhoneNumber(String phoneNumber) {
		log.info("Validating the Given Phone Number is Valid or not");
		PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
		try {
			PhoneNumber number = phoneNumberUtil.parse(phoneNumber, null);
			return phoneNumberUtil.isValidNumber(number);
		} catch (NumberParseException e) {
			log.error("Phone number is Invalid");
			return false;
		}
	}

}
