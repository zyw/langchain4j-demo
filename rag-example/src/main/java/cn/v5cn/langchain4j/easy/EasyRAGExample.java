package cn.v5cn.langchain4j.easy;

import cn.v5cn.langchain4j.common.ToolBar;
import cn.v5cn.langchain4j.shared.Assistant;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.zhipu.ZhipuAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.util.List;

import static cn.v5cn.langchain4j.shared.Utils.*;
import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocuments;

/**
 * 使用 LangChain4J 实现基于 RAG 的问答系统
 *
 * @author ZYW
 */
public class EasyRAGExample {
    public static void main(String[] args) {
        List<Document> documents = loadDocuments(toPath("documents/"), glob("*.txt"));

        ZhipuAiChatModel model = ZhipuAiChatModel.builder()
                .apiKey(ToolBar.ZHIPU_AI_KEY)
                .logResponses(true)
                .logRequests(true)
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(createContentRetriever(documents))
                .build();

        startConversationWith(assistant);
    }

    private static ContentRetriever createContentRetriever(List<Document> documents) {
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        EmbeddingStoreIngestor.ingest(documents, embeddingStore);
        // 创建内容检索器
        return EmbeddingStoreContentRetriever.from(embeddingStore);
    }
}
