package com.alpha.qspiderrestapi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alpha.qspiderrestapi.dao.WeightageDao;
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

	@Autowired
	private WeightageDao weightageDao;

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
		return categories.stream().filter(c->getCategoryWeightage(c,hostname)!=0l).sorted((a, b) -> (int) getCategoryWeightage(a, hostname) - (int) getCategoryWeightage(b, hostname)).collect(Collectors.toList());
	}

	public List<SubCategory> getSortedSubCategory(List<SubCategory> subCategories, String hostname, long categoryId) {
		return subCategories.stream()
				.filter(s->getSubCategoryWeightage(s, hostname, categoryId)!=0l)
				.peek(s->System.err.println(getSubCategoryWeightage(s, hostname, categoryId)))
				.sorted((a, b) -> (int) getSubCategoryWeightage(a, hostname, categoryId)- (int) getSubCategoryWeightage(b, hostname, categoryId))
				.collect(Collectors.toList());
		
	}

	public List<Course> getSortedCourseOfCategory(List<Course> courses, String hostname, long categoryId) {
		  return courses.stream()
						.filter(c->getCourseOfCategoryWeightage(c, hostname, categoryId)!=0l)
						.sorted((a, b) -> (int) getCourseOfCategoryWeightage(a, hostname, categoryId)- (int) getCourseOfCategoryWeightage(b, hostname, categoryId))
						.collect(Collectors.toList());
	}

	public List<CityDto> getSortedCity(List<CityDto> cities, String hostname) {
		if (qspDomainName.equals(hostname) || hostname.contains("http://localhost")) {
			return cities.stream()
					  .filter(c->c.getQspiders()!=0l)
					  .sorted((a, b) -> (int) a.getQspiders() - (int) b.getQspiders())
					  .collect(Collectors.toList());
		} else if (jspDomainName.equals(hostname)) {
			return cities.stream()
					  .filter(c->c.getJspiders()!=0l)
					  .sorted((a, b) -> (int) a.getJspiders() - (int) b.getJspiders())
					  .collect(Collectors.toList());
		} else if (pyspDomainName.equals(hostname)) {
			return cities.stream()
					  .filter(c->c.getPyspiders()!=0l)
					  .sorted((a, b) -> (int) a.getPyspiders() - (int) b.getPyspiders())
					  .collect(Collectors.toList());
		} else if (bspDomainName.equals(hostname)) {
			return cities.stream()
					  .filter(c->c.getBspiders()!=0l)
					  .sorted((a, b) -> (int) a.getBspiders() - (int) b.getBspiders())
					  .collect(Collectors.toList());
		} else {
			throw new DomainMismatchException("Domain name is not matching any Organisation Type ");
		}
	}

	public List<CourseDto> getSortedCourseDto(List<CourseDto> courses, String hostname) {
		if (qspDomainName.equals(hostname) || hostname.contains("http://localhost")) {
			return courses.stream()
						  .filter(c->c.getCQspiders()!=0l)
						  .sorted((a, b) -> (int) a.getCQspiders() - (int) b.getCQspiders())
						  .collect(Collectors.toList());

		} else if (jspDomainName.equals(hostname)) {
			return courses.stream()
					  .filter(c->c.getCJspiders()!=0l)
					  .sorted((a, b) -> (int) a.getCJspiders() - (int) b.getCJspiders())
					  .collect(Collectors.toList());
			
		} else if (pyspDomainName.equals(hostname)) {
			return courses.stream()
					  .filter(c->c.getCPyspiders()!=0l)
					  .sorted((a, b) -> (int) a.getCPyspiders() - (int) b.getCPyspiders())
					  .collect(Collectors.toList());
			
		} else if (bspDomainName.equals(hostname)) {
			return courses.stream()
					  .filter(c->c.getCBspiders()!=0l)
					  .sorted((a, b) -> (int) a.getCBspiders() - (int) b.getCBspiders())
					  .collect(Collectors.toList());
			
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
		weightages = allWeightages.stream().filter(w -> w.getBspiders() >= weightage.getBspiders())
				.peek(w -> w.setBspiders(w.getBspiders() + 1L)).collect(Collectors.toList());
		return weightages;
	}

	public List<Weightage> checkAndUpdatePyspWeightage(Weightage weightage, List<Weightage> allWeightages) {

		List<Weightage> weightages = null;
		weightages = allWeightages.stream().filter(w -> w.getPyspiders() >= weightage.getPyspiders())
				.peek(w -> w.setPyspiders(w.getPyspiders() + 1L)).collect(Collectors.toList());
		return weightages;
	}

	public List<Course> getSortedCourseOfSubCategory(List<Course> courses, String hostname, long subCategoryId) {
		return courses.stream()
				.filter(c->getCourseOfSubCategoryWeightage(c, hostname, subCategoryId)!=0l)
				.sorted((a, b) -> (int) getCourseOfSubCategoryWeightage(a, hostname, subCategoryId)- (int) getCourseOfSubCategoryWeightage(b, hostname, subCategoryId))
				.collect(Collectors.toList());
	}

	private long getCourseOfSubCategoryWeightage(Course course, String hostname, long subCategoryId) {
		if (qspDomainName.equals(hostname) || hostname.contains("http://localhost")) {
			if (course.getWeightages() != null && !course.getWeightages().isEmpty()) {
				for (Weightage weightage : course.getWeightages()) {
					if (weightage.getCourse_SubCategoryId() != null && weightage.getCourse_SubCategoryId() == subCategoryId) {
						return weightage.getQspiders();
					}
				}
			}
			return Integer.MAX_VALUE;
		} else if (jspDomainName.equals(hostname)) {
			if (course.getWeightages() != null && !course.getWeightages().isEmpty()) {
				for (Weightage weightage : course.getWeightages()) {
					if (weightage.getCourse_SubCategoryId() != null && weightage.getCourse_SubCategoryId() == subCategoryId) {
						return weightage.getJspiders();
					}
				}
			}
			return Integer.MAX_VALUE;
		} else if (pyspDomainName.equals(hostname)) {
			if (course.getWeightages() != null && !course.getWeightages().isEmpty()) {
				for (Weightage weightage : course.getWeightages()) {
					if (weightage.getCourse_SubCategoryId() != null && weightage.getCourse_SubCategoryId() == subCategoryId) {
						return weightage.getPyspiders();
					}
				}
			}
			return Integer.MAX_VALUE;
		} else if (bspDomainName.equals(hostname)) {
			if (course.getWeightages() != null && !course.getWeightages().isEmpty()) {
				for (Weightage weightage : course.getWeightages()) {
					if (weightage.getCourse_SubCategoryId() != null && weightage.getCourse_SubCategoryId() == subCategoryId) {
						return weightage.getBspiders();
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

		if (weightage.getQspiders() == 0 || weightage.getPyspiders() == 0 || weightage.getBspiders() == 0
				|| weightage.getJspiders() == 0)
			weightageDao.saveWeightage(weightage);
		else {
			if ((Long) weightage.getQspiders() != null)
				weightages = checkAndUpdateQspWeightage(weightage, allWeightages);

			if ((Long) weightage.getJspiders() != null)
				weightages = checkAndUpdateJspWeightage(weightage, allWeightages);

			if ((Long) weightage.getBspiders() != null)
				weightages = checkAndUpdateBspWeightage(weightage, allWeightages);

			if ((Long) weightage.getPyspiders() != null)
				weightages = checkAndUpdatePyspWeightage(weightage, allWeightages);

			weightageDao.saveAllWeightage(weightages);
		}
	}
}
