package com.alpha.qspiderrestapi.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alpha.qspiderrestapi.dto.CityDto;
import com.alpha.qspiderrestapi.dto.CourseDto;
import com.alpha.qspiderrestapi.entity.Category;
import com.alpha.qspiderrestapi.entity.Course;
import com.alpha.qspiderrestapi.entity.SubCategory;
import com.alpha.qspiderrestapi.entity.Weightage;
import com.alpha.qspiderrestapi.entity.enums.Organization;
import com.alpha.qspiderrestapi.exception.DomainMismatchException;

@Component
public class WeightageUtil {

	@Value("${organization.qsp}")
	private String qspDomainName;

	@Value("${organization.jsp}")
	private String jspDomainName;

	@Value("${organization.pysp}")
	private String pyspDomainName;

	@Value("${organization.bsp}")
	private String bspDomainName;

	public long getCategoryWeightage(Category category, String hostname) {
		if (qspDomainName.equals(hostname) || hostname.contains("http://localhost")) {
			return (category.getWeightage() != null) ? (category.getWeightage().getQspiders()) : Integer.MAX_VALUE;
		} else if (jspDomainName.equals(hostname)) {
			return (category.getWeightage() != null) ? (category.getWeightage().getJspiders()) : Integer.MAX_VALUE;
		} else if (pyspDomainName.equals(hostname)) {
			return (category.getWeightage() != null) ? (category.getWeightage().getPyspiders()) : Integer.MAX_VALUE;
		} else if (bspDomainName.equals(hostname)) {
			return (category.getWeightage() != null) ? (category.getWeightage().getBspiders()) : Integer.MAX_VALUE;
		} else {
			throw new DomainMismatchException("Domain name is not matching any Organisation Type ");
		}
	}

	public long getSubCategoryWeightage(SubCategory subCategory, String hostname, long categoryId) {
		if (qspDomainName.equals(hostname) || hostname.contains("http://localhost")) {
			if (subCategory.getWeightage() != null && !subCategory.getWeightage().isEmpty()) {
				for (Weightage weightage : subCategory.getWeightage()) {
					if (weightage.getSubCategory_categoryId() != null
							&& weightage.getSubCategory_categoryId() == categoryId) {
						return weightage.getQspiders();
					}
				}
			}
			return Integer.MAX_VALUE;
		} else if (jspDomainName.equals(hostname)) {
			if (subCategory.getWeightage() != null && !subCategory.getWeightage().isEmpty()) {
				for (Weightage weightage : subCategory.getWeightage()) {
					if (weightage.getSubCategory_categoryId() != null
							&& weightage.getSubCategory_categoryId() == categoryId) {
						return weightage.getJspiders();
					}
				}
			}
			return Integer.MAX_VALUE;
		} else if (pyspDomainName.equals(hostname)) {
			if (subCategory.getWeightage() != null && !subCategory.getWeightage().isEmpty()) {
				for (Weightage weightage : subCategory.getWeightage()) {
					if (weightage.getSubCategory_categoryId() != null
							&& weightage.getSubCategory_categoryId() == categoryId) {
						return weightage.getPyspiders();
					}
				}
			}
			return Integer.MAX_VALUE;
		} else if (bspDomainName.equals(hostname)) {
			if (subCategory.getWeightage() != null && !subCategory.getWeightage().isEmpty()) {
				for (Weightage weightage : subCategory.getWeightage()) {
					if (weightage.getSubCategory_categoryId() != null
							&& weightage.getSubCategory_categoryId() == categoryId) {
						return weightage.getBspiders();
					}
				}
			}
			return Integer.MAX_VALUE;
		} else {
			throw new DomainMismatchException("Domain name is not matching any Organisation Type ");
		}
	}

