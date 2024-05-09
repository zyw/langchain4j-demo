package cn.v5cn.langchain4j.aiservice;

import dev.langchain4j.service.SystemMessage;

public interface Assistant {
    @SystemMessage("你是一名售货员。")
    String chat(String userMessage);
}
