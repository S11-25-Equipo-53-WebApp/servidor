package com.webapp.backend.Entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "company")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Company extends BaseEntity {

	@Column(name = "name", nullable = false)
	private String name;
	@Column(name = "domain")
	private String domain;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date dataCreacionCompany;
}
