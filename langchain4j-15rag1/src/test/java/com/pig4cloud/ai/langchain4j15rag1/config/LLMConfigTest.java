package com.pig4cloud.ai.langchain4j15rag1.config;

import com.pig4cloud.ai.langchain4j15rag1.service.ChatAssistant;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@SpringBootTest
class LLMConfigTest {
    @Autowired
    InMemoryEmbeddingStore<TextSegment> embeddingStore;

    @Autowired
    ChatAssistant chatAssistant;

    @Test
    void testAdd() throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:测试向量.docx");
        Document document = FileSystemDocumentLoader.loadDocument(resource.getFile().toPath());
        EmbeddingStoreIngestor.ingest(document, embeddingStore);

        String chat = chatAssistant.chat("合同的金额是多少");
        System.out.println(chat);
    }

}
