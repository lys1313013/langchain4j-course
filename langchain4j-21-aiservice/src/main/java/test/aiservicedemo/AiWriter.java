package test.aiservicedemo;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * 类功能描述
 *
 * @author hurry
 * @date 2025/3/18
 */
public class AiWriter {

    interface Write {
        @SystemMessage("你是一个散文作家，根据输入的题目写一篇200字以内的散文")
        String write(String content);
    }
    interface Write2 {
        @SystemMessage("你是一个散文作家，写一篇散文，题目是{{title}} 根据输入的题目写一篇{{count}}字以内的散文")
        String write(@UserMessage String message, @V("title") String title, @V("count") Long count);
    }

    public static void main(String[] args) {
        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey(System.getenv("DASHSCOPE_KEY"))
                .modelName("qwen-turbo")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();

        Write write = AiServices.create(Write.class, model);
        String content = write.write("我最感谢的人");
        System.out.println(content);

        Write2 write2 = AiServices.create(Write2.class, model);
        String content2 = write2.write("写一篇论文", "最爱看的电影", 200L);
        System.out.println(content2);
    }
}
