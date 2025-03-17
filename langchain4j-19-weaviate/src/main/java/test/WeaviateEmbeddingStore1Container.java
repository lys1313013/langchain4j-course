package test;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.weaviate.WeaviateEmbeddingStore;
import io.weaviate.client.v1.data.replication.model.ConsistencyLevel;
import org.testcontainers.weaviate.WeaviateContainer;

import java.util.List;

public class WeaviateEmbeddingStore1Container {

    public static void main(String[] args) {
        // 启动一个Weaviate Docker容器 临时存储数据并查询
        // 填写Weaviate的docker镜像名称
        String dockerImageName = "semitechnologies/weaviate:1.19.0";
        try (WeaviateContainer weaviate = new WeaviateContainer(dockerImageName)) {
            weaviate.start();
            EmbeddingStore<TextSegment> embeddingStore = WeaviateEmbeddingStore.builder()
                    .scheme("http")
                    .host(weaviate.getHttpHostAddress())
                    // "Default" class is used if not specified. Must start from an uppercase letter!
                    .objectClass("Test")
                    // If true (default), then WeaviateEmbeddingStore will generate a hashed ID based on provided
                    // text segment, which avoids duplicated entries in DB. If false, then random ID will be generated.
                    .avoidDups(true)
                    .consistencyLevel(ConsistencyLevel.ALL)
                    .build();

            EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

            TextSegment segment1 = TextSegment.from("I like football.");
            Embedding embedding1 = embeddingModel.embed(segment1).content();
            embeddingStore.add(embedding1, segment1);

            TextSegment segment2 = TextSegment.from("The weather is good today.");
            Embedding embedding2 = embeddingModel.embed(segment2).content();
            embeddingStore.add(embedding2, segment2);

            Embedding queryEmbedding = embeddingModel.embed("What is your favourite sport?").content();
            List<EmbeddingMatch<TextSegment>> relevant = embeddingStore.findRelevant(queryEmbedding, 1);
            EmbeddingMatch<TextSegment> embeddingMatch = relevant.get(0);

            System.out.println(embeddingMatch.score()); // 0.8144288063049316
            System.out.println(embeddingMatch.embedded().text()); // I like football.
        }
    }
}
