package test;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.weaviate.WeaviateEmbeddingStore;
import io.weaviate.client.v1.data.replication.model.ConsistencyLevel;

public class WeaviateEmbeddingStore3Local {

    public static void main(String[] args) {
        // 连接本地启动的Weaviate
        EmbeddingStore<TextSegment> embeddingStore = WeaviateEmbeddingStore.builder()
                .scheme("http")
                .host("127.0.0.1")
                .port(38080)
                .objectClass("Test")
                .apiKey("test-secret-key")
                .avoidDups(true)
                .consistencyLevel(ConsistencyLevel.ALL)
                .build();

        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

        TextSegment segment1 = TextSegment.from("I like football");
        Embedding embedding1 = embeddingModel.embed(segment1).content();
        embeddingStore.add(embedding1, segment1);

        TextSegment segment2 = TextSegment.from("The weather is good today.");
        Embedding embedding2 = embeddingModel.embed(segment2).content();
        embeddingStore.add(embedding2, segment2);

        Embedding queryEmbedding = embeddingModel.embed("What is your favourite sport?").content();

        EmbeddingSearchRequest embeddingSearchRequest1 = EmbeddingSearchRequest
                .builder()
                .queryEmbedding(queryEmbedding)
                .build();
        EmbeddingSearchResult<TextSegment> embeddingSearchResult1 = embeddingStore.search(embeddingSearchRequest1);

        // 打印所有的结果
        embeddingSearchResult1.matches().forEach(System.out::println);

        EmbeddingMatch<TextSegment> textSegmentEmbeddingMatch = embeddingSearchResult1.matches().get(0);
        System.out.println(textSegmentEmbeddingMatch.embedded().text());
        System.out.println(textSegmentEmbeddingMatch.score());

    }
}
