package com.pep.securemessagehandler.message.service;

import com.pep.securemessagehandler.message.model.Message;

import java.util.List;

public interface MessageService {

    Message save(Message message);

    void saveAll(Iterable<Message> messages);

    Message get(Integer id);

    List<Message> getForProcess(Message.Type type);

}
