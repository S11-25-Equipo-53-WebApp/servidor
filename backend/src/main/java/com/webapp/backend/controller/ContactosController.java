package com.webapp.backend.controller;

import java.util.List;

import com.webapp.backend.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.backend.Entities.Contact;
import com.webapp.backend.dto.contacto.ContactoRequestDTO;
import com.webapp.backend.dto.contacto.ContactoResponseDTO;
import com.webapp.backend.mappers.ContactoMapper;
import com.webapp.backend.services.ContactosService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contactos")
@Tag(name = "Contatos", description = "Endpoints para la gestión de contactos")
public class ContactosController {

	@Autowired
	private ContactosService contactosService;

	@Autowired
	private ContactoMapper contactoMapper;

	@PostMapping
	@Operation(summary = "Cria um novo contato", description = "Registra um novo objeto Contacto no sistema", responses = {
			@ApiResponse(responseCode = "201", description = "Contato criado com sucesso", content = @Content(schema = @Schema(implementation = ContactoResponseDTO.class))) }) 																																			
	public ResponseEntity<ContactoResponseDTO> createContacto(@Valid @RequestBody ContactoRequestDTO contactoDTO) {
		// 1. Convertir DTO a Entidad
		Contact entityToSave = contactoMapper.toEntity(contactoDTO);

		// 2. Llamar al servicio
		Contact savedContact = contactosService.createContacto(entityToSave);

		// 3. Convertir Entidad guardada a ResponseDTO
		return new ResponseEntity<>(contactoMapper.toResponseDTO(savedContact), HttpStatus.CREATED);
	}

	@GetMapping
	@Operation(summary = "Consulta todos los contatos", description = "Consulta todos los contactos del sistema", responses = {
			@ApiResponse(responseCode = "200", description = "Consulta realizada", content = @Content(schema = @Schema(implementation = ContactoResponseDTO.class))) })
	public ResponseEntity<List<ContactoResponseDTO>> getAllContactos() {
		List<Contact> contactos = contactosService.getAllContactos();

		List<ContactoResponseDTO> response = contactos.stream().map(contactoMapper::toResponseDTO).toList();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca un contacto por ID", description = "Devuelve contacto específico baseado en el ID recibido", responses = {
			@ApiResponse(responseCode = "200", description = "Contacto encontrado", content = @Content(schema = @Schema(implementation = ContactoResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Contacto no encontrado") })
	public ResponseEntity<ContactoResponseDTO> getContactoById(@PathVariable Long id) {
		Contact contacto = contactosService.getContactoById(id);
		return new ResponseEntity<>(contactoMapper.toResponseDTO(contacto), HttpStatus.OK);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Actualiza un contacto", description = "Actualiza los detalles de un contacto por el ID", responses = {
			@ApiResponse(responseCode = "200", description = "Contacto actualizado con exito", content = @Content(schema = @Schema(implementation = ContactoResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Contacto no encontrado") })
	public ResponseEntity<ContactoResponseDTO> updateContacto(@PathVariable Long id,
			@RequestBody ContactoRequestDTO contactoDTO) {
		// Convertimos el DTO a una entidad temporal para pasar al servicio
		Contact contactoDetails = contactoMapper.toEntity(contactoDTO);

		Contact updatedContacto = contactosService.updateContacto(id, contactoDetails);

		return new ResponseEntity<>(contactoMapper.toResponseDTO(updatedContacto), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Inactiva un contato", description = "Inactiva un contato del sistema por su ID", responses = {
			@ApiResponse(responseCode = "204", description = "Contacto inactivado con exito"),
			@ApiResponse(responseCode = "404", description = "Contacto no encontrado") })
	public ResponseEntity<Void> deleteContacto(@PathVariable Long id) {
		contactosService.deleteContacto(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/assigned")
	public ResponseEntity<List<ContactoResponseDTO>> getContactsAssignedToUser(
			@AuthenticationPrincipal User authUser) {

		Long userId = authUser.getId();

		List<Contact> contacts = contactosService.getContactsAssignedToUser(userId);

		return ResponseEntity.ok(contactoMapper.toResponseList(contacts));
	}
}