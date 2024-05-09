package cn.v5cn.langchain4j;

import cn.v5cn.langchain4j.common.ToolBar;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static dev.langchain4j.data.message.UserMessage.userMessage;

public class ChatMemoryOllama {
    static OllamaChatModel model = OllamaChatModel.builder()
            .baseUrl(ToolBar.OLLAMA_BASE_URL)
            .modelName(ToolBar.OLLAMA_MODEL_NAME)
            .timeout(Duration.of(120, ChronoUnit.SECONDS))
            .build();
    static ChatMemory memory = TokenWindowChatMemory.builder()
            .maxTokens(100, new OpenAiTokenizer())
            .build();

    public static void main(String[] args) {
        memory.add(userMessage("你好, 我叫张三丰"));
        AiMessage answer = model.generate(memory.messages()).content();

        System.out.println(answer.text());
        memory.add(answer);

        memory.add(userMessage("我叫什么名字？"));
        AiMessage answerWithName = model.generate(memory.messages()).content();

        System.out.println(answerWithName.text());
        memory.add(answerWithName);

    }
}
