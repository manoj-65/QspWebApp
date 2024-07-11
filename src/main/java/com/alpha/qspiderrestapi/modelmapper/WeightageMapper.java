package com.alpha.qspiderrestapi.modelmapper;

import org.springframework.stereotype.Component;

import com.alpha.qspiderrestapi.dto.WeightageDto;
import com.alpha.qspiderrestapi.entity.Category;
import com.alpha.qspiderrestapi.entity.Weightage;

import lombok.Data;

@Component
@Data
public class WeightageMapper {

	public Weightage weightageDtoToWeightageMapper(WeightageDto dto, Category category) {

		Weightage weightage = Weightage.builder().qspiders(dto.getQspiders()).jspiders(dto.getJspiders())
				.pyspiders(dto.getPyspiders()).bspiders(dto.getBspiders()).category(category).build();
		category.setWeightage(weightage);
		weightage.setCategory(category);

		return weightage;
	}
}
