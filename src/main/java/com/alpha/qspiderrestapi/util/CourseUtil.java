package com.alpha.qspiderrestapi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
			branchDtos.add(branchDto);
		}
		return branchDtos;
	}

}