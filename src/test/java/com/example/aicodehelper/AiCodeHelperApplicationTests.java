package com.example.aicodehelper;

import com.example.aicodehelper.ai.AiCodeHelper;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AiCodeHelperApplicationTests {

    @Resource
    private AiCodeHelper aiCodeHelper;

    @Test
    void chat() {
        aiCodeHelper.chat("你好，我是程序员布朗尼");
    }

    @Test
    void chatWithMessage() {
        UserMessage userMessage = UserMessage.from(
                TextContent.from("描述图片"),
                ImageContent.from("https://ts3.tc.mm.bing.net/th/id/OIP-C.G6fkfQYRxcVmFwV_HX7bnQHaEH?r=0&rs=1&pid=ImgDetMain&o=7&rm=3")
        );
        aiCodeHelper.chatWithMessage(userMessage);
    }
}
