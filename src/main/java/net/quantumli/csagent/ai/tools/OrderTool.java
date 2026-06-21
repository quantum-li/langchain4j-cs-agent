package net.quantumli.csagent.ai.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.quantumli.csagent.ai.database.DatabaseManager;
import net.quantumli.csagent.ai.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OrderTool {

    @Autowired
    private DatabaseManager databaseManager;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Tool("用于查询用户下有哪些订单。如果不确定用户id，需要返回查询失败，让用户登录")
    public List<String> orderSearchByUser(@P(description = "用于查询用户订单的用户id") String userId){
        Map<String,Order> orderMap = databaseManager.getAllOrders();
        return orderMap.values().stream().filter(o->o.getCustomerId().equals(userId)).map(Order::getOrderId).collect(Collectors.toList());
    }

    /**
     * 用于查询和管理订单信息，包括订单状态、物流信息、订单操作等
     *
     * @param orderId 订单ID
     * @param action 操作类型：query（查询订单信息）、cancel（取消订单）、refund（退款）
     * @return JSON格式的结果字符串
     */
    @Tool("用于查询和管理订单信息，包括订单状态、物流信息、订单操作等。如果不确定订单id，需要先根据用户id查询用户下的所有订单id。")
    public String orderTool(
            @P(description = "订单id，") String orderId,
            @P("操作类型：query（查询订单信息）、cancel（取消订单）、refund（退款）") String action) {

        try {
            if (orderId == null || orderId.trim().isEmpty()) {
                return "{\"error\":\"订单ID不能为空\"}";
            }

            Order order = databaseManager.getOrder(orderId);
            if (order == null) {
                return String.format("{\"error\":\"订单 %s 不存在\"}", orderId);
            }

            if ("query".equalsIgnoreCase(action)) {
                // 查询订单信息，包括物流信息
                Order orderWithLogistics = addLogisticsInfo(order);
                return objectMapper.writeValueAsString(orderWithLogistics);
            } else if ("cancel".equalsIgnoreCase(action)) {
                // 取消订单
                if (!canCancelOrder(order)) {
                    return String.format("{\"error\":\"订单 %s 状态为 %s，无法取消\"}",
                                       orderId, order.getStatus());
                }

                order.setStatus("cancelled");
                order.setCancelledAt("2024-01-15");
                databaseManager.updateOrder(order);

                return String.format("{\"success\":\"订单 %s 已取消\"}", orderId);
            } else if ("refund".equalsIgnoreCase(action)) {
                // 退款
                if (!canRefundOrder(order)) {
                    return String.format("{\"error\":\"订单 %s 状态为 %s，无法退款\"}",
                                       orderId, order.getStatus());
                }

                Map<String, Object> refund = new HashMap<>();
                refund.put("order_id", orderId);
                refund.put("refund_amount", order.getTotalAmount());
                refund.put("refund_method", order.getPaymentMethod());
                refund.put("status", "processing");
                refund.put("estimated_days", "3-5个工作日");

                return objectMapper.writeValueAsString(refund);
            } else {
                return String.format("{\"error\":\"不支持的操作类型: %s\"}", action);
            }
        } catch (Exception e) {
            log.error("处理订单工具时发生错误", e);
            return "{\"error\":\"系统内部错误\"}";
        }
    }

    private Order addLogisticsInfo(Order order) {
        List<Map<String, Object>> logistics = databaseManager.getLogistics(order.getTrackingNumber());
        if (logistics != null && !logistics.isEmpty()) {
            order.setLogistics(logistics);
            return order;
        }
        return order;
    }

    private boolean canCancelOrder(Order order) {
        return "pending".equals(order.getStatus()) || "processing".equals(order.getStatus());
    }

    private boolean canRefundOrder(Order order) {
        return "delivered".equals(order.getStatus());
    }
}