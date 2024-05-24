package com.pep.securemessagehandler.message.repository;

import com.pep.securemessagehandler.message.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findAllByTypeAndProcessed(Message.Type type, boolean processed);

}
