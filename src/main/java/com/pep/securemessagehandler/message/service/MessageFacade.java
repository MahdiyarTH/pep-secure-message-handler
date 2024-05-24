package com.pep.securemessagehandler.message.service;

import com.pep.securemessagehandler.message.model.Message;

public interface MessageFacade {

    Message createMessage(Message message);

    byte[] getSecretKey(Integer messageId);

}
