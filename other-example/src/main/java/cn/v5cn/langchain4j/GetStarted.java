package cn.v5cn.langchain4j;

import cn.v5cn.langchain4j.common.ToolBar;
import dev.langchain4j.model.openai.OpenAiChatModel;

/**
 * 简单示例
 * @author ZYW
 */
public class GetStarted {
    static OpenAiChatModel model = OpenAiChatModel
            .builder()
            .apiKey(ToolBar.OPENAI_API_KEY)
            .baseUrl(ToolBar.OPENAI_API_URL)
            .modelName(ToolBar.OLLAMA_MODEL_NAME)
            .build();

    public static void main(String[] args) {
        String msg = model.generate("你能干什么？");

        System.out.println(msg);
    }
}
