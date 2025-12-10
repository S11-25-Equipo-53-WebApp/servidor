package com.webapp.backend.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.webapp.backend.Entities.Company;
import com.webapp.backend.Entities.enums.FunnelStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webapp.backend.Entities.Contact;
import com.webapp.backend.Entities.enums.EstadoFunnel;
import com.webapp.backend.exceptions.ResourceNotFoundException;
import com.webapp.backend.repository.ContactoRepository;

@Service
public class ContactosService {

	@Autowired
	private ContactoRepository contactoRepository;

	@Autowired
	private CompanyService companyService;

	/**
	 * Valida si un Contacto existe, lanzando una excepción si no se encuentra.
	 * 
	 * @param id El ID del contacto a buscar.
	 * @return El objeto Contacto encontrado.
	 * @throws ResourceNotFoundException Si el contacto con el ID proporcionado no
	 *                                   existe.
	 */
	private Contact validarContactoExistente(Long id) {
		return contactoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Contacto no encontrado con el ID: " + id));
	}

	@Transactional
	public Contact createContacto(Contact contacto) {
		if (contacto.getName() == null || contacto.getName().trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre del Contacto es obligatorio.");
		}

		if (contacto.getFunnelStatus() == null) {
			contacto.setFunnelStatus(FunnelStatus.NEW);
		}

		contacto.setStatus(Boolean.TRUE);

		Date ahora = new Date();
		contacto.setCreatedAt(ahora);
		contacto.setUpdatedAt(ahora);

		return contactoRepository.save(contacto);
	}

	@Transactional(readOnly = true)
	public List<Contact> getAllContactos() {
		return contactoRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Contact getContactoById(Long id) {
		return validarContactoExistente(id);
	}

	@Transactional
	public Contact updateContacto(Long id, Contact contactoDetails) {
		Contact contactoExistente = validarContactoExistente(id);

		// Actualiza campos
		if (contactoDetails.getName() != null) {
			contactoExistente.setName(contactoDetails.getName());
		}
		if (contactoDetails.getEmail() != null) {
			contactoExistente.setEmail(contactoDetails.getEmail());
		}
		if (contactoDetails.getWhatsappPhone() != null) {
			contactoExistente.setWhatsappPhone(contactoDetails.getWhatsappPhone());
		}
		if (contactoDetails.getFunnelStatus() != null) {
			contactoExistente.setFunnelStatus(contactoDetails.getFunnelStatus());
		}
		if (contactoDetails.getAssignedTo() != null) {
			contactoExistente.setAssignedTo(contactoDetails.getAssignedTo());
		}
		if (contactoDetails.getStatus() != null) {
			contactoExistente.setStatus(contactoDetails.getStatus());
		}
		// Actualiza la fecha de modificación
		contactoExistente.setUpdatedAt(new Date());

		return contactoRepository.save(contactoExistente);
	}

	@Transactional
	public void deleteContacto(Long id) {
		Contact contactoExistente = validarContactoExistente(id);

		// Realiza la eliminación lógica (inactivación)
		// Establece el campo 'status' a FALSE para inactivar el contacto.
		contactoExistente.setStatus(Boolean.FALSE);

		contactoExistente.setUpdatedAt(new Date());
		contactoRepository.save(contactoExistente);

	}

	@Transactional
	public Contact getOrCreateContactByWhatsapp(String phone) {

		// Buscar contacto existente
		Contact contact = contactoRepository.findByWhatsappPhone(phone);
		Company company = companyService.getById(1L); // Empresa por defecto

		if (contact != null) {
			return contact;
		}

		// Si no existe, crearlo


		Contact newContact = new Contact();
		newContact.setName( "WhatsApp User " + phone);
		newContact.setWhatsappPhone(phone);
		newContact.setCreatedAt(new Date());
		newContact.setStatus(true);
		newContact.setCompany(company);

		return contactoRepository.save(newContact);
	}
}