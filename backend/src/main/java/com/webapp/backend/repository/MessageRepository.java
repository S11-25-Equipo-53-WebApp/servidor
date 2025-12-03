package com.webapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webapp.backend.Entities.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{

}
