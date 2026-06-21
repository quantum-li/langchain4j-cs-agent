package net.quantumli.csagent.ai.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.quantumli.csagent.ai.database.DatabaseManager;
import net.quantumli.csagent.ai.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProductSearchTool {

    @Autowired
    private DatabaseManager databaseManager;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 用于搜索和查询商品信息，支持分类筛选、价格范围等
     *
     * @param query 搜索查询
     * @param category 可选，商品分类
     * @param limit 返回结果数量限制
     * @return JSON格式的搜索结果
     */
    @Tool("用于搜索和查询商品信息，支持分类筛选、价格范围等")
    public String productSearchTool(
            @P("搜索查询") String query,
            @P(required = false, description = "可选，商品分类") String category,
            @P(required = false, defaultValue = "5", description = "返回结果数量限制,不指定默认5") Integer limit) {

        try {
            if (query == null || query.trim().isEmpty()) {
                return "{\"error\":\"查询词不能为空\"}";
            }

            // 执行搜索
            List<Product> results = databaseManager.searchProducts(query, category);

            // 限制返回数量
            int resultLimit = limit != null ? Math.max(1, limit) : 5;
            List<Product> limitedResults = results.stream()
                    .limit(resultLimit)
                    .collect(Collectors.toList());

            // 构建返回结果
            Map<String, Object> searchResult = Map.of(
                    "query", query,
                    "category", category != null ? category : "",
                    "results", limitedResults,
                    "total", limitedResults.size()
            );

            return objectMapper.writeValueAsString(searchResult);
        } catch (Exception e) {
            log.error("处理商品搜索工具时发生错误", e);
            return "{\"error\":\"系统内部错误\"}";
        }
    }
}