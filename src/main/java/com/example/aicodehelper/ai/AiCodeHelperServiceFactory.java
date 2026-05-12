package com.example.aicodehelper.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiCodeHelperServiceFactory {

    @Resource
    private ChatModel qwenChatModel;

    //AI Service的创建方法
    @Bean
    public AiCodeHelperService aiCodeHelperService() {
        //自动创建出该接口的实现类
        return AiServices.create(AiCodeHelperService.class, qwenChatModel);
    }

}
