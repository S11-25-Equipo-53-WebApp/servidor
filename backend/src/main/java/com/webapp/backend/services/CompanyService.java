package com.webapp.backend.services;

import com.webapp.backend.Entities.Company;
import com.webapp.backend.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final EmpresaRepository repository;

    public Company getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id " + id));
    }

    public Company create(String name, String domain) {

        Company company = new Company();
        company.setName(name);
        company.setDomain(domain);
        company.setDataCreacionCompany(new Date());

        return repository.save(company);
    }

    public Company update(Long id, Company data) {
        Company company = getById(id);

        company.setName(data.getName());
        company.setDomain(data.getDomain());
        company.setDataCreacionCompany(company.getDataCreacionCompany());

        return repository.save(company);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
