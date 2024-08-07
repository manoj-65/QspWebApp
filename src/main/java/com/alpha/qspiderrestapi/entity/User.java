package com.alpha.qspiderrestapi.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.alpha.qspiderrestapi.entity.enums.Role;
import com.alpha.qspiderrestapi.entity.enums.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_info")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	private String userName;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Email> emails;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Contact> contacts;
	private String userPassword;
	@Enumerated(EnumType.STRING)
	private Role role;
	@Enumerated(EnumType.STRING)
	private Status status;
	private String userProfile;
	private String userToken;
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime creationDateTime;
	@UpdateTimestamp
	private LocalDateTime lastModifiedDateTime;
}
