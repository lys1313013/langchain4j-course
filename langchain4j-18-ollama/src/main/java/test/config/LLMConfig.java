package test.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
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
                .modelName("deepseek-r1:32b")
                .baseUrl(System.getenv("OLLAMA_BASE_URL"))
                .build();
    }

}
