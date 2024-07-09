package com.alpha.qspiderrestapi.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alpha.qspiderrestapi.dao.CategoryDao;
import com.alpha.qspiderrestapi.dao.CityDao;
import com.alpha.qspiderrestapi.dao.CourseDao;
import com.alpha.qspiderrestapi.dao.SubCategoryDao;
import com.alpha.qspiderrestapi.dao.WeightageDao;
import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.WeightageDto;
import com.alpha.qspiderrestapi.entity.Category;
import com.alpha.qspiderrestapi.entity.City;
import com.alpha.qspiderrestapi.entity.Course;
import com.alpha.qspiderrestapi.entity.SubCategory;
import com.alpha.qspiderrestapi.entity.Weightage;
import com.alpha.qspiderrestapi.entity.enums.Organization;
import com.alpha.qspiderrestapi.exception.IdNotFoundException;
import com.alpha.qspiderrestapi.exception.InvalidInfoException;
import com.alpha.qspiderrestapi.service.WeightageService;
import com.alpha.qspiderrestapi.util.ResponseUtil;
import com.alpha.qspiderrestapi.util.WeightageUtil;

@Service
public class WeightageServiceImpl implements WeightageService {

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private SubCategoryDao subcategoryDao;

	@Autowired
	private CourseDao courseDao;

	@Autowired
	private CityDao cityDao;

	@Autowired
	private WeightageDao weightageDao;

	@Autowired
	WeightageUtil weightageUtil;
//	@Autowired
//	private EntityManager entityManager;

	@Override
	public ResponseEntity<ApiResponse<Weightage>> saveCategoryWeightage(long categoryId, WeightageDto dto) {

		Optional<Category> optCategory = categoryDao.fetchCategoryById(categoryId);
		if (optCategory.isPresent()) {
			if (optCategory.get().getWeightage() == null) {

				Weightage weightage = Weightage.builder().qspiders(dto.getQspiders()).jspiders(dto.getJspiders())
						.pyspiders(dto.getPyspiders()).bspiders(dto.getBspiders()).category(optCategory.get()).build();
				optCategory.get().setWeightage(weightage);
				weightage.setCategory(optCategory.get());
				weightage = weightageDao.saveWeightage(weightage);

				return ResponseUtil.getCreated(weightage);
			} else {
				throw new InvalidInfoException("The given category already has a weightage");
			}
		}
		throw new IdNotFoundException("No category found with the id: " + categoryId);

	}

	@Override
	public ResponseEntity<ApiResponse<Weightage>> saveSubCategoryWeightage(long categoryId, long subCategoryId,
			WeightageDto dto) {

		Category category = categoryDao.fetchCategoryById(categoryId)
				.orElseThrow(() -> new IdNotFoundException("No category found with id: " + categoryId));
		SubCategory subCategory = subcategoryDao.fetchSubCategoryById(subCategoryId)
				.orElseThrow(() -> new IdNotFoundException("No subCategory found with id: " + subCategoryId));
		if (category.getSubCategories().contains(subCategory)) {
			if (subCategory.getWeightage().stream().anyMatch(w -> w.getSubCategory_categoryId() == categoryId)) {
				throw new InvalidInfoException("The given category and sub-category pair already has a weihtage");
			}
			Weightage weightage = Weightage.builder().qspiders(dto.getQspiders()).jspiders(dto.getJspiders())
					.pyspiders(dto.getPyspiders()).bspiders(dto.getBspiders()).subCategory(subCategory)
					.subCategory_categoryId(categoryId).build();
			weightage = weightageDao.saveWeightage(weightage);
			return ResponseUtil.getCreated(weightage);

		}
		throw new InvalidInfoException("In given info, category does not contain the sub-category");
	}

