package com.example.aicodehelper.ai;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j  //日志注解
public class AiCodeHelper {

    @Resource
    private ChatModel qwenChatModel;

    public String chat(String message) {
        //定义用户发出的消息
        UserMessage userMessage = UserMessage.from(message);
        //调用模型的chat方法传入消息
        ChatResponse chatResponse = qwenChatModel.chat(userMessage);
        //取出ai返回的结果
        AiMessage aiMessage = chatResponse.aiMessage();

        log.info("AI输出：" + aiMessage.toString());
        return aiMessage.toString();
    }
}
