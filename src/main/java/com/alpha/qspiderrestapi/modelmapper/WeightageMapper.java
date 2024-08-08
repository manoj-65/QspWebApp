package com.alpha.qspiderrestapi.modelmapper;

import java.util.List;
import java.util.Optional;

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
				.pyspiders(dto.getPyspiders()).prospiders(dto.getProspiders()).category(category).build();
		category.setWeightage(weightage);
		weightage.setCategory(category);

		return weightage;
	}

	public WeightageDto getDto(List<Weightage> weightages, long categoryId, Long subCategoryId) {
		Weightage weightage = null;
		if (subCategoryId != null) {
			Optional<Weightage> opt = weightages.stream().filter(w -> w.getCourse_SubCategoryId() != null
					&& w.getCourse_SubCategoryId() == subCategoryId.longValue()).findFirst();
			if (opt.isPresent()) {
				weightage = opt.get();
				return weightageDtoMapper(weightage);
			} else
				return null;
		} else {

			Optional<Weightage> opt = weightages.stream()
					.filter(w -> w.getCourse_categoryId() != null && w.getCourse_categoryId() == categoryId)
					.findFirst();
			if (opt.isPresent()) {
				weightage = opt.get();
				return weightageDtoMapper(weightage);
			} else
				return null;
		}

	}

	public WeightageDto weightageDtoMapper(Weightage weightage) {
		return WeightageDto.builder().qspiders(weightage.getQspiders()).jspiders(weightage.getJspiders())
				.pyspiders(weightage.getPyspiders()).prospiders(weightage.getProspiders()).build();
	}

}