package com.webapp.backend.Entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.webapp.backend.Entities.enums.FunnelStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contact")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Contact extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id", nullable = false)
	private Company company;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "phone")
	private String phone;

	@Column(name = "email")
	private String email;

	@Column(name = "whatsapp_phone")
	private String whatsappPhone;

	@Enumerated(EnumType.STRING)
	@Column(name = "funnel_status")
	private FunnelStatus funnelStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assigned_to")
	private User assignedTo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	private Date updatedAt;

	@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ContactTag> tags = new ArrayList<>();
}
