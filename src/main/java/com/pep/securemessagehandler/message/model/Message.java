package com.pep.securemessagehandler.message.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
