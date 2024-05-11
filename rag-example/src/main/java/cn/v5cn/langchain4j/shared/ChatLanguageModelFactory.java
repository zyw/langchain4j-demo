package cn.v5cn.langchain4j.shared;

import cn.v5cn.langchain4j.common.ToolBar;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.zhipu.ZhipuAiChatModel;

/**
 * ChatModel 工厂类
 *
 * @author ZYW
 */
public class ChatLanguageModelFactory {


    public static ChatLanguageModel create(ChatLanguageModelTypeEnum modelTypeEnum) {
        ChatLanguageModel model = null;
        switch (modelTypeEnum){
            case OPENAI:
                model = OpenAiChatModel.withApiKey(ToolBar.OPENAI_API_KEY);
                break;
            case ZHIPUAI:
                model = ZhipuAiChatModel
                        .builder()
                        .apiKey(ToolBar.ZHIPU_AI_KEY)
                        .logRequests(true)
                        .logResponses(true)
                        .maxToken(1000)
                        .build();
                break;
            case OLLAMA:
            case KIMI:
            case QWEN:
            case AZURE:
            case LOCALAI:
            case QIANFAN:
            case XUNFEI:
            default:
                model = OllamaChatModel
                        .builder()
                        .modelName(ToolBar.OLLAMA_MODEL_NAME)
                        .baseUrl(ToolBar.OLLAMA_BASE_URL)
                        .maxRetries(3)
                        .build();
        }
        return model;
    }
}
