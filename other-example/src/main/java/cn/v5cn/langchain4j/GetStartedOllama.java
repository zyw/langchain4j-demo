package cn.v5cn.langchain4j;

import cn.v5cn.langchain4j.common.ToolBar;
import dev.langchain4j.model.ollama.OllamaChatModel;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class GetStartedOllama {

    static OllamaChatModel model = OllamaChatModel.builder()
            .baseUrl(ToolBar.OLLAMA_BASE_URL)
            .modelName(ToolBar.OLLAMA_MODEL_NAME)
            .timeout(Duration.of(120, ChronoUnit.SECONDS))
            .build();

    public static void main(String[] args) {
        String msg = model.generate("你好，我是一个机器人");
        System.out.println(msg);
    }
}
