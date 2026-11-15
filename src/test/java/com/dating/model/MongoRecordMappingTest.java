package com.dating.model;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.convert.NoOpDbRefResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MongoRecordMappingTest {

    @Test
    void mapsRecordComponentsToMongoDocumentFieldsAndBack() throws Exception {
        MongoCustomConversions conversions = MongoCustomConversions.create(adapter -> {});
        MongoMappingContext mappingContext = new MongoMappingContext();
        mappingContext.setSimpleTypeHolder(conversions.getSimpleTypeHolder());
        mappingContext.setInitialEntitySet(Set.of(User.class, Conversation.class, Message.class));
        mappingContext.afterPropertiesSet();

        MappingMongoConverter converter = new MappingMongoConverter(
                NoOpDbRefResolver.INSTANCE, mappingContext);
        converter.setCustomConversions(conversions);
        converter.afterPropertiesSet();

        User user = new User("user-1", "user@example.com", "user", null, null,
                null, List.of("hiking"), "aries", List.of("conversation-1"));
        Document document = new Document();
        converter.write(user, document);

        assertEquals("user-1", document.getString("_id"));
        assertEquals(List.of("conversation-1"), document.get("conversation_ids"));
        assertEquals(user, converter.read(User.class, document));

        Conversation conversation = new Conversation(
                "conversation-1", new String[]{"user-1", "user-2"}, null, "hello");
        Document conversationDocument = new Document();
        converter.write(conversation, conversationDocument);

        assertEquals(List.of("user-1", "user-2"), conversationDocument.get("participant_ids"));
        Conversation restored = converter.read(Conversation.class, conversationDocument);
        assertEquals("conversation-1", restored.id());
        assertArrayEquals(conversation.participantIds(), restored.participantIds());

        Message message = new Message(
                "message-1", "user-1", "user-2", "conversation-1", "hello", null, false);
        Document messageDocument = new Document();
        converter.write(message, messageDocument);

        assertEquals(message, converter.read(Message.class, messageDocument));
    }
}
