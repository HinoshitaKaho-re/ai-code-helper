package com.example.aicodehelper.ai;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiCodeHelperServiceTest {

    @Resource
    private AiCodeHelperService aiCodeHelperService;

    @Test
    void chat() {
        String result = aiCodeHelperService.chat(1, "你好，我是程序员布朗尼");
        System.out.printf(result);
    }

    @Test
    void chatWithMemory() {
        String result = aiCodeHelperService.chat(1, "你好，我是程序员布朗尼");
        System.out.printf(result);
        //继续对话，验证会话记忆功能
        String followUpResult = aiCodeHelperService.chat(1, "你能记住我吗？");
        System.out.printf(followUpResult);
    }

    @Test
    void chatForReport() {
        AiCodeHelperService.Report result = aiCodeHelperService.chatForReport(2, "我是程序员布朗尼，请帮我写一份Java学习报告，内容包括：1. Java的核心特性；2. Java的应用场景；3. 学习Java的建议");
        System.out.printf(result.toString());
    }
}