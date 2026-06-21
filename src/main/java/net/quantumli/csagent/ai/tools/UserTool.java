package net.quantumli.csagent.ai.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.quantumli.csagent.ai.database.DatabaseManager;
import net.quantumli.csagent.ai.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class UserTool {

    @Autowired
    private DatabaseManager databaseManager;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 用于查询和管理用户信息，包括基本信息、账户状态、权限等
     *
     * @param userId 用户ID
     * @param action 操作类型：query（返回数据库信息，用于系统内部使用，不可用于用户请求）、
     *               info（返回用户详细信息，用于用户请求）、
     *               update（更新用户信息）
     * @return JSON格式的结果字符串
     */
    @Tool("用于查询和管理用户信息，包括基本信息、账户状态、权限等")
    public String userTool(
            @P(description = "用户id") String userId,
            @P(description = "操作类型：query（返回数据库信息）、info（返回用户详细信息）、update（更新用户信息）") String action) {

        try {
            if (userId == null || userId.trim().isEmpty()) {
                return "{\"error\":\"用户ID不能为空\"}";
            }

            User user = databaseManager.getUser(userId);
            if (user == null) {
                return String.format("{\"error\":\"用户 %s 不存在\"}", userId);
            }

            if ("query".equalsIgnoreCase(action)) {
                // 返回完整的数据库信息
                return objectMapper.writeValueAsString(user);
            } else if ("info".equalsIgnoreCase(action)) {
                // 返回用户详细信息（适合用户请求）
                Map<String, Object> info = new HashMap<>();
                info.put("用户名", user.getName());
                info.put("邮箱", user.getEmail());
                info.put("账户状态", "活跃".equals(user.getStatus()) ? "活跃" : "非活跃");
                info.put("注册时间", user.getRegistrationDate());
                info.put("订单数量", user.getOrdersCount());
                info.put("累计消费", String.format("¥%.2f", user.getTotalSpent()));

                return objectMapper.writeValueAsString(info);
            } else if ("update".equalsIgnoreCase(action)) {
                // 更新用户信息（模拟更新）
                User updatedUser = databaseManager.getUser(userId);
                if (updatedUser != null) {
                    updatedUser.setLastLogin("2024-01-15");
                    databaseManager.updateUser(updatedUser);
                    return String.format("{\"success\":\"用户 %s 的信息已更新\"}", userId);
                }
                return String.format("{\"error\":\"更新用户 %s 失败\"}", userId);
            } else {
                return String.format("{\"error\":\"不支持的操作类型: %s\"}", action);
            }
        } catch (Exception e) {
            log.error("处理用户工具时发生错误", e);
            return "{\"error\":\"系统内部错误\"}";
        }
    }
}