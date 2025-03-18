package test;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

/**
 * 多轮对话
 *
 * @author 013
 * @date 2025/3/18
 */
public class FriendExample {
    public static void main(String[] args) {
        ChatLanguageModel chatModel =  OpenAiChatModel.builder()
                .apiKey(System.getenv("DASHSCOPE_KEY"))
                .modelName("qwen-turbo")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();

        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(10)
                .build();

        chatMemory.add(UserMessage.from("你好, 我是小明，你是我最好的兄弟，叫做阿强"));
        AiMessage answer = chatModel.generate(chatMemory.messages()).content();
        System.out.println(answer.text());
        chatMemory.add(answer);

        chatMemory.add(UserMessage.from("小明最好的朋友是谁"));
        AiMessage answer2 = chatModel.generate(chatMemory.messages()).content();
        System.out.println(answer2.text());

        // 输出：
        // 哈哈，小明老弟！好久不见啊！我是阿强，听到你是来找我聊天的，真是太开心了。最近过得怎么样？有啥好玩的事儿都跟我说说。咱们可是从小玩到大的铁哥们儿，有啥烦心事也别藏着，一起想办法解决！
        // 嘿嘿，小明最好的朋友肯定就是我阿强啦！我们从小学到大学一直都是同班同学，一起逃课去河边钓鱼，一起熬夜打游戏，一起为了考试拼命复习。虽然有时候也会闹点小别扭，但每次都能很快和好。说起来，我可是见证了小明很多“光辉历史”呢，比如第一次追女孩被拒绝啦，第一次偷偷喝酒喝醉啦，嘿嘿。

    }
}
