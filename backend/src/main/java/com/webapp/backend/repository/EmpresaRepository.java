package com.webapp.backend.repository;

import com.webapp.backend.Entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Company, Long> {
}
