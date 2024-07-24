package com.alpha.qspiderrestapi.modelmapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alpha.qspiderrestapi.dto.SubjectDto;
import com.alpha.qspiderrestapi.entity.Chapter;
import com.alpha.qspiderrestapi.entity.Subject;
import com.alpha.qspiderrestapi.util.SubjectUtil;

@Component
public class SubjectMapper {

	@Autowired
	ChapterMapper chapterMapper;

	@Autowired
	SubjectUtil subjectUtil;

	public List<SubjectDto> mapSubjectToSubjectDto(List<Subject> subjects) {

		List<SubjectDto> subjectDtoList = new ArrayList<SubjectDto>();
		subjects.stream().forEach(subject -> {
			subjectDtoList.add(SubjectDto.builder().subjectTitle(subject.getSubjectTitle())
					.subjectDescription(subject.getSubjectDescrption())
					.chapters(chapterMapper.mapChapterToChapterDto(subject.getChapters()))
					.subjectModuleCount(subjectUtil.getModuleCount(subject.getChapters()))
					.subjecModuleDuration(
							subjectUtil.getModuleDuration(subject.getChapters(), Chapter::getChapterPreviewDuration))
					.build());
		});
		return subjectDtoList;
	}
 
}
