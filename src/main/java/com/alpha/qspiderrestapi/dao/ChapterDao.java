package com.alpha.qspiderrestapi.dao;

import java.util.List;
import java.util.Optional;

import com.alpha.qspiderrestapi.entity.Chapter;

public interface ChapterDao {

	Chapter saveChapter(Chapter chapter);

	Optional<Chapter> fetchChapterById(long chapterId);

	List<Chapter> fetchAllChapters();

	void deleteChapter(long chapterId);

	long isChapterPresent(long chapterId);
}
