package cn.v5cn.langchain4j.advanced;

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
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.bge.small.en.v15.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.router.LanguageModelQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.v5cn.langchain4j.shared.Utils.startConversationWith;
import static cn.v5cn.langchain4j.shared.Utils.toPath;
import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

/**
 *
 *
 * @author ZYW
 */
public class _02_Query_Routing_Example {
    public static void main(String[] args) {
        Assistant assistant = createAssistant();

        startConversationWith(assistant);
    }

    private static Assistant createAssistant() {
        // 创建一个EmbeddingModel
        EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();

        // 创建一个biographyEmbeddingStore
        EmbeddingStore<TextSegment> biographyEmbeddingStore = embed(toPath("documents/biography-of-john-doe.txt"), embeddingModel);
        // 创建一个内容检索器
        ContentRetriever biographyContentRetriever = EmbeddingStoreContentRetriever
                .builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(biographyEmbeddingStore)
                .maxResults(2)
                .minScore(0.6)
                .build();

        // 创建一个termsOfUseEmbeddingStore
        EmbeddingStore<TextSegment> termsOfUseEmbeddingStore =
                embed(toPath("documents/miles-of-smiles-terms-of-use.txt"), embeddingModel);
        ContentRetriever termsOfUseContentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(termsOfUseEmbeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(2)
                .minScore(0.6)
                .build();

        ChatLanguageModel model = ChatLanguageModelFactory.create(ChatLanguageModelTypeEnum.ZHIPUAI);

        // TODO 例子的重要部分
        // 让我们创建一个查询路由器。创建多个内容检索器，使用Map组织多个内容检索器，使用Map value描述内容检索器
        Map<ContentRetriever, String> retrieverToDescription = new HashMap<>();
        retrieverToDescription.put(biographyContentRetriever, "约翰·多伊的传记");
        retrieverToDescription.put(termsOfUseContentRetriever, "汽车租赁公司的使用条款");
        QueryRouter queryRouter = new LanguageModelQueryRouter(model, retrieverToDescription);


        // TODO 例子的重要部分
        // 创建一个查询增强器
        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .queryRouter(queryRouter)
                .build();

        // 创建一个Assistant
        return AiServices.builder(Assistant.class)
                .chatLanguageModel(model)
                .retrievalAugmentor(retrievalAugmentor)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(30))
                .build();
    }

    private static EmbeddingStore<TextSegment> embed(Path documentPath, EmbeddingModel embeddingModel) {
        // 创建文档解析器
        DocumentParser documentParser = new TextDocumentParser();
        // 加载文档
        Document document = loadDocument(documentPath, documentParser);
        // 创建文档分割器
        DocumentSplitter splitter = DocumentSplitters.recursive(300, 0);
        // 分割文档 得到文本片段
        List<TextSegment> segments = splitter.split(document);
        // 通过EmbeddingModel获得Embedding
        List<Embedding> embeddings = embeddingModel.embedAll(segments).content();

        // 创建一个InMemoryEmbeddingStore
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        // 将文本片段和对应的Embedding存储到InMemoryEmbeddingStore中
        embeddingStore.addAll(embeddings, segments);

        // 返回EmbeddingStore
        return embeddingStore;
    }
}
