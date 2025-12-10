package com.webapp.backend.repository;

import com.webapp.backend.Entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepository extends JpaRepository<Company, Long> {
    boolean existsByDomain(String domain);
}
