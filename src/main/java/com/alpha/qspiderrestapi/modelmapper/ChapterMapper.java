package com.alpha.qspiderrestapi.modelmapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alpha.qspiderrestapi.dto.ChapterDto;
import com.alpha.qspiderrestapi.entity.Chapter;
import com.alpha.qspiderrestapi.entity.Topic;
import com.alpha.qspiderrestapi.util.SubjectUtil;

@Component
public class ChapterMapper {

	@Autowired
	TopicMapper topicMapper;

	@Autowired
	SubjectUtil subjectUtil;

	public List<ChapterDto> mapChapterToChapterDto(List<Chapter> chapters) {

		List<ChapterDto> chapterDtoList = new ArrayList<ChapterDto>();
		chapters.stream().forEach(chapter -> {
			chapterDtoList.add(ChapterDto.builder().chapterTitle(chapter.getChapterTitle())
					.chapterDescription(chapter.getChapterDescription())
					.chapterPreviewUrl(chapter.getChapterPreviewUrl())
					.chapterPreviewDuration(chapter.getChapterPreviewDuration())
					.topics(topicMapper.mapTopicToTopicDto(chapter.getTopics()))
					.chapterModuleCount(subjectUtil.getModuleCount(chapter.getTopics()))
					.chapterModuleDuration(
							subjectUtil.getModuleDuration(chapter.getTopics(), Topic::getTopicPreviewDuration))
					.build());
		});
		return chapterDtoList;
	}

}
