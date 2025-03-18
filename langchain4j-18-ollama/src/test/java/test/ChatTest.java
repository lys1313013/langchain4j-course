package test;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 类功能描述
 *
 * @author 只有影子
 * @date 2025/3/17
 */
@SpringBootTest
public class ChatTest {
    @Autowired
    private ChatLanguageModel chatLanguageModel;

    @Test
    void chat() {
        String generate = chatLanguageModel.generate("你好");

        System.out.println(generate);
    }

    /**
     * 多次对话
     */
    @Test
    void mulChat() {
        UserMessage userMessage = UserMessage.from("你是谁");
        Response<AiMessage> response = chatLanguageModel.generate(userMessage);
        AiMessage aiMessage = response.content();
        System.out.println(aiMessage.text());

        UserMessage userMessage2 = UserMessage.from("请重复一次");

        Response<AiMessage> response2 = chatLanguageModel.generate(userMessage, aiMessage, userMessage2);
        System.out.println(response2.content().text());
    }
}
