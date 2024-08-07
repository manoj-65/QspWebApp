package com.alpha.qspiderrestapi.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alpha.qspiderrestapi.dao.CategoryDao;
import com.alpha.qspiderrestapi.dao.CityDao;
import com.alpha.qspiderrestapi.dao.CountryDao;
import com.alpha.qspiderrestapi.dao.CourseDao;
import com.alpha.qspiderrestapi.dao.SubCategoryDao;
import com.alpha.qspiderrestapi.dao.WeightageDao;
import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.WeightageDto;
import com.alpha.qspiderrestapi.entity.Category;
import com.alpha.qspiderrestapi.entity.City;
import com.alpha.qspiderrestapi.entity.Country;
import com.alpha.qspiderrestapi.entity.Course;
import com.alpha.qspiderrestapi.entity.SubCategory;
import com.alpha.qspiderrestapi.entity.Weightage;
import com.alpha.qspiderrestapi.entity.enums.Organization;
import com.alpha.qspiderrestapi.exception.IdNotFoundException;
import com.alpha.qspiderrestapi.exception.InvalidInfoException;
import com.alpha.qspiderrestapi.exception.InvalidOrganisationTypeException;
import com.alpha.qspiderrestapi.modelmapper.WeightageMapper;
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
	private WeightageUtil weightageUtil;

	@Autowired
	private WeightageMapper weightageMapper;

	@Autowired
	private CountryDao countryDao;

