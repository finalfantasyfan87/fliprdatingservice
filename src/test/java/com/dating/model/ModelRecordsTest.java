package com.dating.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ModelRecordsTest {

    @Test
    void userGeneratesAnIdAndEmptyConversationListWhenThoseFieldsAreMissing() {
        User user = new User(null, null, null, null, null, null, null, null, null);

        assertNotNull(user.id());
        assertEquals(List.of(), user.conversationIds());
    }

    @Test
    void conversationCopiesItsParticipantArrayOnInputAndOutput() {
        String[] participants = {"user-1", "user-2"};
        Conversation conversation = new Conversation("conversation-1", participants, null, null);
        participants[0] = "changed";
        String[] exposedParticipants = conversation.participantIds();
        exposedParticipants[1] = "changed";

        assertArrayEquals(new String[]{"user-1", "user-2"}, conversation.participantIds());
    }

    @Test
    void userCopiesMutableListInputs() {
        List<String> interests = new java.util.ArrayList<>(List.of("hiking"));
        User user = new User("user-1", null, null, null, null, null, interests, null, null);
        interests.add("music");

        assertEquals(List.of("hiking"), user.interests());
        assertThrows(UnsupportedOperationException.class, () -> user.interests().add("reading"));
    }
}
