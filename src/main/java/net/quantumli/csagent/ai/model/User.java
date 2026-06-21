package net.quantumli.csagent.ai.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String status;
    private String registrationDate;
    private String lastLogin;
    private Map<String, Object> preferences;
    private int ordersCount;
    private double totalSpent;
}