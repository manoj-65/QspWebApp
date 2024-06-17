package com.alpha.qspiderrestapi.dto;

import java.util.List;

import com.alpha.qspiderrestapi.entity.Address;
import com.alpha.qspiderrestapi.entity.Contact;
import com.alpha.qspiderrestapi.entity.Faq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchByIdDto {

	private String name;
	private String branchImage;
	private List<String> branchGallery;
	private Address address;
	private List<Contact> contacts;
//	private int upcomingBatches;
//	private int ongoingBatches;
	private List<BranchById_CourseDto> courses;
	private List<BranchById_BatchDto> batches;
	private List<Faq> faqs;
}
