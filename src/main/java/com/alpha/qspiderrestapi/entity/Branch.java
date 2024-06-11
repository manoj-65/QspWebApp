package com.alpha.qspiderrestapi.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.alpha.qspiderrestapi.entity.enums.Organization;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Branch {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long branchId;
	@Column(unique = true)
	private String branchTitle;

	private String displayName;

	@Enumerated(EnumType.STRING)
	private Organization branchType;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "branchId", referencedColumnName = "branchId")
	private List<Contact> contacts = new ArrayList<Contact>();

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "branchId", referencedColumnName = "branchId")
	private List<Email> emails = new ArrayList<Email>();

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "addressId")
	private Address branchAddress;

	@OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
	private List<Faq> branchFaqs = new ArrayList<Faq>();

	@OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
	private List<Review> branchReviews = new ArrayList<Review>();

	@OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
	private List<Batch> batches = new ArrayList<Batch>();

	private String branchImage;

	@ElementCollection
	@Column(name = "branch_gallery_url")
	private List<String> gallery = new ArrayList<String>();

	@CreationTimestamp
	private LocalDateTime createdDateAndTime;
	@UpdateTimestamp
	private LocalDateTime updatedDateAndTime;

}
