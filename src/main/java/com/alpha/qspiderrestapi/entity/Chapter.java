package com.alpha.qspiderrestapi.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long chapterId;
	@Column(unique = true)
	private String chapterTitle;
	private String chapterDescription;

	@OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL)
	private List<Topic> topics = new ArrayList<Topic>();

	@ManyToOne
	@JoinColumn(name = "subjectId")
	@JsonIgnore
	private Subject subject;

//	private List<String> previews;

	@CreationTimestamp
	private LocalDateTime createdDateAndTime;
	@UpdateTimestamp
	private LocalDateTime updatedDateAndTime;

}
