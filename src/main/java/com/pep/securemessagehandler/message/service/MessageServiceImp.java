package com.pep.securemessagehandler.message.service;

import com.pep.securemessagehandler.message.model.Message;
import com.pep.securemessagehandler.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImp implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public Message save(Message message) {
        log.info("Saving message {}", message);
        return messageRepository.save(message);
    }

    @Override
    public Message get(Integer id) {
        log.info("Retrieving message with id {}", id);
        return messageRepository.findById(id).orElse(null);
    }

}
