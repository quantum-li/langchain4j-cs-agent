package net.quantumli.csagent.ai.database;

import net.quantumli.csagent.ai.model.User;
import net.quantumli.csagent.ai.model.Order;
import net.quantumli.csagent.ai.model.Logistics;
import net.quantumli.csagent.ai.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DatabaseManager {

    // 用户数据库
    private static final Map<String, User> userDb = new ConcurrentHashMap<>();

    // 订单数据库
    private static final Map<String, Order> orderDb = new ConcurrentHashMap<>();

    // 物流数据库
    private static final Map<String, List<Map<String, Object>>> logisticsDb = new ConcurrentHashMap<>();

    // 商品数据库
    private static final List<Product> productDb = new ArrayList<>();

    static {
        // 初始化用户数据
        initializeUsers();

        // 初始化订单数据
        initializeOrders();

        // 初始化物流数据
        initializeLogistics();

        // 初始化商品数据
        initializeProducts();
    }

    private static void initializeUsers() {
        Map<String, Object> preferences1 = new HashMap<>();
        preferences1.put("language", "zh");
        preferences1.put("theme", "light");
        preferences1.put("notifications", true);

        User user1 = new User("user_001", "张三", "zhangsan@example.com",
                           "13800138000", "active", "2023-01-15",
                           "2024-01-10", preferences1, 15, 5999.99);

        Map<String, Object> preferences2 = new HashMap<>();
        preferences2.put("language", "zh");
        preferences2.put("theme", "dark");
        preferences2.put("notifications", false);

        User user2 = new User("user_002", "李四", "lisi@example.com",
                           "13900139000", "inactive", "2023-03-20",
                           "2023-12-01", preferences2, 8, 3299.99);

        userDb.put("user_001", user1);
        userDb.put("user_002", user2);
    }

    private static void initializeOrders() {
        // 订单1001
        List<Map<String, Object>> items1 = new ArrayList<>();
        items1.add(createItemMap("笔记本电脑", 4999.99, 1));
        items1.add(createItemMap("无线鼠标", 99.99, 1));

        Order order1 = new Order("order_1001", "user_001", "delivered", items1,
                              5099.98, "北京市朝阳区xxx街道123号", "SF1234567890",
                              "2023-12-01", "2023-12-05", null, "支付宝");

        // 订单1002
        List<Map<String, Object>> items2 = new ArrayList<>();
        items2.add(createItemMap("智能手机", 3299.99, 1));

        Order order2 = new Order("order_1002", "user_002", "shipped", items2,
                              3299.99, "上海市浦东新区xxx路456号", "YT9876543210",
                              "2024-01-08", null, "2024-01-12", "微信支付");

        orderDb.put("order_1001", order1);
        orderDb.put("order_1002", order2);
    }

    private static void initializeLogistics() {
        // SF1234567890的物流信息
        List<Map<String, Object>> sfLogistics = new ArrayList<>();
        sfLogistics.add(createTrackingDetail("2023-12-05 14:30", "派送完成", "北京市朝阳区"));
        sfLogistics.add(createTrackingDetail("2023-12-05 10:15", "正在派送", "朝阳区配送站"));
        sfLogistics.add(createTrackingDetail("2023-12-05 08:00", "到达配送站", "北京分拣中心"));
        sfLogistics.add(createTrackingDetail("2023-12-04 20:00", "运输中", "上海转运中心"));
        sfLogistics.add(createTrackingDetail("2023-12-04 12:00", "已发货", "上海仓库"));

        // YT9876543210的物流信息
        List<Map<String, Object>> ytLogistics = new ArrayList<>();
        ytLogistics.add(createTrackingDetail("2024-01-10 09:00", "运输中", "上海转运中心"));
        ytLogistics.add(createTrackingDetail("2024-01-09 18:00", "已发货", "上海仓库"));

        logisticsDb.put("SF1234567890", sfLogistics);
        logisticsDb.put("YT9876543210", ytLogistics);
    }

    private static void initializeProducts() {
        productDb.add(new Product("p001", "笔记本电脑 Pro", "电子产品",
                               5999.99, 50,
                               "高性能办公笔记本电脑，搭载最新处理器",
                               4.8, 1234,
                               Arrays.asList("办公", "高性能", "轻薄")));

        productDb.add(new Product("p002", "智能手机 X", "电子产品",
                               3999.99, 100,
                               "旗舰智能手机，拍照效果出众",
                               4.6, 2341,
                               Arrays.asList("拍照", "旗舰", "5G")));

        productDb.add(new Product("p003", "无线蓝牙耳机", "音频设备",
                               299.99, 200,
                               "高品质无线耳机，降噪效果出色",
                               4.5, 567,
                               Arrays.asList("无线", "降噪", "蓝牙")));

        productDb.add(new Product("p004", "智能手表", "可穿戴设备",
                               1299.99, 80,
                               "多功能智能手表，健康监测",
                               4.4, 890,
                               Arrays.asList("智能", "健康", "运动")));

        productDb.add(new Product("p005", "平板电脑", "电子产品",
                               2599.99, 30,
                               "轻薄便携平板电脑，娱乐办公两用",
                               4.3, 445,
                               Arrays.asList("便携", "娱乐", "办公")));
    }

    private static Map<String, Object> createItemMap(String name, double price, int quantity) {
        Map<String, Object> item = new HashMap<>();
        item.put("name", name);
        item.put("price", price);
        item.put("quantity", quantity);
        return item;
    }

    private static Map<String, Object> createTrackingDetail(String time, String status, String location) {
        Map<String, Object> detail = new HashMap<>();
        detail.put("time", time);
        detail.put("status", status);
        detail.put("location", location);
        return detail;
    }

    // 用户数据库操作方法
    public User getUser(String userId) {
        return userDb.get(userId);
    }

    public Map<String, User> getAllUsers() {
        return new HashMap<>(userDb);
    }

    public boolean updateUser(User user) {
        if (userDb.containsKey(user.getId())) {
            userDb.put(user.getId(), user);
            return true;
        }
        return false;
    }

    // 订单数据库操作方法
    public Order getOrder(String orderId) {
        return orderDb.get(orderId);
    }

    public Map<String, Order> getAllOrders() {
        return new HashMap<>(orderDb);
    }

    public boolean updateOrder(Order order) {
        if (orderDb.containsKey(order.getOrderId())) {
            orderDb.put(order.getOrderId(), order);
            return true;
        }
        return false;
    }

    // 物流数据库操作方法
    public List<Map<String, Object>> getLogistics(String trackingNumber) {
        return logisticsDb.get(trackingNumber);
    }

    // 商品数据库操作方法
    public List<Product> getAllProducts() {
        return new ArrayList<>(productDb);
    }

    public List<Product> searchProducts(String query, String category) {
        List<Product> results = new ArrayList<>(productDb);

        // 按分类筛选
        if (category != null && !category.trim().isEmpty()) {
            results.removeIf(p -> !category.equals(p.getCategory()));
        }

        // 根据查询词过滤
        if (query != null && !query.trim().isEmpty()) {
            String queryLower = query.toLowerCase();
            results.removeIf(p -> {
                boolean match = queryLower.contains(p.getName().toLowerCase()) ||
                               queryLower.contains(p.getDescription().toLowerCase()) ||
                               p.getTags().stream().anyMatch(tag ->
                                   tag.toLowerCase().contains(queryLower));
                return !match;
            });
        }

        // 按评分排序
        results.sort((p1, p2) -> Double.compare(p2.getRating(), p1.getRating()));

        return results;
    }
}