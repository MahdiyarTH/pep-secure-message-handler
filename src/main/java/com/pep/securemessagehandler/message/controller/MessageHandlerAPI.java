package com.pep.securemessagehandler.message.controller;

import com.pep.securemessagehandler.message.model.Message;
import com.pep.securemessagehandler.message.service.MessageFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apis/v1/messages")
public class MessageHandlerAPI {

    private final MessageFacade messageFacade;

    @PostMapping
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(messageFacade.createMessage(message));
    }

    @GetMapping("/{id}/secret-key")
    public byte[] getMessageSecretKey(@PathVariable Integer id) {
        return this.messageFacade.getSecretKey(id);
    }

}
