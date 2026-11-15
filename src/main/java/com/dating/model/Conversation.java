package com.dating.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "conversations")
@CompoundIndex(name = "participant_idx", def = "{'participantIds': 1}")
public record Conversation(
        @Id String id,
        @Field("participant_ids") String[] participantIds,
        LocalDateTime lastMessageTime,
        String lastMessagePreview) {

    public Conversation {
        participantIds = participantIds == null ? null : participantIds.clone();
    }

    @Override
    public String[] participantIds() {
        return participantIds == null ? null : participantIds.clone();
    }
}
