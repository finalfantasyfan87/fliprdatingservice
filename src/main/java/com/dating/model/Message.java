package com.dating.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "messages")
public record Message(
        @Id String id,
        String senderId,
        String receiverId,
        String conversationId,
        String content,
        LocalDateTime timeStamp,
        boolean read) {
}
