package net.quantumli.csagent.ai;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import net.quantumli.csagent.ai.tools.OrderTool;
import net.quantumli.csagent.ai.tools.ProductSearchTool;
import net.quantumli.csagent.ai.tools.UserTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgentFactory {

    @Autowired
    ChatModel model;
    @Autowired
    OrderTool orderTool;
    @Autowired
    ProductSearchTool productSearchTool;
    @Autowired
    UserTool userTool;

    String SYSTEM_MESSAGE= """
            你是一个专业的智能客服助手。你需要帮助用户解决问题，并提供准确、有用的信息。
            
            你的职责包括：
            1. 回答用户的问题和咨询
            2. 查询用户信息、订单状态和商品信息
            3. 提供售后服务和建议
            4. 使用知识库提供准确的答案
            
            使用工具的规则：
            - 当用户询问用户信息时，使用user_query工具
            - 当用户询问订单状态或进行订单操作时，使用order_management工具
            - 当用户搜索商品或询问产品信息时，使用product_search工具
            - 当用户询问一般性知识或FAQ时，使用knowledge_search工具
            - 对于复杂的查询，可以组合使用多个工具
            
            回复风格：
            - 友好、专业、耐心
            - 使用清晰、简洁的语言
            - 提供具体的、可操作的建议
            - 如果无法满足用户需求，礼貌地说明并建议其他解决方案
            
            记住，你的目标是帮助用户解决问题，提升用户体验。
            """;


    @Bean
    public Agent agent(){
        return  AiServices.builder(Agent.class)
                .chatModel(model)
                .tools(orderTool,productSearchTool,userTool)
                .systemMessage(SYSTEM_MESSAGE)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }
}
