package com.alpha.qspiderrestapi.daoImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alpha.qspiderrestapi.dao.AddressDao;
import com.alpha.qspiderrestapi.dao.BatchDao;
import com.alpha.qspiderrestapi.dao.BranchDao;
import com.alpha.qspiderrestapi.dao.CategoryDao;
import com.alpha.qspiderrestapi.dao.CourseDao;
import com.alpha.qspiderrestapi.dao.FaqDao;
import com.alpha.qspiderrestapi.dao.ReviewDao;
import com.alpha.qspiderrestapi.dao.SubjectDao;
import com.alpha.qspiderrestapi.repository.BatchRepository;

@SpringBootTest
public class DaoImplTests {

	@Autowired
	private AddressDao addressDao;
	
	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private CourseDao courseDao;
	
	@Autowired
	private BranchDao branchDao;
	
	@Autowired
	private ReviewDao reviewDao;
	
	@Autowired
	private FaqDao faqDao;
	
	@Autowired
	private SubjectDao subjectDao;
	
	@Autowired
	private BatchRepository batchRepository;
	
	@Disabled
	@Test
	public void testIsEntityPresent() {
		
		assertEquals(1,addressDao.isAddressPresent(1));
		assertEquals(1, categoryDao.isCategoryPresent(1));
		assertEquals(1, courseDao.isCoursePresent(1));
		assertEquals(1, branchDao.isBranchPresent(1));
		assertEquals(3, reviewDao.isReviewPresent(3));
		assertEquals(1, faqDao.isFaqPresent(1));
		assertEquals(1, subjectDao.isSubjectPresent(1));
		
	}
	
	@Test
	public void updateToOngoing() {
		assertEquals(1, batchRepository.updateToOngoing());
	}
		
}
