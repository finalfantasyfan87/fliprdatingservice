package com.dating.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.UUID;

@Document(collection = "user")
public record User(
        @Id String id,
        String email,
        String userName,
        String profilePictureUrl,
        String password,
        String gender,
        List<String> interests,
        String zodiacSign,
        @Field("conversation_ids") List<String> conversationIds) {

    public User {
        id = id == null ? UUID.randomUUID().toString() : id;
        conversationIds = conversationIds == null ? List.of() : List.copyOf(conversationIds);
        interests = interests == null ? null : List.copyOf(interests);
    }
}
