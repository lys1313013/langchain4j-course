package test;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * 类功能描述
 *
 * @author 林义山
 * @date 2025/3/17
 */
@SpringBootTest
public class TestDemo {

    @Autowired
    private ChatLanguageModel chatLanguageModel;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    private EmbeddingStore<TextSegment> embeddingStore;

    @Test
    public void test() {

        // 假设我们有一些文档需要存储
        List<TextSegment> documents = new ArrayList<>();
        documents.add(TextSegment.from("法国的首都是巴黎，它是一个充满浪漫气息的城市。"));
        documents.add(TextSegment.from("英国的首都是伦敦，它是一个历史悠久的城市。"));

        // 将文档嵌入并存储到 EmbeddingStore 中
        for (TextSegment document : documents) {
            Embedding embedding = embeddingModel.embed(document.text()).content();
            embeddingStore.add(embedding, document);
        }
        // 用户查询
        String userQuery = "法国的首都在哪里？";
        Embedding queryEmbedding = embeddingModel.embed(userQuery).content();

        EmbeddingSearchRequest embeddingSearchRequest1 = EmbeddingSearchRequest
                .builder()
                .queryEmbedding(queryEmbedding)
                .build();
        EmbeddingSearchResult<TextSegment> embeddingSearchResult1 = embeddingStore.search(embeddingSearchRequest1);

        // 提取检索到的相关信息
        StringBuilder context = new StringBuilder();
        for (EmbeddingMatch<TextSegment> match : embeddingSearchResult1.matches()) {
            context.append(match.embedded().text());
        }

        // 将检索结果作为提示词的一部分传递给大模型
        String prompt = "相关信息：" + context.toString() + "\n问题：" + userQuery;
        UserMessage userMessage = UserMessage.from(prompt);

        // 使用 ChatLanguageModel 生成回复
        ChatResponse response = chatLanguageModel.chat(userMessage);
        System.out.println("回答: " + response.aiMessage().text());
    }
}
