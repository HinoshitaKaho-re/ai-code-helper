package com.example.aicodehelper.ai;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
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

    //系统提示词全局唯一，在模型初始化时设置一次即可
    private static final String SYSTEM_MESSAGE = """
            你是编程领域的小助手，帮助用户解答编程学习和求职面试相关的问题，并给出建议。重点关注4个方向:
            1.规划清晰的编程学习路线
            2.提供项目学习建议
            3.给出程序员求职全流程指南(比如简历优化、投递技巧)
            4.分享高频面试题和面试技巧
            请用简洁易懂的语言回答，助力用户高效学习与求职。
            """;

    //简单对话
    public String chat(String message) {
        //引入提示词，chat方法支持多消息
        SystemMessage systemMessage = SystemMessage.from(SYSTEM_MESSAGE);
        //定义用户发出的消息
        UserMessage userMessage = UserMessage.from(message);
        //调用模型的chat方法传入消息
        ChatResponse chatResponse = qwenChatModel.chat(systemMessage,userMessage);
        //取出ai返回的结果
        AiMessage aiMessage = chatResponse.aiMessage();

        log.info("AI输出：" + aiMessage.toString());
        return aiMessage.text();
    }

    //简单对话 - 自定义用户消息
    public String chatWithMessage(UserMessage userMessage) {
        //调用模型的chat方法传入消息
        ChatResponse chatResponse = qwenChatModel.chat(userMessage);
        //取出ai返回的结果
        AiMessage aiMessage = chatResponse.aiMessage();

        log.info("AI输出：" + aiMessage.toString());
        return aiMessage.text();
    }
}
