package com.alpha.qspiderrestapi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.alpha.qspiderrestapi.dto.CategoryDashboardResponse;
import com.alpha.qspiderrestapi.dto.CourseResponse;
import com.alpha.qspiderrestapi.entity.Category;
import com.alpha.qspiderrestapi.entity.Course;
import com.alpha.qspiderrestapi.entity.SubCategory;
import com.alpha.qspiderrestapi.entity.enums.Mode;
import com.alpha.qspiderrestapi.modelmapper.CourseMapper;

@Component
public class CategoryUtil {

//	public List<CategoryDashboardResponse> mapToCategoryDashboardResponse(List<Category> categories) {
//		List<CategoryDashboardResponse> list = new ArrayList<CategoryDashboardResponse>();
//		for (Category category : categories) {
//			CategoryDashboardResponse response = new CategoryDashboardResponse();
//			if (category.getSubCategories().isEmpty()) {
//				response.setCategoryId(category.getCategoryId());
//				response.setCategoryName(category.getCategoryTitle());
//				response.setCategoryAlternativeIcon(category.getCategoryAlternativeIcon());
//				response.setOffline(sortByMode(category.getCourses(), Mode.OFFLINECLASSES));
//				response.setOnline(sortByMode(category.getCourses(), Mode.ONLINECLASSES));
//				response.setExperiential(sortByMode(category.getCourses(), Mode.EXPERIMENTALLEARNING));
//				response.setSelfpaced(sortByMode(category.getCourses(), Mode.SELFPACED));
//				list.add(response);
//			} else {
//				response.setCategoryId(category.getCategoryId());
//				response.setCategoryName(category.getCategoryTitle());
//				response.setCategoryAlternativeIcon(category.getCategoryAlternativeIcon());
//				response = mapCoursesFromSubCategory(category, response);
//				list.add(response);
//			}
//		}
//		return list;
//	}
//
//	private CategoryDashboardResponse mapCoursesFromSubCategory(Category category, CategoryDashboardResponse response) {
//		List<CourseResponse> offline = new ArrayList<>();
//		List<CourseResponse> online = new ArrayList<>();
//		List<CourseResponse> selfpaced = new ArrayList<>();
//		List<CourseResponse> experiencial = new ArrayList<>();
//
//		for (SubCategory courseResponse : category.getSubCategories()) {
//			offline.addAll(sortByMode(courseResponse.getCourses(), Mode.OFFLINECLASSES));
//			online.addAll(sortByMode(courseResponse.getCourses(), Mode.ONLINECLASSES));
//			selfpaced.addAll(sortByMode(courseResponse.getCourses(), Mode.SELFPACED));
//			experiencial.addAll(sortByMode(courseResponse.getCourses(), Mode.EXPERIMENTALLEARNING));
//		}
//		response.setOffline(offline);
//		response.setOnline(online);
//		response.setExperiential(experiencial);
//		response.setSelfpaced(selfpaced);s
//		return response;
//	}

	private List<CourseResponse> sortByMode(List<Course> courses, Mode mode) {
		courses = courses.stream().filter(course -> course.getMode().contains(mode)).collect(Collectors.toList());
		List<CourseResponse> responses = new ArrayList<>();
		for (Course course : courses) {
			responses.add(CourseMapper.mapToCourseResponse(course,0l));
		}
		return responses;
	}

}
