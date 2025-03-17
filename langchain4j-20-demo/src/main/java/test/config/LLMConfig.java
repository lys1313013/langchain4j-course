package test.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.weaviate.WeaviateEmbeddingStore;
import io.weaviate.client.v1.data.replication.model.ConsistencyLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类功能描述
 *
 * @author 只有影子
 * @date 2025/3/17
 */
@Configuration(proxyBeanMethods = false)
public class LLMConfig {
    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return OllamaChatModel.builder()
                .modelName("deepseek-r1:1.5b")
                .baseUrl(System.getenv("OLLAMA_BASE_URL"))
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return OllamaEmbeddingModel.builder()
                .modelName("bge-m3:latest")
                .baseUrl(System.getenv("OLLAMA_BASE_URL"))
                .build();
    }

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        return WeaviateEmbeddingStore.builder()
                .scheme("http")
                .host("127.0.0.1")
                .port(38080)
                .objectClass("Test2")
                .apiKey("test-secret-key")
                .avoidDups(true)
                .consistencyLevel(ConsistencyLevel.ALL)
                .build();
    }
}
