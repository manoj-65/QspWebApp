package com.alpha.qspiderrestapi.modelmapper;

import com.alpha.qspiderrestapi.dto.UserDto;
import com.alpha.qspiderrestapi.entity.User;

public class UserDtoMapper {

	public static UserDto mapUserToUserDto(User user) {
		return UserDto.builder()
				   .userName(user.getUserName())
				   .userEmail(user.getEmails())
				   .phoneNumber(user.getContacts())
				   .role(user.getRole())
				   .status(user.getStatus())
				   .build();
	}
}
