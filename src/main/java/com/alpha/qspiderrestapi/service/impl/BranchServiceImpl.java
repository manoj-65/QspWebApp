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
import com.alpha.qspiderrestapi.dao.CityDao;
import com.alpha.qspiderrestapi.dao.CourseDao;
import com.alpha.qspiderrestapi.dto.ApiResponse;
import com.alpha.qspiderrestapi.dto.BranchByIdDto;
import com.alpha.qspiderrestapi.dto.BranchById_BatchDto;
import com.alpha.qspiderrestapi.dto.BranchById_CourseDto;
import com.alpha.qspiderrestapi.dto.BranchDto;
import com.alpha.qspiderrestapi.dto.CityDto;
import com.alpha.qspiderrestapi.dto.CountryDto;
import com.alpha.qspiderrestapi.dto.CourseDto;
import com.alpha.qspiderrestapi.entity.Branch;
import com.alpha.qspiderrestapi.entity.CityCourseBranchView;
import com.alpha.qspiderrestapi.exception.IdNotFoundException;
import com.alpha.qspiderrestapi.exception.InvalidPhoneNumberException;
import com.alpha.qspiderrestapi.modelmapper.BranchById_Mapper;
import com.alpha.qspiderrestapi.service.AWSS3Service;
import com.alpha.qspiderrestapi.service.BranchService;
import com.alpha.qspiderrestapi.util.ResponseUtil;
import com.alpha.qspiderrestapi.util.ValidatePhoneNumber;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BranchServiceImpl implements BranchService {

	@Autowired
	private BranchDao branchDao;

	@Autowired
	private CourseDao courseDao;

	@Autowired
	private AWSS3Service awss3Service;

	@Autowired
	private CityCourseBranchViewDao viewDao;

	@Autowired
	private CityDao cityDao;

	@Autowired
	private ValidatePhoneNumber validatePhoneNumber;

	@Override
	public ResponseEntity<ApiResponse<Branch>> saveBranch(Branch branch) {
		log.info("Saving branch: {}", branch);
		branch.setBranchTitle(branch.getDisplayName() + "-" + branch.getBranchType());
		for (String contact : branch.getContacts()) {
			if (!validatePhoneNumber.isValidPhoneNumber(contact)) {
				throw new InvalidPhoneNumberException("Invalid contact : " + contact);
			}
		}
		branch.setBranchFaqs(
				branch.getBranchFaqs().stream().peek((faqs) -> faqs.setBranch(branch)).collect(Collectors.toList()));
		log.info("Branch saved successfully: {}", branch);
		Branch savedBranch = branchDao.saveBranch(branch);
		cityDao.updateCityBranchCount();
		return ResponseUtil.getCreated(savedBranch);
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
			} else {
				log.error("Icon cant be uploaded due to admin restriction");
				throw new NullPointerException("Icon can't be Upload Due the Admin restriction");
			}
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
       
        // Group by country -> city -> courseId
        Map<String, Map<String, Map<Long, List<CityCourseBranchView>>>> groupedData = view.stream()
                .collect(Collectors.groupingBy(
                        CityCourseBranchView::getCountry,
                        Collectors.groupingBy(
                                CityCourseBranchView::getCity,
                                Collectors.groupingBy(
                                        CityCourseBranchView::getCourseId))));

        List<CountryDto> countries = new ArrayList<>();

        // Process grouped data
        groupedData.forEach((countryName, citiesMap) -> {
            CountryDto country = new CountryDto();
            country.setCountryName(countryName);
            List<CityDto> cities = new ArrayList<>();

            citiesMap.forEach((cityName, coursesMap) -> {
                CityDto city = new CityDto();
                city.setCityName(cityName);
                CityCourseBranchView anyBranch = coursesMap.values().iterator().next().get(0);
                city.setCityIcon(anyBranch.getCityIconUrl());
                city.setCityImage(anyBranch.getCityImageUrl());
                city.setBranchCount(anyBranch.getBranchCount());
                List<CourseDto> courses = new ArrayList<>();
                
                coursesMap.forEach((courseId, branchesList) -> {
                	CourseDto course = new CourseDto();
                    course.setCourseId(courseId);
                    course.setCourseName(branchesList.get(0).getCourseName());
                    course.setCourseIcon(branchesList.get(0).getCourseIcon());
                    course.setCourseDescription(branchesList.get(0).getCourseDescription());
                    List<BranchDto> branches = branchesList.stream().distinct().map(branchView -> {
                        BranchDto branch = new BranchDto();
                        branch.setBranchId(branchView.getBranchId());
                        branch.setBranchName(branchView.getDisplayName());
                        branch.setBranchImage(branchView.getBranchImage());
                        branch.setLocation(branchView.getLocation());
                        branch.setPhoneNumber(branchView.getContacts());
                        branch.setUpcomingBatches(branchView.getUpcomingBatches());
						branch.setOngoingBatches(branchView.getOngoingBatches());
                        return branch;
                    }).sorted(Comparator.comparing(BranchDto::getBranchId)).collect(Collectors.toList());
                    course.setBranches(branches);
                    courses.add(course);
              
                });
                
                // Sort courses by courseId
                courses.sort(Comparator.comparing(CourseDto::getCourseId));
                city.setCourses(courses);
                cities.add(city);
            });

            // Sort cities by city name
            cities.sort(Comparator.comparing(CityDto::getCityName));
            country.setCities(cities);
            countries.add(country);
        });

        // Sort countries by country name
        countries.sort(Comparator.comparing(CountryDto::getCountryName));
        return ResponseUtil.getOk(countries);
    }



	@Override
	public ResponseEntity<ApiResponse<BranchByIdDto>> fetchById(long branchId, long courseId) {

		if (branchDao.isBranchPresent(branchId)) {
			if (courseDao.isCourseExist(courseId)) {
				Branch branch = branchDao.findBranchWithUpcomingBatches(branchId);

				BranchByIdDto branchResponse = BranchById_Mapper.mapToBranchByIdDto(branch);
				branchResponse.getCourses()
						.sort(Comparator.comparing((BranchById_CourseDto dto) -> dto.getCourseId() != courseId)
								.thenComparing(BranchById_CourseDto::getCourseId));
				branchResponse.getBatches()
						.sort(Comparator
								.comparing((BranchById_BatchDto dto) -> !dto.getBatchName()
										.equals(branchResponse.getCourses().get(0).getCourseName()))
								.thenComparing(BranchById_BatchDto::getBatchId));

				return ResponseUtil.getOk(branchResponse);
			} else
				throw new IdNotFoundException("Course not found with the Id : " + courseId);
		}
		throw new IdNotFoundException("Branch not found with the Id : " + branchId);

	}

	@Override
	@Transactional
	public ResponseEntity<ApiResponse<String>> deleteById(long branchId) {
		if(branchDao.isBranchPresent(branchId))
			branchDao.deleteBranch(branchId);
		else
			throw new IdNotFoundException("No branch found with the id: "+branchId);
		return ResponseUtil.getNoContent("Deleted Successfully");
	}

	@Override
	public ResponseEntity<ApiResponse<String>> updateBranchLocation(long branchId, String location) {
		if(branchDao.isBranchPresent(branchId)) {
			branchDao.updateBranchLocation(branchId,location);
			return ResponseUtil.getOk("Updated Successfull");
		}
			throw new IdNotFoundException("Branch with the Id: "+branchId);
			
	}

}
