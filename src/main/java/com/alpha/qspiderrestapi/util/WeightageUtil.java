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

	public void checkAndUpdateQspWeightage(Weightage weightage, List<Weightage> allWeightages) {

//		Weightage newWeightage = allWeightages.stream().filter(w -> w.getQspiders() == weightage.getQspiders()).limit(1)
//				.collect(Collectors.toList()).get(0);
//		System.err.println(allWeightages);
		System.err.println(weightage.getQspiders());
//		for (Weightage w : allWeightages) {
//			if (w.getQspiders() == weightage.getQspiders()) {
//				System.err.println("true");
//				newWeightageList.add(w);
//			}
//		}

		List<Weightage> weightages = null;

		if (weightage == null)
			weightageDao.saveWeightage(weightage);
		else {
			weightages = allWeightages.stream().filter(w -> w.getQspiders() > weightage.getQspiders())
					.peek(w -> w.setQspiders(w.getQspiders() + 1L)).collect(Collectors.toList());
		}
		weightageDao.saveAllWeightage(weightages);
	}

	public void checkAndUpdateJspWeightage(Weightage weightage, List<Weightage> allWeightages) {

//		Weightage newWeightage = allWeightages.stream().filter(w -> w.getJspiders() == weightage.getJspiders()).limit(1)
//				.collect(Collectors.toList()).get(0);
		List<Weightage> newWeightageList = new ArrayList();
		List<Weightage> weightages = null;
//		for (Weightage w : allWeightages) {
//			if (w.getJspiders() == weightage.getJspiders()) {
//				newWeightageList.add(w);
//			}
//		}

		if (weightage == null)
			weightageDao.saveWeightage(weightage);
		else {
			weightages = allWeightages.stream().filter(w -> w.getJspiders() > weightage.getJspiders())
					.peek(w -> w.setJspiders(w.getJspiders() + 1L)).collect(Collectors.toList());
		}
		weightageDao.saveAllWeightage(weightages);
	}

	public void checkAndUpdateBspWeightage(Weightage weightage, List<Weightage> allWeightages) {

//		Weightage newWeightage = allWeightages.stream().filter(w -> w.getBspiders() == weightage.getBspiders()).limit(1)
//				.collect(Collectors.toList()).get(0);
		List<Weightage> newWeightageList = new ArrayList();
		List<Weightage> weightages = null;
//		for (Weightage w : allWeightages) {
//			if (w.getBspiders() == weightage.getBspiders()) {
//				newWeightageList.add(w);
//			}
//		}
//		Weightage newWeightage = newWeightageList.get(0);

		if (weightage == null)
			weightageDao.saveWeightage(weightage);
		else {
			weightages = allWeightages.stream().filter(w -> w.getBspiders() > weightage.getBspiders())
					.peek(w -> w.setBspiders(w.getBspiders() + 1L)).collect(Collectors.toList());
		}
		weightageDao.saveAllWeightage(weightages);
	}

	public void checkAndUpdatePyspWeightage(Weightage weightage, List<Weightage> allWeightages) {

//		Weightage newWeightage = allWeightages.stream().filter(w -> w.getPyspiders() == weightage.getPyspiders())
//				.limit(1).collect(Collectors.toList()).get(0);
		List<Weightage> newWeightageList = new ArrayList();
		List<Weightage> weightages = null;
//		for (Weightage w : allWeightages) {
//			if (w.getPyspiders() == weightage.getPyspiders()) {
//				newWeightageList.add(w);
//			}
//		}
//		Weightage newWeightage = newWeightageList.get(0);

		if (weightage == null)
			weightageDao.saveWeightage(weightage);
		else {
			weightages = allWeightages.stream().filter(w -> w.getPyspiders() > weightage.getPyspiders())
					.peek(w -> w.setPyspiders(w.getPyspiders() + 1L)).collect(Collectors.toList());
		}

		weightageDao.saveAllWeightage(weightages);
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

}
