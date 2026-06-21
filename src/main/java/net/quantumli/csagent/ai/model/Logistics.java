package net.quantumli.csagent.ai.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Logistics {
    private String trackingNumber;
    private List<Map<String, Object>> trackingDetails;
}