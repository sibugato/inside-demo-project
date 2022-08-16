package com.sibgatullinvv.insidedemoproject.dto;

import com.sibgatullinvv.insidedemoproject.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

/*
Репозиторий непосредственно взаимодействующий с таблицей "messages"
 */

public interface MessageRepository extends JpaRepository<Message, Long> {
}
