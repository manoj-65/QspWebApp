package com.alpha.qspiderrestapi.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alpha.qspiderrestapi.dao.ChapterDao;
import com.alpha.qspiderrestapi.entity.Chapter;
import com.alpha.qspiderrestapi.repository.ChapterRepository;

@Repository
public class ChapterDaoImpl implements ChapterDao {

	@Autowired
	ChapterRepository chapterRepository;

	@Override
	public Chapter saveChapter(Chapter chapter) {
		return chapterRepository.save(chapter);
	}

	@Override
	public Optional<Chapter> fetchChapterById(long chapterId) {
		return chapterRepository.findById(chapterId);
	}

	@Override
	public List<Chapter> fetchAllChapters() {
		return chapterRepository.findAll();
	}

	@Override
	public void deleteChapter(long chapterId) {
		chapterRepository.deleteById(chapterId);
	}

	@Override
	public long isChapterPresent(long chapterId) {
		return chapterRepository.findByChapterId(chapterId);
	}

}
