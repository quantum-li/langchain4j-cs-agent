package net.quantumli.csagent.ai.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String orderId;
    private String customerId;
    private String status;
    private List<Map<String, Object>> items;
    private double totalAmount;
    private String shippingAddress;
    private String trackingNumber;
    private String orderDate;
    private String deliveryDate;
    private String estimatedDelivery;
    private String paymentMethod;
    private String cancelledAt;

    // 可选的物流信息
    private List<Map<String, Object>> logistics;

    public Order(String orderId, String customerId, String status, List<Map<String, Object>> items, double totalAmount, String shippingAddress, String trackingNumber, String orderDate, String deliveryDate, String estimatedDelivery, String paymentMethod) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.status = status;
        this.items = items;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.trackingNumber = trackingNumber;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.estimatedDelivery = estimatedDelivery;
        this.paymentMethod = paymentMethod;
    }
}