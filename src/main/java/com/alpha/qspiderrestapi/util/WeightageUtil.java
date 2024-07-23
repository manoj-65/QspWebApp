package com.alpha.qspiderrestapi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alpha.qspiderrestapi.dao.WeightageDao;
import com.alpha.qspiderrestapi.dto.CityDto;
import com.alpha.qspiderrestapi.dto.CountryDto;
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
	private String prospDomainName;

	@Autowired
	private WeightageDao weightageDao;

	public long getCategoryWeightage(Category category, String hostname) {
		if (qspDomainName.equals(hostname) || hostname.contains("http://localhost")) {
			return (category.getWeightage() != null) ? (category.getWeightage().getQspiders()) : Integer.MAX_VALUE;
		} else if (jspDomainName.equals(hostname)) {
			return (category.getWeightage() != null) ? (category.getWeightage().getJspiders()) : Integer.MAX_VALUE;
		} else if (pyspDomainName.equals(hostname)) {
			return (category.getWeightage() != null) ? (category.getWeightage().getPyspiders()) : Integer.MAX_VALUE;
		} else if (prospDomainName.equals(hostname)) {
			return (category.getWeightage() != null) ? (category.getWeightage().getProspiders()) : Integer.MAX_VALUE;
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
		} else if (prospDomainName.equals(hostname)) {
			if (subCategory.getWeightage() != null && !subCategory.getWeightage().isEmpty()) {
				for (Weightage weightage : subCategory.getWeightage()) {
					if (weightage.getSubCategory_categoryId() != null
							&& weightage.getSubCategory_categoryId() == categoryId) {
						return weightage.getProspiders();
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
		} else if (prospDomainName.equals(hostname)) {
			if (course.getWeightages() != null && !course.getWeightages().isEmpty()) {
				for (Weightage weightage : course.getWeightages()) {
					if (weightage.getCourse_categoryId() != null && weightage.getCourse_categoryId() == categoryId) {
						return weightage.getProspiders();
					}
				}
			}
			return Integer.MAX_VALUE;
		} else {
			throw new DomainMismatchException("Domain name is not matching any Organisation Type ");
		}
	}

	public List<Category> getSortedCategory(List<Category> categories, String hostname) {
		return categories.stream().filter(c -> getCategoryWeightage(c, hostname) != 0l)
				.sorted((a, b) -> (int) getCategoryWeightage(a, hostname) - (int) getCategoryWeightage(b, hostname))
				.collect(Collectors.toList());
	}

	public List<SubCategory> getSortedSubCategory(List<SubCategory> subCategories, String hostname, long categoryId) {
		return subCategories.stream().filter(s -> getSubCategoryWeightage(s, hostname, categoryId) != 0l)
				.peek(s -> System.err.println(getSubCategoryWeightage(s, hostname, categoryId)))
				.sorted((a, b) -> (int) getSubCategoryWeightage(a, hostname, categoryId)
						- (int) getSubCategoryWeightage(b, hostname, categoryId))
				.collect(Collectors.toList());

	}

	public List<Course> getSortedCourseOfCategory(List<Course> courses, String hostname, long categoryId) {
		return courses.stream().filter(c -> getCourseOfCategoryWeightage(c, hostname, categoryId) != 0l)
				.sorted((a, b) -> (int) getCourseOfCategoryWeightage(a, hostname, categoryId)
						- (int) getCourseOfCategoryWeightage(b, hostname, categoryId))
				.collect(Collectors.toList());
	}

	public List<CityDto> getSortedCity(List<CityDto> cities, String hostname) {
		if (qspDomainName.equals(hostname) || hostname.contains("http://localhost")) {
			return cities.stream().filter(c -> c.getQspiders() != 0l)
					.sorted((a, b) -> (int) a.getQspiders() - (int) b.getQspiders()).collect(Collectors.toList());
		} else if (jspDomainName.equals(hostname)) {
			return cities.stream().filter(c -> c.getJspiders() != 0l)
					.sorted((a, b) -> (int) a.getJspiders() - (int) b.getJspiders()).collect(Collectors.toList());
		} else if (pyspDomainName.equals(hostname)) {
			return cities.stream().filter(c -> c.getPyspiders() != 0l)
					.sorted((a, b) -> (int) a.getPyspiders() - (int) b.getPyspiders()).collect(Collectors.toList());
		} else if (prospDomainName.equals(hostname)) {
			return cities.stream().filter(c -> c.getProspiders() != 0l)
					.sorted((a, b) -> (int) a.getProspiders() - (int) b.getProspiders()).collect(Collectors.toList());
		} else {
			throw new DomainMismatchException("Domain name is not matching any Organisation Type ");
		}
	}

	public List<CourseDto> getSortedCourseDto(List<CourseDto> courses, String hostname) {
		if (qspDomainName.equals(hostname) || hostname.contains("http://localhost")) {
			return courses.stream().filter(c -> c.getCQspiders() != 0l)
					.sorted((a, b) -> (int) a.getCQspiders() - (int) b.getCQspiders()).collect(Collectors.toList());

		} else if (jspDomainName.equals(hostname)) {
			return courses.stream().filter(c -> c.getCJspiders() != 0l)
					.sorted((a, b) -> (int) a.getCJspiders() - (int) b.getCJspiders()).collect(Collectors.toList());

		} else if (pyspDomainName.equals(hostname)) {
			return courses.stream().filter(c -> c.getCPyspiders() != 0l)
					.sorted((a, b) -> (int) a.getCPyspiders() - (int) b.getCPyspiders()).collect(Collectors.toList());

		} else if (prospDomainName.equals(hostname)) {
			return courses.stream().filter(c -> c.getCProspiders() != 0l)
					.sorted((a, b) -> (int) a.getCProspiders() - (int) b.getCProspiders()).collect(Collectors.toList());

		} else {
			throw new DomainMismatchException("Domain name is not matching any Organisation Type ");
		}
	}

	public boolean isValidOrganisation(Organization orgType) {
		for (Organization org : Organization.values()) {
			if (org.name().equals(orgType.toString()))
				return true;
		}
		return false;
	}

	public List<Weightage> checkAndUpdateQspWeightage(Weightage weightage, List<Weightage> allWeightages) {

		List<Weightage> weightages = null;
		weightages = allWeightages.stream().filter(w -> w.getQspiders() >= weightage.getQspiders())
				.peek(w -> w.setQspiders(w.getQspiders() + 1L)).collect(Collectors.toList());
		return weightages;
	}

	public List<Weightage> checkAndUpdateJspWeightage(Weightage weightage, List<Weightage> allWeightages) {

		List<Weightage> weightages = null;
		weightages = allWeightages.stream().filter(w -> w.getJspiders() >= weightage.getJspiders())
				.peek(w -> w.setJspiders(w.getJspiders() + 1L)).collect(Collectors.toList());
		return weightages;
	}

	public List<Weightage> checkAndUpdateBspWeightage(Weightage weightage, List<Weightage> allWeightages) {

		List<Weightage> weightages = null;
		weightages = allWeightages.stream().filter(w -> w.getProspiders() >= weightage.getProspiders())
				.peek(w -> w.setProspiders(w.getProspiders() + 1L)).collect(Collectors.toList());
		return weightages;
	}

	public List<Weightage> checkAndUpdatePyspWeightage(Weightage weightage, List<Weightage> allWeightages) {

		List<Weightage> weightages = null;
		weightages = allWeightages.stream().filter(w -> w.getPyspiders() >= weightage.getPyspiders())
				.peek(w -> w.setPyspiders(w.getPyspiders() + 1L)).collect(Collectors.toList());
		return weightages;
	}

	public List<Course> getSortedCourseOfSubCategory(List<Course> courses, String hostname, long subCategoryId) {
		return courses.stream().filter(c -> getCourseOfSubCategoryWeightage(c, hostname, subCategoryId) != 0l)
				.sorted((a, b) -> (int) getCourseOfSubCategoryWeightage(a, hostname, subCategoryId)
						- (int) getCourseOfSubCategoryWeightage(b, hostname, subCategoryId))
				.collect(Collectors.toList());
	}

	private long getCourseOfSubCategoryWeightage(Course course, String hostname, long subCategoryId) {
		if (qspDomainName.equals(hostname) || hostname.contains("http://localhost")) {
			if (course.getWeightages() != null && !course.getWeightages().isEmpty()) {
				for (Weightage weightage : course.getWeightages()) {
					if (weightage.getCourse_SubCategoryId() != null
							&& weightage.getCourse_SubCategoryId() == subCategoryId) {
						return weightage.getQspiders();
					}
				}
			}
			return Integer.MAX_VALUE;
		} else if (jspDomainName.equals(hostname)) {
			if (course.getWeightages() != null && !course.getWeightages().isEmpty()) {
				for (Weightage weightage : course.getWeightages()) {
					if (weightage.getCourse_SubCategoryId() != null
							&& weightage.getCourse_SubCategoryId() == subCategoryId) {
						return weightage.getJspiders();
					}
				}
			}
			return Integer.MAX_VALUE;
		} else if (pyspDomainName.equals(hostname)) {
			if (course.getWeightages() != null && !course.getWeightages().isEmpty()) {
				for (Weightage weightage : course.getWeightages()) {
					if (weightage.getCourse_SubCategoryId() != null
							&& weightage.getCourse_SubCategoryId() == subCategoryId) {
						return weightage.getPyspiders();
					}
				}
			}
			return Integer.MAX_VALUE;
		} else if (prospDomainName.equals(hostname)) {
			if (course.getWeightages() != null && !course.getWeightages().isEmpty()) {
				for (Weightage weightage : course.getWeightages()) {
					if (weightage.getCourse_SubCategoryId() != null
							&& weightage.getCourse_SubCategoryId() == subCategoryId) {
						return weightage.getProspiders();
					}
				}
			}
			return Integer.MAX_VALUE;
		} else {
			throw new DomainMismatchException("Domain name is not matching any Organisation Type ");
		}
	}

	public void checkAndUpdateWeightage(Weightage weightage, List<Weightage> allWeightages) {

		List<Weightage> weightages = new ArrayList<Weightage>();

		if (weightage.getQspiders() != 0)
			weightages = checkAndUpdateQspWeightage(weightage, allWeightages);

		if (weightage.getPyspiders() != 0)
			weightages = checkAndUpdateJspWeightage(weightage, allWeightages);

		if (weightage.getProspiders() != 0)
			weightages = checkAndUpdateBspWeightage(weightage, allWeightages);

		if (weightage.getJspiders() != 0)
			weightages = checkAndUpdatePyspWeightage(weightage, allWeightages);

		else {
			weightageDao.saveWeightage(weightage);
		}
		weightageDao.saveAllWeightage(weightages);

	}

	public long findMaxSize(List<Weightage> allWeightages, Organization orgType) {
		long maxSize = 0;
		if (orgType == Organization.QSP) {
			maxSize = allWeightages.stream().max((w1, w2) -> (int) w1.getQspiders() - (int) w2.getQspiders()).get()
					.getQspiders();
		}
		if (orgType == Organization.JSP) {
			maxSize = allWeightages.stream().max((w1, w2) -> (int) w1.getJspiders() - (int) w2.getJspiders()).get()
					.getJspiders();
		}
		if (orgType == Organization.PYSP) {
			maxSize = allWeightages.stream().max((w1, w2) -> (int) w1.getPyspiders() - (int) w2.getPyspiders()).get()
					.getPyspiders();
		}
		if (orgType == Organization.PROSP) {
			maxSize = allWeightages.stream().max((w1, w2) -> (int) w1.getProspiders() - (int) w2.getProspiders()).get()
					.getProspiders();
		}

		return maxSize;
	}

	public List<CountryDto> getSortedCountry(List<CountryDto> countries, String hostname) {
		if (qspDomainName.equals(hostname) || hostname.contains("http://localhost")) {
			return countries.stream().sorted((a, b) -> Long.compare(a.getCtQspiders(), b.getCtQspiders()))
					.collect(Collectors.toList());
		} else if (jspDomainName.equals(hostname)) {
			return countries.stream().sorted((a, b) -> (int) a.getCtJspiders() - (int) b.getCtJspiders())
					.collect(Collectors.toList());
		} else if (pyspDomainName.equals(hostname)) {
			return countries.stream().sorted((a, b) -> (int) a.getCtPyspiders() - (int) b.getCtPyspiders())
					.collect(Collectors.toList());
		} else if (prospDomainName.equals(hostname)) {
			return countries.stream().sorted((a, b) -> (int) a.getCtProspiders() - (int) b.getCtProspiders())
					.collect(Collectors.toList());
		} else {
			throw new DomainMismatchException("Domain name is not matching any Organisation Type ");
		}
	}
}
