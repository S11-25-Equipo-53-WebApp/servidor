package com.webapp.backend.Entities;

import java.util.Date;

import com.webapp.backend.Entities.enums.EstadoFunnel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contactos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Contacto extends BaseEntity{
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "empresa_id")
	private Empresa empresa;
	@Column(name = "nombre", nullable = false)
	private String nombre;
	@Column(name = "email")
	private String email;
	@Column(name = "telefone_whatsapp")
	private String telefonoWhatsapp;
    @Enumerated(EnumType.STRING)
	@Column(name = "estado_funnel")
	private EstadoFunnel estadoFunnel;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_creacion_contacto")
	private Date dataCreacionContacto;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_atualizacion")
	private Date dataAtualizacion;
}
