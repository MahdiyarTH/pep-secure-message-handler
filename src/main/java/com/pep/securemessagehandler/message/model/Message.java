package com.pep.securemessagehandler.message.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message")
public class Message {

    @Id
    private Integer id;

    @Enumerated(EnumType.STRING)
    Type type;

    String body;

    String sender;

    String scheduled;

    Instant createdAt;

    boolean processed;

    public enum Type {
        INFO,
        ERROR,
        WARNING
    }

}
