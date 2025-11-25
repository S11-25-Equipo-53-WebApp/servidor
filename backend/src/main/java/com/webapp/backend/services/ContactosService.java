package com.webapp.backend.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webapp.backend.Entities.Contacto;
import com.webapp.backend.Entities.enums.EstadoFunnel;
import com.webapp.backend.exceptions.ResourceNotFoundException;
import com.webapp.backend.repository.ContactoRepository;

@Service
public class ContactosService {

	@Autowired
	private ContactoRepository contactoRepository;

	/**
	 * Valida si un Contacto existe, lanzando una excepción si no se encuentra.
	 * 
	 * @param id El ID del contacto a buscar.
	 * @return El objeto Contacto encontrado.
	 * @throws ResourceNotFoundException Si el contacto con el ID proporcionado no
	 *                                   existe.
	 */
	private Contacto validarContactoExistente(Long id) {
		return contactoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Contacto no encontrado con el ID: " + id));
	}

	@Transactional
	public Contacto createContacto(Contacto contacto) {
		if (contacto.getNombre() == null || contacto.getNombre().trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre del Contacto es obligatorio.");
		}

		if (contacto.getEstadoFunnel() == null) {
			contacto.setEstadoFunnel(EstadoFunnel.NUEVO);
		}

		contacto.setStatus(Boolean.TRUE);

		Date ahora = new Date();
		contacto.setDataCreacionContacto(ahora);
		contacto.setDataAtualizacion(ahora);

		return contactoRepository.save(contacto);
	}

	@Transactional(readOnly = true)
	public List<Contacto> getAllContactos() {
		return contactoRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Contacto getContactoById(Long id) {
		return validarContactoExistente(id);
	}

	@Transactional
	public Contacto updateContacto(Long id, Contacto contactoDetails) {
		Contacto contactoExistente = validarContactoExistente(id);

		// Actualiza campos
		if (contactoDetails.getNombre() != null) {
			contactoExistente.setNombre(contactoDetails.getNombre());
		}
		if (contactoDetails.getEmail() != null) {
			contactoExistente.setEmail(contactoDetails.getEmail());
		}
		if (contactoDetails.getTelefonoWhatsapp() != null) {
			contactoExistente.setTelefonoWhatsapp(contactoDetails.getTelefonoWhatsapp());
		}
		if (contactoDetails.getEstadoFunnel() != null) {
			contactoExistente.setEstadoFunnel(contactoDetails.getEstadoFunnel());
		}
		if (contactoDetails.getUsuario() != null) {
			contactoExistente.setUsuario(contactoDetails.getUsuario());
		}
		if (contactoDetails.getStatus() != null) {
			contactoExistente.setStatus(contactoDetails.getStatus());
		}
		// Actualiza la fecha de modificación
		contactoExistente.setDataAtualizacion(new Date());

		return contactoRepository.save(contactoExistente);
	}

	@Transactional
	public void deleteContacto(Long id) {
		Contacto contactoExistente = validarContactoExistente(id);

		// Realiza la eliminación lógica (inactivación)
		// Establece el campo 'status' a FALSE para inactivar el contacto.
		contactoExistente.setStatus(Boolean.FALSE);

		contactoExistente.setDataAtualizacion(new Date());
		contactoRepository.save(contactoExistente);

	}
}