	public long getCourseOfCategoryWeightage(Course course, String hostname, long categoryId) {
		if (qspDomainName.equals(hostname) || hostname.contains("http://localhost")) {
			if (course.getWeightages() != null && !course.getWeightages().isEmpty()) {
				for (Weightage weightage : course.getWeightages()) {
					if (weightage.getCourse_categoryId() != null && weightage.getCourse_categoryId() == categoryId) {
						return weightage.getQspiders();
					}
				}
			}
			return Integer.MAX_VALUE;
		} else if (jspDomainName.equals(hostname)) {
			if (course.getWeightages() != null && !course.getWeightages().isEmpty()) {
				for (Weightage weightage : course.getWeightages()) {
					if (weightage.getCourse_categoryId() != null && weightage.getCourse_categoryId() == categoryId) {
						return weightage.getJspiders();
					}
				}
			}
			return Integer.MAX_VALUE;
		} else if (pyspDomainName.equals(hostname)) {
			if (course.getWeightages() != null && !course.getWeightages().isEmpty()) {
				for (Weightage weightage : course.getWeightages()) {
					if (weightage.getCourse_categoryId() != null && weightage.getCourse_categoryId() == categoryId) {
						return weightage.getPyspiders();
					}
				}
			}
			return Integer.MAX_VALUE;
		} else if (bspDomainName.equals(hostname)) {
			if (course.getWeightages() != null && !course.getWeightages().isEmpty()) {
				for (Weightage weightage : course.getWeightages()) {
					if (weightage.getCourse_categoryId() != null && weightage.getCourse_categoryId() == categoryId) {
						return weightage.getBspiders();
					}
				}
			}
			return Integer.MAX_VALUE;
		} else {
			throw new DomainMismatchException("Domain name is not matching any Organisation Type ");
		}
	}

	public List<Category> getSortedCategory(List<Category> categories, String hostname) {
		categories.sort((a, b) -> (int) getCategoryWeightage(a, hostname) - (int) getCategoryWeightage(b, hostname));
		return categories;
	}

	public List<SubCategory> getSortedSubCategory(List<SubCategory> subCategories, String hostname, long categoryId) {
		subCategories.sort((a, b) -> (int) getSubCategoryWeightage(a, hostname, categoryId)
				- (int) getSubCategoryWeightage(b, hostname, categoryId));
		return subCategories;
	}

	public List<Course> getSortedCourseOfCategory(List<Course> courses, String hostname, long categoryId) {
		courses.sort((a, b) -> (int) getCourseOfCategoryWeightage(a, hostname, categoryId)
				- (int) getCourseOfCategoryWeightage(b, hostname, categoryId));
		return courses;
	}

	public List<CityDto> getSortedCity(List<CityDto> cities, String hostname) {
		if (qspDomainName.equals(hostname) || hostname.contains("http://localhost")) {
			cities.sort((a, b) -> (int) a.getQspiders() - (int) b.getQspiders());
			return cities;
		} else if (jspDomainName.equals(hostname)) {
			cities.sort((a, b) -> (int) a.getJspiders() - (int) b.getJspiders());
			return cities;
		} else if (pyspDomainName.equals(hostname)) {
			cities.sort((a, b) -> (int) a.getPyspiders() - (int) b.getPyspiders());
			return cities;
		} else if (bspDomainName.equals(hostname)) {
			cities.sort((a, b) -> (int) a.getBspiders() - (int) b.getBspiders());
			return cities;
		} else {
			throw new DomainMismatchException("Domain name is not matching any Organisation Type ");
		}
	}

	public List<CourseDto> getSortedCourseDto(List<CourseDto> courses, String hostname) {
		if (qspDomainName.equals(hostname) || hostname.contains("http://localhost")) {
			courses.sort((a, b) -> (int) a.getCQspiders() - (int) b.getCQspiders());
			return courses;
		} else if (jspDomainName.equals(hostname)) {
			courses.sort((a, b) -> (int) a.getCJspiders() - (int) b.getCJspiders());
			return courses;
		} else if (pyspDomainName.equals(hostname)) {
			courses.sort((a, b) -> (int) a.getCPyspiders() - (int) b.getCPyspiders());
			return courses;
		} else if (bspDomainName.equals(hostname)) {
			courses.sort((a, b) -> (int) a.getCBspiders() - (int) b.getCBspiders());
			return courses;
		} else {
			throw new DomainMismatchException("Domain name is not matching any Organisation Type ");
		}
	}

	public boolean isValidOrganisation(Organization orgType) {
		for (Organization org : Organization.values()) {
			if (org.name().equals(orgType))
				return true;
		}
		return false;
	}

	public static void incrementWeightage(long qspWeightage, Long weightage) {

	}

}
