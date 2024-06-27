package com.alpha.qspiderrestapi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.alpha.qspiderrestapi.dto.BranchDto;

@Component
public class CourseUtil {

//	public List<BranchDto> getTheBranchDto(List<Object> fetchAllViewByCityName) {
//		List<BranchDto> branchDtos = new ArrayList<BranchDto>();
//		for (Object object : fetchAllViewByCityName) {
//			branchDtos.add(objectMapper.convertValue(object, BranchDto.class));
//		}
//		return branchDtos;
//	}

	public List<BranchDto> getTheBranchDto(List<Map<String, Object>> fetchAllViewByCityName) {
		List<BranchDto> branchDtos = new ArrayList<BranchDto>();
		for (Map<String, Object> result : fetchAllViewByCityName) {
			BranchDto branchDto = new BranchDto();
			branchDto.setBranchId((long) result.get("branch_id"));
			branchDto.setBranchImage((String) result.get("branch_image"));
			branchDto.setBranchName((String) result.get("display_name"));
			branchDto.setPhoneNumber((String) result.get("contacts"));
			branchDto.setLocation((String) result.get("location"));
			branchDto.setUpcomingBatches((Long) result.get("upcoming_batches"));
			branchDto.setOngoingBatches((Long) result.get("ongoing_batches"));
			branchDto.setCity((String) result.get("city"));
			branchDto.setPinCode((int) result.get("pincode"));
			branchDto.setState((String) result.get("state"));
			branchDto.setStreet((String) result.get("street"));
			branchDto.setOrganizationType((String) result.get("branch_type"));
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

}
