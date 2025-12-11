package com.webapp.backend.repository;

import com.webapp.backend.Entities.Message;
import com.webapp.backend.Entities.enums.DirectionMenssage;
import com.webapp.backend.Entities.enums.TypeMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Historial de mensajes por contacto (ordenado)
    List<Message> findByContactIdOrderByTimestampAsc(Long contactId);

    // Mensajes filtrados por empresa
    List<Message> findByCompanyId(Long companyId);

    // Mensajes filtrados por tipo (whatsapp, email)
    List<Message> findByTypeAndCompanyId(TypeMessage type, Long companyId);

    // Buscar todos los entrantes de un contacto
    List<Message> findByContactIdAndDirection(Long contactId, DirectionMenssage direction);

    // Buscar por rango de fechas (útil para reportes)
    List<Message> findByCompanyIdAndTimestampBetween(Long companyId, Date start, Date end);

    List<Message> findByContactIdAndCompanyIdOrderByTimestampDesc(Long contactId, Long companyId);

    List<Message> findByContactIdAndUserIdOrderByTimestampAsc(Long contactId, Long userId);
    // Para obtener solo el último mensaje entre contacto y usuario
    Optional<Message> findTopByContactIdAndUserIdOrderByTimestampDesc(Long contactId, Long userId);

    Optional<Message> findTopByContactIdOrderByTimestampDesc(Long contactId);

    // Listado de **últimos mensajes por contacto** para contactos NO asignados
    @Query("SELECT m FROM Message m " +
            "WHERE m.contact.assignedTo IS NULL AND m.timestamp = " +
            "(SELECT MAX(m2.timestamp) FROM Message m2 WHERE m2.contact = m.contact)")
    List<Message> findLastMessagesOfUnassignedContacts();

    @Query("SELECT m FROM Message m " +
            "WHERE m.contact.assignedTo.id = :userId AND m.timestamp = " +
            "(SELECT MAX(m2.timestamp) FROM Message m2 WHERE m2.contact = m.contact)")
    List<Message> findLastMessagesOfAssignedContacts(@Param("userId") Long userId);

}
