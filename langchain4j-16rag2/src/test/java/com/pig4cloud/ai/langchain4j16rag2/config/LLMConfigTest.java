package com.pig4cloud.ai.langchain4j16rag2.config;

import com.pig4cloud.ai.langchain4j16rag2.service.ChatAssistant;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.poi.ApachePoiDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByCharacterSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@SpringBootTest
class LLMConfigTest {

    @Autowired
    InMemoryEmbeddingStore<TextSegment> embeddingStore;

    @Autowired
    ChatAssistant chatAssistant;

    @Test
    void testAdd() throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:test.docx");
        Document document = FileSystemDocumentLoader.loadDocument(resource.getFile().toPath());
        EmbeddingStoreIngestor.ingest(document, embeddingStore);


        String chat = chatAssistant.chat("合同的金额是多少");
        System.out.println(chat);
    }

    @Test
    void test1() throws IOException {

        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:test.docx");
        Path path = resource.getFile().toPath();
        String abSolutePath = path.toAbsolutePath().toString();
        FileInputStream fileInputStream = new FileInputStream(abSolutePath);
        Document document = new ApachePoiDocumentParser().parse(fileInputStream);

        System.out.println(document);
    }

    @Test
    void test2() throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:test.docx");
        Path path = resource.getFile().toPath();
        String abSolutePath = path.toAbsolutePath().toString();
        FileInputStream fileInputStream = new FileInputStream(abSolutePath);
        Document document = new ApachePoiDocumentParser().parse(fileInputStream);

        List<TextSegment> split = new DocumentByCharacterSplitter(100, 0).split(document);

        System.out.println(split);

    }


}
