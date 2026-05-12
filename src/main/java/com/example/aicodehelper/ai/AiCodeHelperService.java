package com.example.aicodehelper.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

//@AiService
public interface AiCodeHelperService {

    //该注解可以直接指定文件，将内容读取出来作为提示词
    @SystemMessage(fromResource = "system-prompt.txt")
    String chat(String userMessage);
}
