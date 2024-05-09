package cn.v5cn.langchain4j.aiservice;

import cn.v5cn.langchain4j.common.ToolBar;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.zhipu.ZhipuAiChatModel;
import dev.langchain4j.service.AiServices;

/**
 * @author: ZYW
 */
public class SimplestAIService {
    static ChatLanguageModel model = ZhipuAiChatModel
            .builder()
            .apiKey(ToolBar.ZHIPU_AI_KEY)
            .maxToken(1000)
            .logRequests(true)
            .logResponses(true)
            .build();

    public static void main(String[] args) {
        Assistant assistant = AiServices.create(Assistant.class, model);
        String msg = assistant.chat("我肚子疼");

        System.out.println(msg);
    }
}
