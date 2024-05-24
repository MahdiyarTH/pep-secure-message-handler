package com.pep.securemessagehandler.message.service;

import com.pep.securemessagehandler.message.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageFacadeImp implements MessageFacade {

    private final MessageService messageService;
    private final Map<Integer, SecretKey> encryptionKeys = new ConcurrentHashMap<>();

    @Override
    public Message createMessage(Message message) {
        log.info("New message for creation: {}", message.toString());

        final SecretKey secretKey = generateSecretKey();
        message.setBody(encryptMessage(message, secretKey));

        message.setCreatedAt(Instant.now());
        message.setProcessed(false);
        message.setScheduled(getSchedule(message.getType()));

        log.info("Message saved into the db");

        final Message save = messageService.save(message);

        encryptionKeys.put(save.getId(), secretKey);

        return save;
    }

    @Override
    public byte[] getSecretKey(Integer messageId) {
        final SecretKey secretKey = this.encryptionKeys.get(messageId);
        if (secretKey == null)
            return new byte[0];

        return secretKey.getEncoded();
    }

    private String getSchedule(Message.Type type) {
        switch (type) {
            case INFO:
                return "0 0 * * *";

            case ERROR:
                return "0 */15 * * *";

            default:
            case WARNING:
                return "0 0 */2 * *";
        }
    }

    private String encryptMessage(Message message, SecretKey secretKey) {
        try {
            log.info("Encrypting message");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(message.getBody().getBytes());
            log.info("Message encrypted");

            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            log.error("Error encrypting message", e);
            throw new RuntimeException("Error encrypting message");
        }
    }

    private SecretKey generateSecretKey() {
        try {
            log.info("Creating secret key ...");
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            log.info("Secret key generated!");

            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            log.error("Error generating encryption key", e);
            throw new RuntimeException("Error generating encryption key");
        }
    }
}
