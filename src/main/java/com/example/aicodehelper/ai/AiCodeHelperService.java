package com.example.aicodehelper.ai;

import com.example.aicodehelper.ai.guardrail.SafeInputGuardrail;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.guardrail.InputGuardrails;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

import java.util.List;

//@AiService
@InputGuardrails(SafeInputGuardrail.class)
public interface AiCodeHelperService {

    //该注解可以直接指定文件，将内容读取出来作为提示词
    @SystemMessage(fromResource = "system-prompt.txt")
    //加了 @MemoryId 之后，LangChain4j 对所有参数都要求明确注解（它需要知道每个参数的用途）。只有一个参数时它能猜到是用户消息，多个参数时就必须显式标注。
    String chat(@MemoryId int memoryId, @UserMessage String userMessage);

    @SystemMessage(fromResource = "system-prompt.txt")
    Report chatForReport(@MemoryId int memoryId, @UserMessage String userMessage);

    //定义返回值类型：学习报告  -  定义了数据结构（相当于一个只有字段、没有方法的简单 Java 类）
    record Report(String name, List<String> suggestionList) {};

    //RAG返回封装后的结果
    @SystemMessage(fromResource = "system-prompt.txt")
    Result<String> chatWithRag(@MemoryId int memoryId, @UserMessage String userMessage);

    // 流式对话
    @SystemMessage(fromResource = "system-prompt.txt")
    Flux<String> chatStream(@MemoryId int memoryId, @UserMessage String userMessage);
}
