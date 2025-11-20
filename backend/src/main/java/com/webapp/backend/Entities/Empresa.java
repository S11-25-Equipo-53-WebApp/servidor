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
@Table(name = "empresas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Empresa extends BaseEntity {

	@Column(name = "nombre", nullable = false)
	private String nombre;
	@Column(name = "dominio")
	private String dominio;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_creacion_empresa")
	private Date dataCreacionEmpresa;
}