//	@Autowired
//	private EntityManager entityManager;

	@Override
	public ResponseEntity<ApiResponse<Weightage>> saveCategoryWeightage(long categoryId, WeightageDto dto) {

		Optional<Category> optCategory = categoryDao.fetchCategoryById(categoryId);
		if (optCategory.isPresent()) {
			if (optCategory.get().getWeightage() == null) {

				Weightage weightage = Weightage.builder().qspiders(dto.getQspiders()).jspiders(dto.getJspiders())
						.pyspiders(dto.getPyspiders()).prospiders(dto.getProspiders()).category(optCategory.get())
						.build();
				optCategory.get().setWeightage(weightage);
				weightage.setCategory(optCategory.get());

				weightageDao.incrementWeightageValues(dto.getQspiders(), dto.getJspiders(), dto.getPyspiders(),
						dto.getProspiders(), "category_id", categoryId);

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
			List<Weightage> weightages = weightageDao.findSubCategoryWeightages(categoryId);
			maxWeightageCheck(dto, weightages);
			Weightage weightage = Weightage.builder().qspiders(dto.getQspiders()).jspiders(dto.getJspiders())
					.pyspiders(dto.getPyspiders()).prospiders(dto.getProspiders()).subCategory(subCategory)
					.subCategory_categoryId(categoryId).build();
			weightageDao.incrementWeightageValues(dto.getQspiders(), dto.getJspiders(), dto.getPyspiders(),
					dto.getProspiders(), "sub_category_category_id", categoryId);
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
					if (course.getWeightages().stream().anyMatch(
							w -> w.getCourse_SubCategoryId() != null && w.getCourse_SubCategoryId() == subCategoryId)) {
						throw new InvalidInfoException("The given course and sub-category pair already has a weihtage");
					}
				//checking whether the given weightages are within the limit and is not out of bound
					List<Weightage> weightages = weightageDao.findCourseOfSubCategoryWeightages(subCategoryId);
					maxWeightageCheck(dto,weightages);
					
					Weightage weightage = Weightage.builder().qspiders(dto.getQspiders()).jspiders(dto.getJspiders())
							.pyspiders(dto.getPyspiders()).prospiders(dto.getProspiders()).course(course)
							.course_SubCategoryId(subCategoryId).build();
					weightageDao.incrementWeightageValues(dto.getQspiders(), dto.getJspiders(), dto.getPyspiders(),
							dto.getProspiders(), "course_sub_category_id", subCategoryId);
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
				
			//checking whether the given weightages are within the limit and is not out of bound
				List<Weightage> weightages = weightageDao.findCourseOfCategoryWeightages(categoryId);
				maxWeightageCheck(dto,weightages);
				

				Weightage weightage = Weightage.builder().qspiders(dto.getQspiders()).jspiders(dto.getJspiders())
						.pyspiders(dto.getPyspiders()).prospiders(dto.getProspiders()).course(course)
						.course_categoryId(categoryId).build();
				// increment old weightages to save new weightages
				weightageDao.incrementWeightageValues(dto.getQspiders(), dto.getJspiders(), dto.getPyspiders(),
						dto.getProspiders(), "course_category_id", categoryId);
				weightage = weightageDao.saveWeightage(weightage);
				return ResponseUtil.getCreated(weightage);
			} else {
				throw new InvalidInfoException("In given info, category does not contain the course");
			}
		}
	}

	private void maxWeightageCheck(WeightageDto dto, List<Weightage> weightages) {
		Map<Organization, Weightage> orgWeightageMap = new HashMap<Organization, Weightage>();
		for (Organization org : Organization.values()) {
			Optional<Weightage> max = weightages.stream()
					.max((w1, w2) -> (int) -(int) getOrgWeightage(w2, org));
			orgWeightageMap.put(org, max.get());
		}
		String errorStm = "Given weightage out of bound : ";
		int count = 0;
		for (Entry<Organization, Weightage> weightage : orgWeightageMap.entrySet()) {
			if (weightage.getKey().equals(Organization.QSP)) {
				if (dto.getQspiders() > (weightage.getValue().getQspiders() + 1l)) {
					errorStm+=(" qspiders value max limit : "+(weightage.getValue().getQspiders() + 1l)+",");
					count++;
				}
			}
			if (weightage.getKey().equals(Organization.JSP)) {
				if (dto.getJspiders() > (weightage.getValue().getJspiders() + 1l)) {
					errorStm+=(" jspiders value max limit : "+(weightage.getValue().getJspiders() + 1l)+",");
					count++;				}
			}
			if (weightage.getKey().equals(Organization.PYSP)) {
				if (dto.getPyspiders() > (weightage.getValue().getPyspiders() + 1l)) {
					errorStm+=(" pyspiders value max limit : "+(weightage.getValue().getPyspiders() + 1l)+",");
					count++;				}
			}
			if (weightage.getKey().equals(Organization.PROSP)) {
				if (dto.getProspiders() > (weightage.getValue().getProspiders() + 1l)) {
					errorStm+=(" prospiders value max limit : "+(weightage.getValue().getProspiders() + 1l)+",");
					count++;				}
			}
		}
		
		if(count>0) 
			throw new InvalidInfoException(errorStm);		
	}

	@Override
	public ResponseEntity<ApiResponse<Weightage>> saveCityWeightage(String cityName, WeightageDto dto) {
		City city = cityDao.findCityByCityName(cityName)
				.orElseThrow(() -> new IdNotFoundException("No city found with the given city name"));
		if (city.getWeightage() == null) {
			List<Weightage> allCityWeightage = weightageDao.findAllCityWeightage();

			Weightage weightage = Weightage.builder().qspiders(dto.getQspiders()).jspiders(dto.getJspiders())
					.pyspiders(dto.getPyspiders()).prospiders(dto.getProspiders()).city(city).build();
			city.setWeightage(weightage);
			weightage.setCity(city);

			weightageUtil.checkAndUpdateWeightage(weightage, allCityWeightage);
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

				weightages = weightages.stream()
						.filter(w -> getOrgWeightage(w, organization) >= weightage
								&& getOrgWeightage(w, organization) <= getOrgWeightage(target, organization))
						.collect(Collectors.toList());
				weightages = weightages.stream()
						.peek(w -> setOrgWeightage(w, organization, (getOrgWeightage(w, organization) + 1l)))
						.collect(Collectors.toList());
				setOrgWeightage(target, organization, weightage);
			} else if (getOrgWeightage(target, organization) < weightage) {
				weightages = weightages.stream()
						.filter(w -> getOrgWeightage(w, organization) <= weightage
								&& getOrgWeightage(w, organization) >= getOrgWeightage(target, organization))
						.collect(Collectors.toList());
				weightages = weightages.stream()
						.peek(w -> setOrgWeightage(w, organization, (getOrgWeightage(w, organization) - 1l)))
						.peek(w -> {
							if (w.getSubCategory().getSubCategoryId() == subCategoryId) {
								setOrgWeightage(w, organization, weightage);
							}
						}).collect(Collectors.toList());
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
		} else if (organization.equals(Organization.PROSP)) {
			return weightage.getProspiders();
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
		} else if (organization.equals(Organization.PROSP)) {
			weightage.setProspiders(setWeightage);
		} else {
			throw new InvalidInfoException("Organization not found");
		}
	}

	@Override
	public ResponseEntity<ApiResponse<String>> updateCategoryWeightage(Long categoryId, Long weightage,
			Organization orgType) {

		Optional<Category> category = categoryDao.fetchCategoryById(categoryId);
		if (category.isEmpty())
			throw new IdNotFoundException("Category Id incorrect");

		if (!weightageUtil.isValidOrganisation(orgType))
			throw new InvalidOrganisationTypeException("Invalid Organisation Type");

		Weightage categoryWeightage = category.get().getWeightage();
		List<Weightage> allWeightages = weightageDao.getAllWeightages();

		if (!(categoryWeightage != null))
			throw new InvalidInfoException("No weightages found with the given category");

		long maxSize = weightageUtil.findMaxSize(allWeightages, orgType);

		if (weightage > allWeightages.size())
			throw new InvalidInfoException("Weightage size limit exceeded");

		if (weightage == 0) {
			allWeightages = allWeightages.stream()
					.filter(w -> getOrgWeightage(w, orgType) >= getOrgWeightage(categoryWeightage, orgType))
					.collect(Collectors.toList());
			allWeightages = allWeightages.stream()
					.peek(w -> setOrgWeightage(w, orgType, (getOrgWeightage(w, orgType) - 1l))).peek(w -> {
						if (w.getCategory().getCategoryId() == categoryId) {
							setOrgWeightage(w, orgType, weightage);
						}
					}).collect(Collectors.toList());
		}

		else if (getOrgWeightage(categoryWeightage, orgType) == 0) {
			allWeightages = allWeightages.stream().filter(w -> getOrgWeightage(w, orgType) >= weightage)
					.collect(Collectors.toList());

//			allWeightages = allWeightages.stream()
//					.peek(w -> setOrgWeightage(w, orgType, (getOrgWeightage(w, orgType) + 1l))).peek(w -> {
//						if (w.getCategory().getCategoryId() == categoryId) {
//							setOrgWeightage(w, orgType, weightage);
//						}
//					}).collect(Collectors.toList());

			allWeightages = allWeightages.stream()
					.peek(w -> setOrgWeightage(w, orgType, getOrgWeightage(w, orgType) + 1L))
					.collect(Collectors.toList());

//			for (Weightage w : allWeightages) {
//				if (w.getCategory().getCategoryId() == categoryId) {
//				}
			setOrgWeightage(categoryWeightage, orgType, weightage);
		}

		else if (getOrgWeightage(categoryWeightage, orgType) > weightage && weightage <= maxSize) {
			allWeightages = allWeightages.stream()
					.filter(w -> getOrgWeightage(w, orgType) >= weightage
							&& getOrgWeightage(w, orgType) <= getOrgWeightage(categoryWeightage, orgType))
					.collect(Collectors.toList());

			allWeightages = allWeightages.stream()
					.peek(w -> setOrgWeightage(w, orgType, (getOrgWeightage(w, orgType) + 1l))).peek(w -> {
						if (w.getCategory().getCategoryId() == categoryId) {
							setOrgWeightage(w, orgType, weightage);
						}
					}).collect(Collectors.toList());
		}

		else if (getOrgWeightage(categoryWeightage, orgType) < weightage && weightage <= maxSize) {
			allWeightages = allWeightages.stream()
					.filter(w -> getOrgWeightage(w, orgType) <= weightage
							&& getOrgWeightage(w, orgType) >= getOrgWeightage(categoryWeightage, orgType))
					.collect(Collectors.toList());
			allWeightages = allWeightages.stream()
					.peek(w -> setOrgWeightage(w, orgType, (getOrgWeightage(w, orgType) - 1l))).peek(w -> {
						if (w.getCategory().getCategoryId() == categoryId) {
							setOrgWeightage(w, orgType, weightage);
						}
					}).collect(Collectors.toList());
		} else {
			throw new InvalidInfoException("Weightage size limit exceeded");
		}

		weightageDao.saveAllWeightage(allWeightages);
		return ResponseUtil.getOk("Updated Successfully");

	}

	@Override
	public ResponseEntity<ApiResponse<String>> updateCourseWeightage(long categoryId, Long subCategoryId, long courseId,
			WeightageDto weightageDto) {

		if (!categoryDao.isCategoryPresent(categoryId))
			throw new IdNotFoundException("No category found with the id :" + categoryId);

		List<Weightage> finalWeightage = new ArrayList<Weightage>();

		if (subCategoryId != null) {

			if (!subcategoryDao.isSubCategoryPresent(subCategoryId))
				throw new IdNotFoundException("No Sub-Category found with the id :" + subCategoryId);

			if (!courseDao.isCourseExist(courseId))
				throw new IdNotFoundException("No course found with the id :" + courseId);

			List<Weightage> weightages = weightageDao.findCourseOfSubCategoryWeightages(subCategoryId);

			if (weightages.isEmpty()) {
				throw new InvalidInfoException("No weightages found with the given sub-category and course pair");
			} else {
				Weightage target = weightages.stream().filter(w -> w.getCourse().getCourseId() == courseId).findFirst()
						.get();

				finalWeightage.addAll(getUpdatedWeightages(weightages, target, Organization.QSP,
						weightageDto.getQspiders(), courseId));
				finalWeightage.addAll(getUpdatedWeightages(weightages, target, Organization.JSP,
						weightageDto.getJspiders(), courseId));
				finalWeightage.addAll(getUpdatedWeightages(weightages, target, Organization.PYSP,
						weightageDto.getPyspiders(), courseId));
				finalWeightage.addAll(getUpdatedWeightages(weightages, target, Organization.PROSP,
						weightageDto.getProspiders(), courseId));

			}

			weightageDao.saveAllWeightage(weightages);
			return ResponseUtil.getOk("Updated Successfully");
		} else {

			if (!courseDao.isCourseExist(courseId))
				throw new IdNotFoundException("No course found with the id :" + courseId);

			List<Weightage> weightages = weightageDao.findCourseOfCategoryWeightages(categoryId);
			
			if (weightages.isEmpty()) {
				throw new InvalidInfoException("No weightages found with the given category and course pair");
			} else {
				Weightage target = weightages.stream().filter(w -> w.getCourse().getCourseId() == courseId).findFirst()
						.get();

				finalWeightage.addAll(getUpdatedWeightages(weightages, target, Organization.QSP,
						weightageDto.getQspiders(), courseId));
				finalWeightage.addAll(getUpdatedWeightages(weightages, target, Organization.JSP,
						weightageDto.getJspiders(), courseId));
				finalWeightage.addAll(getUpdatedWeightages(weightages, target, Organization.PYSP,
						weightageDto.getPyspiders(), courseId));
				finalWeightage.addAll(getUpdatedWeightages(weightages, target, Organization.PROSP,
						weightageDto.getProspiders(), courseId));

			}

			weightageDao.saveAllWeightage(finalWeightage);
			return ResponseUtil.getOk("Updated Successfully");
		}

	}

	private List<Weightage> getUpdatedWeightages(List<Weightage> weightages, Weightage target,
			Organization organization, long weightage, long courseId) {
		Optional<Weightage> max = weightages.stream()
				.max((w1, w2) -> (int) getOrgWeightage(w1, organization) - (int) getOrgWeightage(w2, organization));

		if (weightage > weightages.size() || weightage > (getOrgWeightage(max.get(), organization) + 1l)) {
			throw new InvalidInfoException(
					"In given info, weightage exceeds weightage limit in Organization type: " + organization);
		}
		if (weightage == 0l && getOrgWeightage(target, organization) != 0l) {
			weightages = weightages.stream()
					.filter(w -> getOrgWeightage(w, organization) >= getOrgWeightage(target, organization))
					.collect(Collectors.toList());
			return weightages.stream()
					.peek(w -> setOrgWeightage(w, organization, (getOrgWeightage(w, organization) - 1l))).peek(w -> {
						if (w.getCourse().getCourseId() == courseId) {
							setOrgWeightage(w, organization, weightage);
						}
					}).collect(Collectors.toList());

		} else if (getOrgWeightage(target, organization) == 0l && weightage != 0l) {
			if (weightage == (getOrgWeightage(max.get(), organization) + 1l)) {
				return weightages.stream().peek(w -> {
					if (w.getCourse().getCourseId() == courseId) {
						setOrgWeightage(w, organization, weightage);
					}
				}).collect(Collectors.toList());
			} else {
				weightages = weightages.stream().peek(w -> {
					if (w.getCourse().getCourseId() == courseId) {
						setOrgWeightage(w, organization, weightage);
					}
				}).filter(w -> getOrgWeightage(w, organization) >= weightage).collect(Collectors.toList());
				return weightages.stream()
						.peek(w -> setOrgWeightage(w, organization, (getOrgWeightage(w, organization) + 1l)))
						.peek(w -> {
							if (w.getCourse().getCourseId() == courseId) {
								setOrgWeightage(w, organization, weightage);
							}
						}).collect(Collectors.toList());
			}
		} else if (getOrgWeightage(target, organization) > weightage) {
			weightages = weightages.stream()
					.filter(w -> getOrgWeightage(w, organization) >= weightage
							&& getOrgWeightage(w, organization) <= getOrgWeightage(target, organization)
							&& getOrgWeightage(w, organization) != 0l)
					.collect(Collectors.toList());
			return weightages.stream()
					.peek(w -> setOrgWeightage(w, organization, (getOrgWeightage(w, organization) + 1l))).peek(w -> {
						if (w.getCourse().getCourseId() == courseId) {
							setOrgWeightage(w, organization, weightage);
						}
					}).collect(Collectors.toList());
		} else if (getOrgWeightage(target, organization) < weightage) {
			if (weightage > (getOrgWeightage(max.get(), organization))) {
				throw new InvalidInfoException(
						"In given info weightage exceeds weightage limit of Organization type: " + organization);
			}
			weightages = weightages.stream()
					.filter(w -> getOrgWeightage(w, organization) <= weightage
							&& getOrgWeightage(w, organization) >= getOrgWeightage(target, organization)
							&& getOrgWeightage(w, organization) != 0l)
					.collect(Collectors.toList());
			return weightages.stream()
					.peek(w -> setOrgWeightage(w, organization, (getOrgWeightage(w, organization) - 1l))).peek(w -> {
						if (w.getCourse().getCourseId() == courseId) {
							setOrgWeightage(w, organization, weightage);
						}
					}).collect(Collectors.toList());
		} else {
			return weightages;
		}
	}

	@Override
	public ResponseEntity<ApiResponse<Weightage>> saveCategoryWeightageAndIncrement(long categoryId, WeightageDto dto) {

		Optional<Category> category = categoryDao.fetchCategoryById(categoryId);
		List<Weightage> allWeightages = weightageDao.getAllWeightages();
		maxWeightageCheck(dto, allWeightages);
		if (category.isPresent()) {
			if (category.get().getWeightage() == null) {
				Weightage weightage = weightageMapper.weightageDtoToWeightageMapper(dto, category.get());
				weightageUtil.checkAndUpdateWeightage(weightage, allWeightages);
				weightageDao.saveWeightage(weightage);
				return ResponseUtil.getCreated(weightage);
			} else {
				throw new InvalidInfoException("The given category already has a weightage");
			}
		}
		throw new IdNotFoundException("No category found with the id: " + categoryId);
	}

	@Override
	public ResponseEntity<ApiResponse<Weightage>> saveCountryWeightage(String countryName, WeightageDto dto) {

		Country country = countryDao.findCountryByCountryName(countryName)
				.orElseThrow(() -> new IdNotFoundException("No country found with the given country name"));
		if (country.getCountryWeightage() == null) {
			List<Weightage> allCountryWeightage = weightageDao.findAllCountryWeightage();

			Weightage weightage = Weightage.builder().qspiders(dto.getQspiders()).jspiders(dto.getJspiders())
					.pyspiders(dto.getPyspiders()).prospiders(dto.getProspiders()).build();
			country.setCountryWeightage(weightage);
			weightage.setCountry(country);

			weightageUtil.checkAndUpdateWeightage(weightage, allCountryWeightage);
			weightage = weightageDao.saveWeightage(weightage);
			return ResponseUtil.getCreated(weightage);
		}
		throw new InvalidInfoException("Given Country already contains a weightage");

	}

	@Override
	public ResponseEntity<ApiResponse<String>> updateCourseWeightageForAdminForm(long categoryId, Long subCategoryId,
			long courseId, Long weightage, Organization organisation) {
		
		if (!courseDao.isCourseExist(courseId))
			throw new IdNotFoundException("No course found with the id :" + courseId);
		
		List<Weightage> weightages = weightageDao.getWeightagesByCourseId(courseId);
		Weightage obj = null;
		if(subCategoryId!=null) {
			obj = weightages.stream().filter(w->w.getCourse_SubCategoryId()==subCategoryId.longValue()).findFirst().get();
		}
		else {
			obj = weightages.stream().filter(w->w.getCourse_categoryId()==categoryId).findFirst().get();
		}
	    WeightageDto dto = weightageMapper.weightageDtoMapper(obj);
	    dto = weightageUtil.setWeightageInDto(dto,organisation,weightage);
		return updateCourseWeightage(categoryId, subCategoryId, courseId, dto);
	}

}
