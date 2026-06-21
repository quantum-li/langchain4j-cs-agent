package net.quantumli.csagent.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Slf4j
public class ModelFactory {

    @Bean
    public ChatModel openAi(){
        return OpenAiChatModel.builder()
                .baseUrl("https://open.bigmodel.cn/api/paas/v4/")
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("glm-4.7-flash")
                .logger(log)
                .logRequests(true)
                .logResponses(true)
                .maxRetries(0)
                .build();
    }
}
