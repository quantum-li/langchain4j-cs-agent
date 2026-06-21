package net.quantumli.csagent.ai.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String id;
    private String name;
    private String category;
    private double price;
    private int stock;
    private String description;
    private double rating;
    private int reviews;
    private List<String> tags;
}