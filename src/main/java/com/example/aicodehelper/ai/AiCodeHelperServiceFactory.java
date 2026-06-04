package com.example.aicodehelper.ai;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiCodeHelperServiceFactory {

    @Resource
    private ChatModel qwenChatModel;

    @Resource
    private PersistentChatMemoryStore persistentChatMemoryStore;

    //AI Service的创建方法
    @Bean
    public AiCodeHelperService aiCodeHelperService() {
        //建造者模式创建AI Service实例，指定接口和模型，同时注入持久化会话记忆
        return AiServices.builder(AiCodeHelperService.class)
                .chatModel(qwenChatModel)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(10)
                        .chatMemoryStore(persistentChatMemoryStore)
                        .build())
                .build();
    }

}
