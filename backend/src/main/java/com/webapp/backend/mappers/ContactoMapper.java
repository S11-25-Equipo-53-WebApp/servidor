package com.webapp.backend.mappers;

import org.springframework.stereotype.Component;
import com.webapp.backend.Entities.Contacto;
import com.webapp.backend.Entities.Usuario;
import com.webapp.backend.Entities.Empresa;
import com.webapp.backend.dto.contacto.ContactoRequestDTO;
import com.webapp.backend.dto.contacto.ContactoResponseDTO;

@Component
public class ContactoMapper {

    // Convierte Entity -> ResponseDTO
    public ContactoResponseDTO toResponseDTO(Contacto contacto) {
        if (contacto == null) return null;
        return new ContactoResponseDTO(
            contacto.getId(),
            contacto.getNombre(),
            contacto.getEmail(),
            contacto.getTelefonoWhatsapp(),
            contacto.getEstadoFunnel(),
            contacto.getDataCreacionContacto()
        );
    }

    // Convierte RequestDTO -> Entity 
    public Contacto toEntity(ContactoRequestDTO dto) {
        if (dto == null) return null;
        
        Contacto contacto = new Contacto();
        contacto.setNombre(dto.nombre());
        contacto.setEmail(dto.email());
        contacto.setTelefonoWhatsapp(dto.telefonoWhatsapp());
        
        // 1. Mapear Usuario (Si viene el ID en el DTO)
        if (dto.usuarioId() != null) {
            Usuario usuario = new Usuario();
            usuario.setId(dto.usuarioId());
            contacto.setUsuario(usuario);
        }

        // 2. Mapear Empresa
        if (dto.empresaId() != null) {
            Empresa empresa = new Empresa();
            empresa.setId(dto.empresaId()); 
            contacto.setEmpresa(empresa); 
        }
        
        return contacto;
    }
}