package com.alpha.qspiderrestapi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.alpha.qspiderrestapi.dto.BranchDto;
import com.alpha.qspiderrestapi.entity.Course;
import com.alpha.qspiderrestapi.entity.ViewAllHomePage;
import com.alpha.qspiderrestapi.entity.enums.Mode;

@Component
public class CourseUtil {

//	public List<BranchDto> getTheBranchDto(List<Object> fetchAllViewByCityName) {
//		List<BranchDto> branchDtos = new ArrayList<BranchDto>();
//		for (Object object : fetchAllViewByCityName) {
//			branchDtos.add(objectMapper.convertValue(object, BranchDto.class));
//		}
//		return branchDtos;
//	}

	public List<BranchDto> getTheBranchDto(List<ViewAllHomePage> fetchAllViewByCityName) {
		List<BranchDto> branchDtos = new ArrayList<BranchDto>();
		for (ViewAllHomePage result : fetchAllViewByCityName) {
			BranchDto branchDto = new BranchDto();
			branchDto.setBranchId(result.getBranchId());
			branchDto.setBranchImage(result.getBranchImage());
			branchDto.setBranchName(result.getDisplayName());
			branchDto.setPhoneNumber(result.getContacts());
			branchDto.setLocation(result.getLocation());
			branchDto.setUpcomingBatches(result.getUpcomingBatches());
			branchDto.setOngoingBatches(result.getOngoingBatches());
			branchDto.setCountry(result.getCountry());
			branchDto.setCity(result.getCityName());
			branchDto.setPinCode(result.getPincode());
			branchDto.setState(result.getState());
			branchDto.setStreet(result.getStreet());
			branchDto.setOrganizationType(result.getBranchType());
			branchDtos.add(branchDto);
		}
		return branchDtos;
	}

	public double generateRandomRating() {
		return 4.5 + Math.random() * 0.4;
	}

	public int generateRandomParticipant() {
		int lowerBound = 30000;
		int upperBound = 50001;

		Random random = new Random();
		return random.nextInt(upperBound - lowerBound) + lowerBound;
	}

	public List<Course> filterForOnline(List<Course> courses) {
		return courses.stream().filter(course -> course.getMode().contains(Mode.ONLINE_CLASSES))
				.collect(Collectors.toList());
	}

}
