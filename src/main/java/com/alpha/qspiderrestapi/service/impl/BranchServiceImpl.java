package com.alpha.qspiderrestapi.service.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.alpha.qspiderrestapi.dto.BranchFileRequestDto;
import com.alpha.qspiderrestapi.dto.BranchRequestDto;
import com.alpha.qspiderrestapi.dto.CityDto;
import com.alpha.qspiderrestapi.dto.CountryDto;
import com.alpha.qspiderrestapi.dto.CourseDto;
import com.alpha.qspiderrestapi.dto.OrganizationDto;
import com.alpha.qspiderrestapi.dto.UpdateBranchRequestDto;
import com.alpha.qspiderrestapi.entity.Branch;
import com.alpha.qspiderrestapi.entity.City;
import com.alpha.qspiderrestapi.entity.CityCourseBranchView;
import com.alpha.qspiderrestapi.entity.enums.Organization;
import com.alpha.qspiderrestapi.exception.DomainMismatchException;
import com.alpha.qspiderrestapi.exception.IdNotFoundException;
import com.alpha.qspiderrestapi.exception.InvalidInfoException;
import com.alpha.qspiderrestapi.exception.InvalidPhoneNumberException;
import com.alpha.qspiderrestapi.modelmapper.BranchById_Mapper;
import com.alpha.qspiderrestapi.service.AWSS3Service;
import com.alpha.qspiderrestapi.service.BranchService;
import com.alpha.qspiderrestapi.util.ResponseUtil;
import com.alpha.qspiderrestapi.util.ValidatePhoneNumber;
import com.alpha.qspiderrestapi.util.WeightageUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

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

	@Autowired
	private WeightageUtil weightageUtil;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${organization.qsp}")
	private String qspDomainName;

	@Value("${organization.jsp}")
	private String jspDomainName;

	@Value("${organization.pysp}")
	private String pyspDomainName;

	@Value("${organization.bsp}")
	private String prospDomainName;

	@Override
	public ResponseEntity<ApiResponse<Branch>> saveBranch(Branch branch) {
		Optional<City> city = cityDao.findCityByCityName(branch.getBranchAddress().getCity());
		if (city.isPresent()) {
			if (city.get().getWeightage() == null) {
				throw new InvalidInfoException("Given city does'nt have a weightage, Branch cannot be added");
			}
		} else {
			throw new IdNotFoundException("Given city info in the address not found in the database");
		}
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
//				return iconUrl;
			}
			log.error("Icon cant be uploaded due to admin restriction");
			throw new NullPointerException("Icon can't be Upload Due the Admin restriction");
		}
		log.error("Branch not found with ID: {}", branchId);
		throw new IdNotFoundException("Branch With the Given Id Not Found");
	}

	public ResponseEntity<ApiResponse<List<CountryDto>>> fetchAll(String domainName) {
		List<CityCourseBranchView> view = viewDao.fetchAll();
		// Group by country -> city -> courseId
		Map<String, Map<String, Map<Long, List<CityCourseBranchView>>>> groupedData = view.stream()
				.collect(Collectors.groupingBy(CityCourseBranchView::getCountry, Collectors.groupingBy(
						CityCourseBranchView::getCity, Collectors.groupingBy(CityCourseBranchView::getCourseId))));

		List<CountryDto> countries = new ArrayList<>();

		// Process grouped data
		groupedData.forEach((countryName, citiesMap) -> {
			CountryDto country = new CountryDto();
			country.setCountryName(countryName);

			CityCourseBranchView countryView = citiesMap.values().iterator().next().values().iterator().next().get(0);

			country.setCtQspiders(
					(Long) countryView.getCtQspiders() == null ? Integer.MAX_VALUE : countryView.getCtQspiders());
			country.setCtJspiders(
					((Long) countryView.getCtJspiders() == null) ? Integer.MAX_VALUE : countryView.getCtJspiders());
			country.setCtPyspiders(
					(Long) countryView.getCtPyspiders() == null ? Integer.MAX_VALUE : countryView.getCtPyspiders());
			country.setCtProspiders(
					(Long) countryView.getCtProspiders() == null ? Integer.MAX_VALUE : countryView.getCtProspiders());

			List<CityDto> cities = new ArrayList<>();

			citiesMap.forEach((cityName, coursesMap) -> {
				CityDto city = new CityDto();
				city.setCityName(cityName);
				CityCourseBranchView anyBranch = coursesMap.values().iterator().next().get(0);
				city.setCityIcon(anyBranch.getCityIconUrl());
				city.setCityImage(anyBranch.getCityImageUrl());
				city.setQspiders(anyBranch.getQspiders());
				city.setJspiders(anyBranch.getJspiders());
				city.setPyspiders(anyBranch.getPyspiders());
				city.setProspiders(anyBranch.getProspiders());
				city.setBranchCount(anyBranch.getBranchCount());
				List<CourseDto> courses = new ArrayList<>();

				coursesMap.forEach((courseId, branchesList) -> {
					CourseDto course = new CourseDto();
					course.setCourseId(courseId);
					course.setCourseName(branchesList.get(0).getCourseName());
					course.setCourseIcon(branchesList.get(0).getCourseIcon());
					course.setCourseDescription(branchesList.get(0).getCourseDescription());
					course.setCQspiders(branchesList.get(0).getCQspiders());
					course.setCJspiders(branchesList.get(0).getCJspiders());
					course.setCPyspiders(branchesList.get(0).getCPyspiders());
					course.setCProspiders(branchesList.get(0).getCProspiders());
					List<BranchDto> branches = branchesList.stream().distinct().map(branchView -> {
						BranchDto branch = new BranchDto();
						branch.setBranchId(branchView.getBranchId());
						branch.setBranchName(branchView.getDisplayName());
						branch.setBranchImage(branchView.getBranchImage());
						branch.setOrganizationType(branchView.getBranch_type());
						branch.setLocation(branchView.getLocation());
						branch.setPhoneNumber(branchView.getContacts());
						branch.setUpcomingBatches(branchView.getUpcomingBatches());
						branch.setOngoingBatches(branchView.getOngoingBatches());
						branch.setAddressId(branchView.getAddressId());
						branch.setState(branchView.getState());
						branch.setStreet(branchView.getStreet());
						branch.setPinCode(branchView.getPincode());
						return branch;
					}).sorted(Comparator.comparing(BranchDto::getBranchId)).collect(Collectors.toList());
					course.setBranches(branches);
					courses.add(course);

				});

				// Sort courses by domain weightage
				List<CourseDto> sortedCourseDto = weightageUtil.getSortedCourseDto(courses, domainName);
				city.setCourses(sortedCourseDto);
				cities.add(city);
			});

			// Sort cities by domain weightage
			List<CityDto> sortedCity = weightageUtil.getSortedCity(cities, domainName);
			country.setCities(sortedCity);
			countries.add(country);
		});

		// Sort countries by country name
		List<CountryDto> sortedCountry = weightageUtil.getSortedCountry(countries, domainName);
//		countries.sort(Comparator.comparing(CountryDto::getCountryName));
		return ResponseUtil.getOk(sortedCountry);
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
		if (branchDao.isBranchPresent(branchId))
			branchDao.deleteBranch(branchId);
		else
			throw new IdNotFoundException("No branch found with the id: " + branchId);
		return ResponseUtil.getNoContent("Deleted Successfully");
	}

	@Override
	public ResponseEntity<ApiResponse<List<Branch>>> findAll() {
		return ResponseUtil.getOk(branchDao.findAll());
	}

	public ResponseEntity<ApiResponse<String>> updateBranchLocation(long branchId, String location) {
		if (branchDao.isBranchPresent(branchId)) {
			branchDao.updateBranchLocation(branchId, location);
			return ResponseUtil.getOk("Updated Successfull");
		}
		throw new IdNotFoundException("Branch with the Id: " + branchId);

	}

	@Override
	public ResponseEntity<ApiResponse<Branch>> saveBranchAlongWithFile(BranchFileRequestDto branchRequestDto) {

		MultipartFile branchImage = branchRequestDto.getBranchImage();
		List<MultipartFile> branchGallery = branchRequestDto.getBranchGallery();

		BranchRequestDto branchDto;
		try {
			objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
			Reader read = new StringReader(branchRequestDto.getBranch());
			branchDto = objectMapper.readValue(read, BranchRequestDto.class);
			System.err.println(branchDto);

		} catch (IOException e) {
			System.err.println(e);
			throw new InvalidInfoException("The Json body format is incorrect");
		}
		Branch branch = new Branch();
		log.info("Saving branch: {}", branchDto);
		branch.setDisplayName(branchDto.getDisplayName());
		branch.setBranchType(branchDto.getBranchType());
		branch.setBranchTitle(branchDto.getDisplayName() + "-" + branchDto.getBranchType());
		if (branchDto != null && branchDto.getContacts().stream()
				.anyMatch(contact -> !validatePhoneNumber.isValidPhoneNumber(contact))) {
			throw new InvalidPhoneNumberException("Invalid contact found");
		}
		branch.setBranchAddress(branchDto.getBranchAddress());
		branch.setBranchFaqs(branchDto.getBranchFaqs());
		Branch savedBranch = branchDao.saveBranch(branch);
		if (branchImage != null)
			uploadIcon(branchImage, branch.getBranchId());
		if (branchGallery != null)
			uploadImagesToGallery(branchGallery, branch.getBranchId());

		log.info("Branch saved successfully: {}", branch);
		cityDao.updateCityBranchCount();
		return ResponseUtil.getCreated(branch);
	}

	@Override
	public ResponseEntity<ApiResponse<Branch>> updateBranchAlongWithFile(BranchFileRequestDto branchRequestDto) {

		MultipartFile branchImage = branchRequestDto.getBranchImage();
		List<MultipartFile> branchGallery = branchRequestDto.getBranchGallery();
		UpdateBranchRequestDto branchDto;
		try {
			objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
			Reader read = new StringReader(branchRequestDto.getBranch());
			branchDto = objectMapper.readValue(read, UpdateBranchRequestDto.class);
			System.err.println(branchDto);

		} catch (IOException e) {
			System.err.println(e.getMessage());
			throw new InvalidInfoException("The Json body format is incorrect");
		}

		Branch branch = branchDao.fetchBranchById(branchDto.getBranchId()).orElseThrow(
				() -> new IdNotFoundException("Given branch Id " + branchDto.getBranchId() + " not found"));

		branch.setBranchId(branchDto.getBranchId());
		branch.setDisplayName(branchDto.getDisplayName());
		branch.setBranchType(branchDto.getBranchType());
		branch.setBranchTitle(branchDto.getDisplayName() + "-" + branchDto.getBranchType());
		if (branchDto != null && branchDto.getContacts().stream()
				.anyMatch(contact -> !validatePhoneNumber.isValidPhoneNumber(contact))) {
			throw new InvalidPhoneNumberException("Invalid contact found");
		}
		branch.setContacts(branchDto.getContacts());
		branch.setEmails(branchDto.getEmails());
		branch.setBranchAddress(branchDto.getBranchAddress());
		branch.setBranchFaqs(branchDto.getBranchFaqs());
		if (branchDto.getBranchImageUrl() != null && branchDto.getBranchGalleryUrl() != null) {
			branch.setGallery(branchDto.getBranchGalleryUrl());
			branch.setBranchImage(branchDto.getBranchImageUrl());
		}
		Branch savedBranch = branchDao.saveBranch(branch);
		if (branchImage != null)
			uploadIcon(branchImage, branch.getBranchId());
		if (branchGallery != null)
			uploadImagesToGallery(branchGallery, branch.getBranchId());

		log.info("Branch saved successfully: {}", branch);
		return ResponseUtil.getCreated(savedBranch);
	}

	@Override
	public ResponseEntity<ApiResponse<Branch>> fetchByIdForm(long branchId) {

		Branch branch = branchDao.fetchBranchById(branchId).orElseThrow(() -> new IdNotFoundException());
		return ResponseUtil.getOk(branch);

	}

	@Override
	public ResponseEntity<ApiResponse<List<CountryDto>>> getAllBranchesForForm(String domainName) {

		List<CityCourseBranchView> view = viewDao.fetchAll();
		// Group by country -> city -> branchType
		Map<String, Map<String, Map<String, List<CityCourseBranchView>>>> groupedData = view.stream()
				.collect(Collectors.groupingBy(CityCourseBranchView::getCountry, Collectors.groupingBy(
						CityCourseBranchView::getCity, Collectors.groupingBy(CityCourseBranchView::getBranch_type))));

		List<CountryDto> countries = new ArrayList<>();

		// Process grouped data
		groupedData.forEach((countryName, citiesMap) -> {
			CountryDto country = new CountryDto();
			country.setCountryName(countryName);

			CityCourseBranchView countryView = citiesMap.values().iterator().next().values().iterator().next().get(0);

			country.setCtQspiders(
					(Long) countryView.getCtQspiders() == null ? Integer.MAX_VALUE : countryView.getCtQspiders());
			country.setCtJspiders(
					((Long) countryView.getCtJspiders() == null) ? Integer.MAX_VALUE : countryView.getCtJspiders());
			country.setCtPyspiders(
					(Long) countryView.getCtPyspiders() == null ? Integer.MAX_VALUE : countryView.getCtPyspiders());
			country.setCtProspiders(
					(Long) countryView.getCtProspiders() == null ? Integer.MAX_VALUE : countryView.getCtProspiders());

			List<CityDto> cities = new ArrayList<>();

			citiesMap.forEach((cityName, organizationMap) -> {
				CityDto city = new CityDto();
				city.setCityName(cityName);
				CityCourseBranchView anyBranch = organizationMap.values().iterator().next().get(0);
				city.setQspiders(anyBranch.getQspiders());
				city.setJspiders(anyBranch.getJspiders());
				city.setPyspiders(anyBranch.getPyspiders());
				city.setProspiders(anyBranch.getProspiders());
				List<OrganizationDto> organizations = new ArrayList<>();

				organizationMap.forEach((orgType, branchesList) -> {
					OrganizationDto organization = new OrganizationDto();
					organization.setOrganization(orgType);
					List<BranchDto> branches = branchesList.stream().map(branchView -> {
						BranchDto branch = new BranchDto();
						branch.setBranchId(branchView.getBranchId());
						branch.setBranchName(branchView.getDisplayName());
						branch.setOrganizationType(branchView.getBranch_type());
						return branch;
					}).sorted(Comparator.comparing(BranchDto::getBranchId)).distinct().collect(Collectors.toList());
					organization.setBranches(branches);
					organizations.add(organization);

				});

				// set list of organization to city
				city.setOrganizations(organizations);
				cities.add(city);
			});

			// Sort cities by domain weightage
			List<CityDto> sortedCity = weightageUtil.getSortedCity(cities, domainName);
			country.setCities(sortedCity);
			countries.add(country);
		});

		// Sort countries by country name
		List<CountryDto> sortedCountry = weightageUtil.getSortedCountry(countries, domainName);
//		countries.sort(Comparator.comparing(CountryDto::getCountryName));
		return ResponseUtil.getOk(sortedCountry);

	}

	@Override
	public ResponseEntity<ApiResponse<List<CountryDto>>> viewall(String domainName) {

		List<CityCourseBranchView> view = viewDao.fetchAll();
		// Group by country -> city -> courseId
		Map<String, Map<String, List<CityCourseBranchView>>> groupedData = view.stream().collect(Collectors
				.groupingBy(CityCourseBranchView::getCountry, Collectors.groupingBy(CityCourseBranchView::getCity)));

		List<CountryDto> countries = new ArrayList<>();

		// Process grouped data
		groupedData.forEach((countryName, citiesMap) -> {
			CountryDto country = new CountryDto();
			country.setCountryName(countryName);

			CityCourseBranchView countryView = citiesMap.values().iterator().next().get(0);

			country.setCtQspiders(
					(Long) countryView.getCtQspiders() == null ? Integer.MAX_VALUE : countryView.getCtQspiders());
			country.setCtJspiders(
					((Long) countryView.getCtJspiders() == null) ? Integer.MAX_VALUE : countryView.getCtJspiders());
			country.setCtPyspiders(
					(Long) countryView.getCtPyspiders() == null ? Integer.MAX_VALUE : countryView.getCtPyspiders());
			country.setCtProspiders(
					(Long) countryView.getCtProspiders() == null ? Integer.MAX_VALUE : countryView.getCtProspiders());

			List<CityDto> cities = new ArrayList<>();

			citiesMap.forEach((cityName, branchesList) -> {
				CityDto city = new CityDto();
				city.setCityName(cityName);
				CityCourseBranchView anyBranch = branchesList.get(0);
				city.setCityIcon(anyBranch.getCityIconUrl());
				city.setCityImage(anyBranch.getCityImageUrl());
				city.setQspiders(anyBranch.getQspiders());
				city.setJspiders(anyBranch.getJspiders());
				city.setPyspiders(anyBranch.getPyspiders());
				city.setProspiders(anyBranch.getProspiders());
				city.setBranchCount(anyBranch.getBranchCount());

				List<BranchDto> branches = branchesList.stream().map(branchView -> {
					BranchDto branch = new BranchDto();
					branch.setBranchId(branchView.getBranchId());
					branch.setBranchName(branchView.getDisplayName());
					branch.setBranchImage(branchView.getBranchImage());
					branch.setOrganizationType(branchView.getBranch_type());
					branch.setLocation(branchView.getLocation());
					branch.setPhoneNumber(branchView.getContacts());
					branch.setUpcomingBatches(branchView.getUpcomingBatches());
					branch.setOngoingBatches(branchView.getOngoingBatches());
					branch.setAddressId(branchView.getAddressId());
					branch.setState(branchView.getState());
					branch.setStreet(branchView.getStreet());
					branch.setPinCode(branchView.getPincode());
					return branch;
				}).distinct().filter(b -> b.getOrganizationType().equals(getOrganization(domainName).toString()))
						.sorted(Comparator.comparing(BranchDto::getBranchId)).collect(Collectors.toList());
				city.setBranchDtos(branches);
				cities.add(city);
			});

			// Sort cities by domain weightage
			List<CityDto> sortedCity = weightageUtil.getSortedCity(cities, domainName);
			country.setCities(sortedCity);
			countries.add(country);
		});

		// Sort countries by country name
		List<CountryDto> sortedCountry = weightageUtil.getSortedCountry(countries, domainName);
//		countries.sort(Comparator.comparing(CountryDto::getCountryName));
		return ResponseUtil.getOk(sortedCountry);

	}

	private Organization getOrganization(String origin) {
		Organization organization = null;
		if (qspDomainName.equals(origin) || origin.contains("localhost"))
			organization = Organization.QSP;
		else if (jspDomainName.equals(origin))
			organization = Organization.JSP;
		else if (pyspDomainName.equals(origin))
			organization = Organization.PYSP;
		else if (prospDomainName.equals(origin))
			organization = Organization.PROSP;

		return organization;
	}

}
