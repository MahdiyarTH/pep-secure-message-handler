package com.pep.securemessagehandler.message.service;

import com.pep.securemessagehandler.message.model.Message;

public interface MessageService {

    Message save(Message message);

    Message get(Integer id);

}
