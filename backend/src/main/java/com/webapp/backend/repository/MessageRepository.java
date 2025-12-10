package com.webapp.backend.repository;

import com.webapp.backend.Entities.Message;
import com.webapp.backend.Entities.enums.DirectionMenssage;
import com.webapp.backend.Entities.enums.TypeMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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
}
