package com.alpha.qspiderrestapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alpha.qspiderrestapi.entity.Chapter;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {

	@Query(value = "Select c.chapterId From Chapter c Where c.chapterId = :chapterId")
	Long findByChapterId(@Param("chapterId") long chapterId);
}
