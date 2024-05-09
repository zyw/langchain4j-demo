package cn.v5cn.langchain4j;

import cn.v5cn.langchain4j.common.ToolBar;
import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.zhipu.ZhipuAiChatModel;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class ConversationalChainChatMemory {

//    static OllamaChatModel model = OllamaChatModel.builder()
//            .baseUrl("http://127.0.0.1:11434")
//            .modelName("terrence/openbuddy:8b")
//            .timeout(Duration.of(120, ChronoUnit.SECONDS))
//            .build();

    static ChatLanguageModel model = ZhipuAiChatModel
            .builder()
            .apiKey(ToolBar.ZHIPU_AI_KEY)
            .maxToken(1000)
            .logRequests(true)
            .logResponses(true)
            .build();

    public static void main(String[] args) {

        ConversationalChain chain = ConversationalChain
                .builder()
                .chatLanguageModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();

        String msg = chain.execute("我叫张三，今年30岁，是一名软件工程师。");

        System.out.println(msg);

        String msg1 = chain.execute("我叫什么名字？");
        System.out.println(msg1);

        String msg2 = chain.execute("我多大了？");
        System.out.println(msg2);

        String msg3 = chain.execute("我从事什么工作？");
        System.out.println(msg3);

    }
}
