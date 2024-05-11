package cn.v5cn.langchain4j.shared;

/**
 * 根据LLM的类型，创建对于的ChatLanguageModel
 *
 * @author ZYW
 */

public enum ChatLanguageModelTypeEnum {
    // 智谱
    ZHIPUAI,
    // azure open ai
    AZURE,
    // open ai
    OPENAI,
    // ollama
    OLLAMA,
    // 千帆
    QIANFAN,
    // LocalAi
    LOCALAI,
    //kimi
    KIMI,
    // 通义千问
    QWEN,
    // 讯飞
    XUNFEI,
}
