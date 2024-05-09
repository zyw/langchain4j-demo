package cn.v5cn.langchain4j.common;

/**
 * 工具类
 *
 * @author ZYW
 */
public interface ToolBar {

    /**
     * 智谱AI开放平台
     */
    String ZHIPU_AI_KEY = "";

    /**
     * OpenAI Key
     */
    String OPENAI_API_KEY = "sk-3vV35vq6k51XPxq301T3T3BlbkFJ7KY696yh3";
    /**
     * OpenAI API 接口地址
     */
    String OPENAI_API_URL = "http://127.0.0.1:11434/v1/";

    /**
     * Ollama API 接口地址
     */
    String OLLAMA_BASE_URL = "http://127.0.0.1:11434";
    /**
     * ollama 模型名称
     */
    String OLLAMA_MODEL_NAME = "terrence/openbuddy:8b";

}
