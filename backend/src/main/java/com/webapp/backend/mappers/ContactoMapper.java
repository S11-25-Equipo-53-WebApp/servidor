package com.webapp.backend.mappers;

import com.webapp.backend.Entities.User;
import org.springframework.stereotype.Component;
import com.webapp.backend.Entities.Contact;
import com.webapp.backend.Entities.Company;
import com.webapp.backend.dto.contacto.ContactoRequestDTO;
import com.webapp.backend.dto.contacto.ContactoResponseDTO;

import java.util.Date;
import java.util.List;

@Component
public class ContactoMapper {

    // Convierte Entity -> ResponseDTO
    public ContactoResponseDTO toResponseDTO(Contact contacto) {
        if (contacto == null) return null;
        return new ContactoResponseDTO(
            contacto.getId(),
            contacto.getName(),
            contacto.getEmail(),
            contacto.getWhatsappPhone(),
            contacto.getFunnelStatus(),
            contacto.getCreatedAt()
        );
    }

    // Convierte RequestDTO -> Entity 
    public Contact toEntity(ContactoRequestDTO dto) {
        if (dto == null) return null;
        
        Contact contacto = new Contact();
        contacto.setName(dto.name());
        contacto.setEmail(dto.email());
        contacto.setWhatsappPhone(dto.whatsappPhone());
        
        // 1. Mapear Usuario (Si viene el ID en el DTO)
        if (dto.userId() != null) {
            User user = new User();
            user.setId(dto.userId());
            contacto.setAssignedTo(user);
        }

        // 2. Mapear Empresa
        if (dto.companyId() != null) {
            Company company = new Company();
            company.setId(dto.companyId());
            contacto.setCompany(company);
        }
        
        return contacto;
    }

    public List<ContactoResponseDTO> toResponseList(List<Contact> contacts) {
        if (contacts == null) return null;

        return contacts.stream()
                .map(this::toResponseDTO)
                .toList();
    }

}