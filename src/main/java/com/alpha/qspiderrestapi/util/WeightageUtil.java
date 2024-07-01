package com.alpha.qspiderrestapi.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alpha.qspiderrestapi.entity.Category;
import com.alpha.qspiderrestapi.entity.Course;
import com.alpha.qspiderrestapi.entity.SubCategory;
import com.alpha.qspiderrestapi.entity.Weightage;
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
		if(qspDomainName.equals(hostname) ) {
			return (category.getWeightage()!= null)?(category.getWeightage().getQspiders()):0l;
		}
		else if(jspDomainName.equals(hostname)) {
			return (category.getWeightage()!= null)?(category.getWeightage().getJspiders()):0l;
		}
		else if(pyspDomainName.equals(hostname)) {
			return (category.getWeightage()!= null)?(category.getWeightage().getPyspiders()):0l;
		}
		else if(bspDomainName.equals(hostname)) {
			return (category.getWeightage()!= null)?(category.getWeightage().getBspiders()):0l;
		}
		else {
			throw new DomainMismatchException("Domain name is not matching any Organisation Type ");
		}
	}
	
	public long getSubCategoryWeightage(SubCategory subCategory, String hostname,long categoryId) {
		if(qspDomainName.equals(hostname) ) {
			if(subCategory.getWeightage()!= null) {
				return subCategory.getWeightage().stream().filter(w->w.getSubCategory_categoryId()==categoryId).findFirst().get().getQspiders();
			}
			return 0l;
		}
		else if(jspDomainName.equals(hostname)) {
			if(subCategory.getWeightage()!= null) {
				return subCategory.getWeightage().stream().filter(w->w.getSubCategory_categoryId()==categoryId).findFirst().get().getJspiders();
			}
			return 0l;
		}
		else if(pyspDomainName.equals(hostname)) {
			if(subCategory.getWeightage()!= null) {
				return subCategory.getWeightage().stream().filter(w->w.getSubCategory_categoryId()==categoryId).findFirst().get().getPyspiders();
			}
			return 0l;
		}
		else if(bspDomainName.equals(hostname)) {
			if(subCategory.getWeightage()!= null) {
				return subCategory.getWeightage().stream().filter(w->w.getSubCategory_categoryId()==categoryId).findFirst().get().getBspiders();
			}
			return 0l;
		}
		else {
			throw new DomainMismatchException("Domain name is not matching any Organisation Type ");
		}
	}
	
	public long getCourseOfCategoryWeightage(Course course, String hostname,long categoryId) {
		if(qspDomainName.equals(hostname) ) {
			if(course.getWeightages()!= null && !course.getWeightages().isEmpty()) {
				for (Weightage weightage : course.getWeightages()) {
					if(weightage.getCourse_categoryId()!=null && weightage.getCourse_categoryId()==categoryId) {
						return weightage.getQspiders();
					}
				}
			}
			return 0l;
		}
		else if(jspDomainName.equals(hostname)) {
			if(course.getWeightages()!= null && !course.getWeightages().isEmpty()) {
				for (Weightage weightage : course.getWeightages()) {
					if(weightage.getCourse_categoryId()!=null && weightage.getCourse_categoryId()==categoryId) {
						return weightage.getJspiders();
					}
				}
			}
			return 0l;
		}
		else if(pyspDomainName.equals(hostname)) {
			if(course.getWeightages()!= null && !course.getWeightages().isEmpty()) {
				for (Weightage weightage : course.getWeightages()) {
					if(weightage.getCourse_categoryId()!=null && weightage.getCourse_categoryId()==categoryId) {
						return weightage.getPyspiders();
					}
				}
			}
			return 0l;
		}
		else if(bspDomainName.equals(hostname)) {
			if(course.getWeightages()!= null && !course.getWeightages().isEmpty()) {
				for (Weightage weightage : course.getWeightages()) {
					if(weightage.getCourse_categoryId()!=null && weightage.getCourse_categoryId()==categoryId) {
						return weightage.getBspiders();
					}
				}
			}
			return 0l;
		}
		else {
			throw new DomainMismatchException("Domain name is not matching any Organisation Type ");
		}
	}
	
	public List<Category> getSortedCategory(List<Category> categories, String hostname){
		categories.sort((a,b)->(int)getCategoryWeightage(a, hostname)-(int)getCategoryWeightage(b, hostname));
		return categories;
	}
	
	public List<SubCategory> getSortedSubCategory(List<SubCategory> subCategories, String hostname,long categoryId){
		subCategories.sort((a,b)->(int)getSubCategoryWeightage(a,hostname, categoryId)-(int)getSubCategoryWeightage(b,hostname, categoryId));
		return subCategories;
	}
	
	public List<Course> getSortedCourseOfCategory(List<Course> courses, String hostname,long categoryId){
		courses.sort((a,b)->(int)getCourseOfCategoryWeightage(a,hostname, categoryId)-(int)getCourseOfCategoryWeightage(b,hostname, categoryId));
		return courses;
	}

}
