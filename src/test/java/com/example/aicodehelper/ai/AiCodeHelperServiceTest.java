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
}