	@Override
	public ResponseEntity<ApiResponse<Weightage>> saveCourseWeightage(long categoryId, Long subCategoryId,
			long courseId, WeightageDto dto) {

		Category category = categoryDao.fetchCategoryById(categoryId)
				.orElseThrow(() -> new IdNotFoundException("No category found with id: " + categoryId));

		Course course = courseDao.fetchCourseById(courseId)
				.orElseThrow(() -> new IdNotFoundException("No Course found with id: " + courseId));

		if (subCategoryId != null) {
			SubCategory subCategory = subcategoryDao.fetchSubCategoryById(subCategoryId)
					.orElseThrow(() -> new IdNotFoundException("No subCategory found with id: " + subCategoryId));

			if (category.getSubCategories().contains(subCategory)) {
				if (subCategory.getCourses().contains(course)) {
					if (subCategory.getWeightage().stream()
							.anyMatch(w -> w.getCourse_SubCategoryId() == subCategoryId)) {
						throw new InvalidInfoException("The given course and sub-category pair already has a weihtage");
					}
					Weightage weightage = Weightage.builder().qspiders(dto.getQspiders()).jspiders(dto.getJspiders())
							.pyspiders(dto.getPyspiders()).bspiders(dto.getBspiders()).course(course)
							.course_SubCategoryId(subCategoryId).build();
					weightage = weightageDao.saveWeightage(weightage);
					return ResponseUtil.getCreated(weightage);
				} else {
					throw new InvalidInfoException("In given info, sub-category does not contain the course");
				}
			} else {
				throw new InvalidInfoException("In given info, category does not contain the sub-category");
			}
		} else {
			if (category.getCourses().contains(course)) {
				if (course.getWeightages().stream()
						.anyMatch(w -> w.getCourse_categoryId() != null && w.getCourse_categoryId() == categoryId)) {
					throw new InvalidInfoException("In given category and course pair already has a weihtage");
				}
				Weightage weightage = Weightage.builder().qspiders(dto.getQspiders()).jspiders(dto.getJspiders())
						.pyspiders(dto.getPyspiders()).bspiders(dto.getBspiders()).course(course)
						.course_categoryId(categoryId).build();
				weightage = weightageDao.saveWeightage(weightage);
				return ResponseUtil.getCreated(weightage);
			} else {
				throw new InvalidInfoException("In given info, category does not contain the course");
			}
		}
	}

	@Override
	public ResponseEntity<ApiResponse<Weightage>> saveCityWeightage(String cityName, WeightageDto dto) {
		City city = cityDao.findCityByCityName(cityName)
				.orElseThrow(() -> new IdNotFoundException("No city found with the given city name"));
		if (city.getWeightage() == null) {
			Weightage weightage = Weightage.builder().qspiders(dto.getQspiders()).jspiders(dto.getJspiders())
					.pyspiders(dto.getPyspiders()).bspiders(dto.getBspiders()).city(city).build();
			city.setWeightage(weightage);
			weightage.setCity(city);
			weightage = weightageDao.saveWeightage(weightage);
			return ResponseUtil.getCreated(weightage);
		}
		throw new InvalidInfoException("Given city already contains a weightage");
	}

	// under progress

	@Override
	public ResponseEntity<ApiResponse<String>> deleteCategoryWeightage(Long categoryId) {

		if (categoryId != null) {
			Category category = categoryDao.fetchCategoryById(categoryId).get();

			Weightage weightage = category.getWeightage();
			if (weightage != null) {
				System.err.println(weightage.getId());
				weightageDao.deleteWeightage(weightage);
				category.setWeightage(weightage);
				categoryDao.saveCategory(category);
				return ResponseUtil.getNoContent("Category with the given weightage is removed");
			}

		}
		throw new IdNotFoundException("Category ID not be found");
	}

	// under progress

	@Override
	public ResponseEntity<ApiResponse<String>> deleteSubCategoryWeightage(Long subCategoryId) {

		if (subCategoryId != null) {
			Optional<SubCategory> subCategory = subcategoryDao.fetchSubCategoryById(subCategoryId);
			List<Weightage> weightages = subCategory.get().getWeightage();
			if (weightages != null) {
				weightages.stream().forEach((weightage) -> weightageDao.deleteWeightage(weightage));
				subcategoryDao.saveSubCategory(subCategory.get());
				return ResponseUtil.getNoContent("SubCategory with the given weightage is removed");
			}
			throw new IdNotFoundException("Weightage not present");
		}
		throw new IdNotFoundException("SubCategory ID not be found");
	}

