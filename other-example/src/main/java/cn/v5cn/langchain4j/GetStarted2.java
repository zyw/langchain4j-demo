package cn.v5cn.langchain4j;

import cn.v5cn.langchain4j.common.ToolBar;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * 使用ChatMessage
 * @author ZYW
 */
public class GetStarted2 {

    static OpenAiChatModel model = OpenAiChatModel
            .builder()
            .baseUrl(ToolBar.OPENAI_API_URL)
            .apiKey(ToolBar.OPENAI_API_KEY)
            .modelName(ToolBar.OLLAMA_MODEL_NAME)
            .timeout(Duration.of(120, ChronoUnit.SECONDS))
            .maxTokens(120)
            .logRequests(true)
            .logResponses(true)
            .build();

    public static void main(String[] args) {

        // 创建一个ChatMessage
        ChatMessage chatMessage = new UserMessage("你好?");

        // 调用模型
        Response<AiMessage> response = model.generate(chatMessage);

        // 打印响应
//        System.out.println(response.content().type());
        System.out.println(response.content().text());
    }
}
