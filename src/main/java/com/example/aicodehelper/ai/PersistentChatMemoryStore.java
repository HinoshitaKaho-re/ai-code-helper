package com.example.aicodehelper.ai;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import jakarta.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersistentChatMemoryStore implements ChatMemoryStore {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String sql = "SELECT messages FROM chat_memory WHERE memory_id = ?";
        List<String> rows = jdbcTemplate.queryForList(sql, String.class, memoryId.toString());
        if (rows.isEmpty()) {
            return List.of();
        }
        return ChatMessageDeserializer.messagesFromJson(rows.get(0));
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String json = ChatMessageSerializer.messagesToJson(messages);
        String sql = "INSERT INTO chat_memory (memory_id, messages) VALUES (?, ?) " +
                     "ON DUPLICATE KEY UPDATE messages = ?";
        jdbcTemplate.update(sql, memoryId.toString(), json, json);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        String sql = "DELETE FROM chat_memory WHERE memory_id = ?";
        jdbcTemplate.update(sql, memoryId.toString());
    }
}
