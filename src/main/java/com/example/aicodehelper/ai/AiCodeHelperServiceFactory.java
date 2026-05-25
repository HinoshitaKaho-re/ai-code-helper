package com.example.aicodehelper.ai;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiCodeHelperServiceFactory {

    @Resource
    private ChatModel qwenChatModel;

    //AI Service的创建方法
    @Bean
    public AiCodeHelperService aiCodeHelperService() {

        //实现会话记忆，最多保存10条消息的聊天记忆，超过部分会被丢弃
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        //建造者模式创建AI Service实例，指定接口和模型，同时注入会话记忆
        AiCodeHelperService aiCodeHelperService = AiServices.builder(AiCodeHelperService.class)
                .chatModel(qwenChatModel)
                .chatMemory(chatMemory)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10)) //每个会话ID对应一个独立的聊天记忆实例
                .build();
        return aiCodeHelperService;
    }

}
