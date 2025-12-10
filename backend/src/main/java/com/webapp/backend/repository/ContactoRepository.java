package com.webapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webapp.backend.Entities.Contact;

import java.util.Optional;

@Repository
public interface ContactoRepository extends JpaRepository<Contact, Long>{
    Optional<Contact> findByWhatsappPhoneAndCompanyId(String whatsappPhone, Long companyId);

    Contact findByWhatsappPhone(String whatsappPhone);

}
