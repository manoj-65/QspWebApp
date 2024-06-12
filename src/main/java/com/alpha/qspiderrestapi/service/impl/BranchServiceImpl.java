package com.alpha.qspiderrestapi.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.qspiderrestapi.dao.BranchDao;
import com.alpha.qspiderrestapi.dao.CityCourseBranchViewDao;
import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.BranchDto;
import com.alpha.qspiderrestapi.dto.CityDto;
import com.alpha.qspiderrestapi.dto.CountryDto;
import com.alpha.qspiderrestapi.dto.CourseDto;
import com.alpha.qspiderrestapi.entity.Branch;
import com.alpha.qspiderrestapi.entity.CityCourseBranchView;
import com.alpha.qspiderrestapi.exception.IdNotFoundException;
import com.alpha.qspiderrestapi.service.AWSS3Service;
import com.alpha.qspiderrestapi.service.BranchService;
import com.alpha.qspiderrestapi.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BranchServiceImpl implements BranchService {

	@Autowired
	private BranchDao branchDao;

	@Autowired
	private AWSS3Service awss3Service;

	@Autowired
	private CityCourseBranchViewDao viewDao;

	@Override
	public ResponseEntity<ApiResponse<Branch>> saveBranch(Branch branch) {
		log.info("Saving branch: {}", branch);
		branch.setBranchFaqs(
				branch.getBranchFaqs().stream().peek((faqs) -> faqs.setBranch(branch)).collect(Collectors.toList()));
		log.info("Branch saved successfully: {}", branch);
		return ResponseUtil.getCreated(branchDao.saveBranch(branch));
	}

	@Override

	public ResponseEntity<ApiResponse<String>> uploadImagesToGallery(List<MultipartFile> files, long branchId) {
		log.info("Entered uploadImageToGallery");
		String folder = "BRANCH/";
		Branch branch = branchDao.fetchBranchById(branchId).orElseThrow(() -> {
			log.error("Branch with given Id: {} " + branchId + " Not found");
			return new IdNotFoundException("Branch With the Given Id Not Found");
		});
		folder += branch.getBranchTitle();
		for (MultipartFile file : files) {
			String iconUrl = awss3Service.uploadFile(file, folder);
			if (!(iconUrl.isEmpty())) {
				log.info("Icon uploaded successfully to S3: {}", iconUrl);
				List<String> gallery = branch.getGallery();
				gallery.add(iconUrl);
				branch.setGallery(gallery);
				branchDao.saveBranch(branch);
			} else
				log.error("Icon cant be uploaded due to admin restriction");
			throw new NullPointerException("Icon can't be Upload Due the Admin restriction");
		}
		return ResponseUtil.getCreated("Icon Uplodaed!!");
	}

	public ResponseEntity<ApiResponse<String>> uploadIcon(MultipartFile file, long branchId) {
		log.info("Uploading icon for branch ID: {}", branchId);
		String folder = "BRANCH/";
		Optional<Branch> optionalBranch = branchDao.fetchBranchById(branchId);
		if (optionalBranch.isPresent()) {
			folder += optionalBranch.get().getBranchTitle();
			String iconUrl = awss3Service.uploadFile(file, folder);
			if (!iconUrl.isEmpty()) {
				log.info("File successfully uploaded to sw3: {}" + iconUrl);
				optionalBranch.get().setBranchImage(iconUrl);
				branchDao.saveBranch(optionalBranch.get());
				return ResponseUtil.getCreated(iconUrl);
			}
			log.error("Icon cant be uploaded due to admin restriction");
			throw new NullPointerException("Icon can't be Upload Due the Admin restriction");
		}
		log.error("Branch not found with ID: {}", branchId);
		throw new IdNotFoundException("Branch With the Given Id Not Found");
	}

	public ResponseEntity<ApiResponse<List<CountryDto>>> fetchAll() {
		List<CityCourseBranchView> view = viewDao.fetchAll();
		// Group by country
		Map<String, Map<String, Map<Long, List<CityCourseBranchView>>>> groupedData = view.stream()
				.collect(Collectors.groupingBy(CityCourseBranchView::getCountry, Collectors.groupingBy(
						CityCourseBranchView::getCity, Collectors.groupingBy(CityCourseBranchView::getCourseId))));

		List<CountryDto> countries = new ArrayList<>();

		groupedData.forEach((countryName, citiesMap) -> {
			CountryDto country = new CountryDto();
			country.setCountryName(countryName);
			List<CityDto> cities = new ArrayList<>();

			citiesMap.forEach((cityName, coursesMap) -> {
				CityDto city = new CityDto();
				city.setCityName(cityName);
				List<CourseDto> courses = new ArrayList<>();

				coursesMap.forEach((courseId, branchesList) -> {
					CourseDto course = new CourseDto();
					course.setCourseId(courseId);
					course.setCourseName(branchesList.get(0).getCourseName());
					List<BranchDto> branches = branchesList.stream().map(branchView -> {
						BranchDto branch = new BranchDto();
						branch.setBranchId(branchView.getBranchId());
						branch.setBranchName(branchView.getDisplayName());
						branch.setBranchImage(branchView.getBranchImage());
						branch.setLocation(branchView.getLocation());
						branch.setPhoneNumber(branchView.getContacts());
						branch.setUpcomingBatches(branchView.getUpcomingBatches());
						branch.setOngoingBatches(branchView.getUpcomingBatches());
						return branch;
					}).sorted(Comparator.comparing(BranchDto::getBranchId)).collect(Collectors.toList());
					course.setBranches(branches);
					courses.add(course);
				});
//	                courses.stream().sorted((a,b)->(int)a.getCourseId()-(int)b.getCourseId());
				courses.sort(Comparator.comparing(CourseDto::getCourseId));
				city.setCourses(courses);
				cities.add(city);
			});
			cities.sort(Comparator.comparing(CityDto::getCityName));
			country.setCities(cities);
			countries.add(country);
		});
		countries.sort(Comparator.comparing(CountryDto::getCountryName));
		return ResponseUtil.getOk(countries);
	}

}
