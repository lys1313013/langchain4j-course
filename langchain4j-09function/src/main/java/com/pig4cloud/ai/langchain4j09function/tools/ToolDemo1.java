package com.pig4cloud.ai.langchain4j09function.tools;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Collections;

/**
 * 工具调用
 * 1. 创建工具dateUtil
 * 2. 带着工具问ai问题
 * 3. ai返回调用方法与参数
 * 4. 调用工具
 * 5. 将原本信息、工具调用结果给ai
 * 6. ai返回最终结果
 *
 * @author 013
 * @date 2025/3/18
 */
public class ToolDemo1 {

    @Tool("获取当前日期")
    public static String dateUtil() {
        return LocalDate.now().toString();
    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ChatLanguageModel chatModel =  OpenAiChatModel.builder()
                .apiKey(System.getenv("DASHSCOPE_KEY"))
                .modelName("qwen-turbo")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();

        ToolSpecification toolSpecification = ToolSpecifications.toolSpecificationFrom(ToolDemo1.class.getMethod("dateUtil"));
        UserMessage userMessage = UserMessage.from("今天是几月几号");

        AiMessage aiMessage = chatModel.generate(Collections.singletonList(userMessage), toolSpecification).content();
        System.out.println(aiMessage);

        if (aiMessage.hasToolExecutionRequests()) {
            for (ToolExecutionRequest toolExecutionRequest : aiMessage.toolExecutionRequests()) {
                String methodName = toolExecutionRequest.name();
                Method method = ToolDemo1.class.getMethod(methodName);
                String result = (String) method.invoke(null);
                System.out.println(result);

                ToolExecutionResultMessage toolExecutionResultMessage = ToolExecutionResultMessage.from(toolExecutionRequest.id(), toolExecutionRequest.name(), result);
                Response<AiMessage> generate = chatModel.generate(userMessage, aiMessage, toolExecutionResultMessage);
                System.out.println(generate.content().text());
            }
        }



    }
}
