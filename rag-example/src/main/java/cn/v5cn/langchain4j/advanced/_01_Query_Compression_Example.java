package cn.v5cn.langchain4j.advanced;

import cn.v5cn.langchain4j.shared.Assistant;
import cn.v5cn.langchain4j.shared.ChatLanguageModelFactory;
import cn.v5cn.langchain4j.shared.ChatLanguageModelTypeEnum;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.bge.small.en.v15.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.transformer.CompressingQueryTransformer;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import static cn.v5cn.langchain4j.shared.Utils.startConversationWith;
import static cn.v5cn.langchain4j.shared.Utils.toPath;
import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

/**
 * 使用压缩查询的例子
 *
 * @author ZYW
 */
public class _01_Query_Compression_Example {
    public static void main(String[] args) {
        Assistant assistant = createAssistant("documents/biography-of-john-doe.txt");

        startConversationWith(assistant);
    }

    private static Assistant createAssistant(String documentPath) {
        // 1. 加载文档
        Document document = loadDocument(toPath(documentPath), new TextDocumentParser());
        // 1.1. 创建文档分割器
        DocumentSplitter splitter = DocumentSplitters.recursive(300, 0);
        // 2. 创建EmbeddingModel
        EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();
        // 3. 创建EmbeddingStore
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        // 4. 使用EmbeddingStoreIngestor(摄取器)串联"DocumentSplitter"、"EmbeddingModel"和"EmbeddingStore"。可以去NaiveRAGExample查看不使用EmbeddingStoreIngestor的例子
        // “Ingestor” 是一个术语，通常用于数据处理和摄取领域，指的是一个系统或组件，用于将数据从源系统中提取出来并加载到目标系统中。中文可以翻译为“摄取器”或“导入器”。
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor
                .builder()
                // 设置文档分割器
                .documentSplitter(splitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        // 摄取文档
        ingestor.ingest(document);
        // 5. 创建ChatModel
        ChatLanguageModel model = ChatLanguageModelFactory.create(ChatLanguageModelTypeEnum.ZHIPUAI);

        // TODO 例子的重要部分
        // 6. 创建压缩查询Transformer（一个利用ChatLanguageModel的QueryTransformer，能够将给定的查询以及聊天记忆（之前的对话历史）压缩成一个简洁的查询。这只适用于使用ChatMemory的情况。）
        QueryTransformer queryTransformer = new CompressingQueryTransformer(model);
        // 7. 创建一个内容检索器（ContentRetriever）
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever
                .builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .maxResults(2)
                .minScore(0.6)
                .build();

        // TODO 例子的重要部分
        // 8. 创建检索增强器（RetrievalAugmentor）
        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor
                .builder()
                .queryTransformer(queryTransformer)
                .contentRetriever(contentRetriever)
                .build();

        return AiServices.builder(Assistant.class)
                .chatLanguageModel(model)
                .retrievalAugmentor(retrievalAugmentor)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }
}
