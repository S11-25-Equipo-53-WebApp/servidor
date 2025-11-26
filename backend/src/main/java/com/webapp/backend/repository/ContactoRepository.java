package com.webapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webapp.backend.Entities.Contacto;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Long>{

}
