package com.example.aicodehelper.ai;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

//@AiService
public interface AiCodeHelperService {

    //该注解可以直接指定文件，将内容读取出来作为提示词
    @SystemMessage(fromResource = "system-prompt.txt")

    //加了 @MemoryId 之后，LangChain4j 对所有参数都要求明确注解（它需要知道每个参数的用途）。只有一个参数时它能猜到是用户消息，多个参数时就必须显式标注。
    String chat(@MemoryId int memoryId, @UserMessage String userMessage);
}
