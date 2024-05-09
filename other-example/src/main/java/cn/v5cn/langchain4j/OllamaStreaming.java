package cn.v5cn.langchain4j;

import cn.v5cn.langchain4j.common.ToolBar;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.model.output.Response;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class OllamaStreaming {

    static StreamingChatLanguageModel model = OllamaStreamingChatModel
            .builder()
            .baseUrl(ToolBar.OLLAMA_BASE_URL)
            .modelName(ToolBar.OLLAMA_MODEL_NAME)
            .timeout(Duration.of(120, ChronoUnit.SECONDS))
            .build();

    public static void main(String[] args) {
        model.generate("你是谁？", new StreamingResponseHandler<AiMessage>() {
            @Override
            public void onNext(String token) {
                System.out.println("token: " + token);
            }

            @Override
            public void onError(Throwable error) {

            }

            @Override
            public void onComplete(Response<AiMessage> response) {
                System.out.println("response: " + response);
            }
        });
    }
}