	// under progress
	@Override
	public ResponseEntity<ApiResponse<String>> deleteCourseWeightage(Long courseId) {
		if (courseId != null) {
			Optional<Course> course = courseDao.fetchCourseById(courseId);
			List<Weightage> weightages = course.get().getWeightages();
			if (weightages != null) {
				weightages.stream().forEach((weightage) -> weightageDao.deleteWeightage(weightage));
				return ResponseUtil.getNoContent("Course with the given weightage is removed");
			}
			throw new IdNotFoundException("Weightage not present");
		}
		throw new IdNotFoundException("Course ID not be found");

	}

	@Override
	public ResponseEntity<ApiResponse<String>> updateSubCategoryWeightage(long categoryId, long subCategoryId,
			Organization organization, long weightage) {
		if (!categoryDao.isCategoryPresent(categoryId))
			throw new IdNotFoundException("No category found with the id :" + categoryId);

		if (!subcategoryDao.isSubCategoryPresent(subCategoryId))
			throw new IdNotFoundException("No Sub-Category found with the id :" + subCategoryId);

		List<Weightage> weightages = weightageDao.findSubCategoryWeightages(categoryId);

		if (weightages.isEmpty()) {
			throw new InvalidInfoException("No weightages found with the given category and sub-category pair");
		} else {
			Weightage target = weightages.stream().filter(w -> w.getSubCategory().getSubCategoryId() == subCategoryId)
					.findFirst().get();
			if (getOrgWeightage(target, organization) > weightage) {
				weightages.stream()
						.filter(w -> getOrgWeightage(w, organization) >= weightage
								&& getOrgWeightage(w, organization) <= getOrgWeightage(target, organization))
						.peek(w -> w.setQspiders(getOrgWeightage(w, organization) + 1l)).forEach(w -> {
							if (w.getSubCategory().getSubCategoryId() == subCategoryId) {
								w.setQspiders(weightage);
							}
						});
			} else if (target.getQspiders() < weightage) {
				weightages.stream().filter(w -> w.getQspiders() <= weightage && w.getQspiders() >= target.getQspiders())
						.peek(w -> setOrgWeightage(w, organization, (w.getQspiders() - 1l))).forEach(w -> {
							if (w.getSubCategory().getSubCategoryId() == subCategoryId) {
								setOrgWeightage(w, organization, weightage);
							}
						});
			}

		}

		weightageDao.saveAllWeightage(weightages);
		return ResponseUtil.getOk("Updated Successfully");
	}

	private long getOrgWeightage(Weightage weightage, Organization organization) {
		if (organization.equals(Organization.QSP)) {
			return weightage.getQspiders();
		} else if (organization.equals(Organization.JSP)) {
			return weightage.getJspiders();
		} else if (organization.equals(Organization.PYSP)) {
			return weightage.getPyspiders();
		} else if (organization.equals(Organization.BSP)) {
			return weightage.getBspiders();
		} else {
			throw new InvalidInfoException("Organization not found");
		}
	}

	private void setOrgWeightage(Weightage weightage, Organization organization, long setWeightage) {
		if (organization.equals(Organization.QSP)) {
			weightage.setQspiders(setWeightage);
		} else if (organization.equals(Organization.JSP)) {
			weightage.setJspiders(setWeightage);
		} else if (organization.equals(Organization.PYSP)) {
			weightage.setPyspiders(setWeightage);
		} else if (organization.equals(Organization.BSP)) {
			weightage.setBspiders(setWeightage);
		} else {
			throw new InvalidInfoException("Organization not found");
		}
	}

	@Override
	public ResponseEntity<ApiResponse<String>> updateCategoryWeightage(Long categoryId, Long weightage,
			Organization orgType) {

		if (categoryId != null && weightageUtil.isValidOrganisation(orgType)) {
			Category category = categoryDao.fetchCategoryById(categoryId).get();
			Weightage categoryWeightage = category.getWeightage();

			if (categoryWeightage != null)
				throw new InvalidInfoException("No weightages found with the given category and sub-category pair");

			if(getOrgWeightage(categoryWeightage, orgType) > weightage)
			{
				
			}

		}

		return null;

	}

}
