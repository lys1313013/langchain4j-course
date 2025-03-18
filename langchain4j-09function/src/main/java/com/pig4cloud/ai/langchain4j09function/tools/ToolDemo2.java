package com.pig4cloud.ai.langchain4j09function.tools;

import dev.langchain4j.agent.tool.*;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.tool.DefaultToolExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 工具调用
 * 1. 创建工具WeatherUtil
 * 2. 带着工具问ai问题
 * 3. ai返回调用方法与参数
 * 4. 调用工具
 * 5. 将原本信息、工具调用结果给ai
 * 6. ai返回最终结果
 *
 * @author 013
 * @date 2025/3/18
 */
public class ToolDemo2 {

    static class WeatherUtil {
        /**
         * 工具的描述一定要清晰！
         */
        @Tool("获取某一个具体城市的天气")
        public String getWeather(@P("指定的城市") String city) {
            return "今天" + city + "的天气是晴天";
        }
    }

    public static void main(String[] args) {
        ChatLanguageModel chatModel = OpenAiChatModel.builder()
                .apiKey(System.getenv("DASHSCOPE_KEY"))
                .modelName("qwen-turbo")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();

        // 构建工具
        List<ToolSpecification> toolSpecifications = ToolSpecifications.toolSpecificationsFrom(WeatherUtil.class);

        // 构建对话消息
        List<ChatMessage> chatMessageList = new ArrayList<>();
        UserMessage userMessage = UserMessage.from("北京市的天气怎么样？");
        chatMessageList.add(userMessage);

        // 第一次与AI大模型交互，获取需要调用的工具
        AiMessage aiMessage = chatModel.generate(chatMessageList, toolSpecifications).content();
        System.out.println(aiMessage);

        // 本地执行工具方法
        List<ToolExecutionRequest> toolExecutionRequests = aiMessage.toolExecutionRequests();
        if (aiMessage.hasToolExecutionRequests()) {
            for (ToolExecutionRequest toolExecutionRequest : toolExecutionRequests) {
                String methodName = toolExecutionRequest.name();
                String arguments = toolExecutionRequest.arguments();
                System.out.println("调用的方式" + methodName);
                System.out.println("调用的参数" + arguments);
            }
        }

        chatMessageList.add(aiMessage);
        WeatherUtil weatherUtil = new WeatherUtil();

        // 将工具调用的方法与聊天消息一起传入AI大模型
        toolExecutionRequests.forEach(toolExecutionRequest -> {
            DefaultToolExecutor toolExecutor = new DefaultToolExecutor(weatherUtil, toolExecutionRequest);
            String result = toolExecutor.execute(toolExecutionRequest, UUID.randomUUID().toString());
            System.out.println("工具执行结果：" + result);
            ToolExecutionResultMessage toolExecutionResultMessage = ToolExecutionResultMessage.from(toolExecutionRequest, result);
            chatMessageList.add(toolExecutionResultMessage);

        });

        // 拿到本地工具执行的结果
        AiMessage finalResponse = chatModel.generate(chatMessageList).content();
        System.out.println("最终结果：" + finalResponse.text());
    }
}
