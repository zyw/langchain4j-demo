package cn.v5cn.langchain4j.naive;

import cn.v5cn.langchain4j.shared.Assistant;
import cn.v5cn.langchain4j.shared.ChatLanguageModelFactory;
import cn.v5cn.langchain4j.shared.ChatLanguageModelTypeEnum;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.bge.small.en.v15.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.util.List;

import static cn.v5cn.langchain4j.shared.Utils.startConversationWith;
import static cn.v5cn.langchain4j.shared.Utils.toPath;
import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

/**
 * 本地Naive RAG示例，使用低级API
 *
 * @author ZYW
 */
public class NaiveRAGExample {


    public static void main(String[] args) {
        Assistant assistant = createAssistant("documents/miles-of-smiles-terms-of-use.txt");

        startConversationWith(assistant);
    }

    private static Assistant createAssistant(String documentPath) {
        // 创建智谱AI模型调用
        ChatLanguageModel model = ChatLanguageModelFactory.create(ChatLanguageModelTypeEnum.ZHIPUAI);
        // 创建DocumentParser
        DocumentParser parser = new TextDocumentParser();
        // 加载 Document
        Document document = loadDocument(toPath(documentPath), parser);
        // 1. 创建Document分割器
        DocumentSplitter splitter = DocumentSplitters.recursive(300, 0);
        // 2. 文档分割
        List<TextSegment> segments = splitter.split(document);
        // 3. 创建EmbeddingModel
        EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();
        // 4. 使用EmbeddingModel嵌入文档
        List<Embedding> embeddings = embeddingModel.embedAll(segments).content();
        // 5. 创建Embedding存储器(内存存储器)
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        // 6. 存储Embeddings和segments
        embeddingStore.addAll(embeddings,segments);
        // 1~6可以使用EmbeddingStoreIngestor整合起来
//        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor
//                .builder()
//                .documentSplitter(splitter)
//                .embeddingModel(embeddingModel)
//                .embeddingStore(embeddingStore)
//                .build();
//        ingestor.ingest(document);

        // 创建内容检索器，内容检索器负责根据用户查询检索相关内容。
        EmbeddingStoreContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(2)
                .minScore(0.5)
                .build();

        // 创建ChatMemory
        ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);


        // 创建Assistant
        return AiServices.builder(Assistant.class)
                .chatLanguageModel(model)
                .contentRetriever(contentRetriever)
                .chatMemory(memory)
                .build();
    }
}
