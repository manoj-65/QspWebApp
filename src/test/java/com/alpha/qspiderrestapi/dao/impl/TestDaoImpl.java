package com.alpha.qspiderrestapi.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alpha.qspiderrestapi.dao.BatchDao;
import com.alpha.qspiderrestapi.dao.ChapterDao;
import com.alpha.qspiderrestapi.dao.ReviewDao;
import com.alpha.qspiderrestapi.dao.SubCategoryDao;
import com.alpha.qspiderrestapi.dao.SubTopicDao;
import com.alpha.qspiderrestapi.dao.TopicDao;

@SpringBootTest
class TestDaoImpl {

	@Autowired
	private SubTopicDao subTopicDao;

	@Autowired
	private ChapterDao chapterDao;

	@Autowired
	private BatchDao batchDao;

	@Autowired
	private ReviewDao reviewDao;

	@Autowired
	private SubCategoryDao subCategoryDao;

	@Autowired
	private TopicDao topicDao;

	@Test
	public void testIsPresentSubTopic() {
		long subTopicPresent = subTopicDao.isSubTopicPresent(1);
		assertEquals(1, subTopicPresent);
		System.out.println(subTopicPresent);
	}

	@Test
	public void testIsPresentChapter() {
		long chapterPresent = chapterDao.isChapterPresent(1);
		assertEquals(1, chapterPresent);
		System.out.println(chapterPresent);
	}

	@Test
	public void testIsPresentBatch() {
		long batchPresent = batchDao.isBatchPresent(1);
		assertEquals(1, batchPresent);
		System.out.println(batchPresent);
	}

	@Test
	public void testIsPresentReview() {
		long reviewPresent = reviewDao.isReviewPresent(1);
		assertEquals(1, reviewPresent);
		System.out.println(reviewPresent);
	}

	@Test
	public void testIsPresentSubCategory() {
		boolean subCategoryPresent = subCategoryDao.isSubCategoryPresent(1);
		assertEquals(true, subCategoryPresent);
		System.out.println(subCategoryPresent);
	}

	@Test
	public void testIsPresentTopic() {
		long topicPresent = topicDao.isTopicPresent(1);
		assertEquals(1, topicPresent);
		System.out.println(topicPresent);
	}

}
