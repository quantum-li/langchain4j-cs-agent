package net.quantumli.csagent.endpoint.entity;

import lombok.Data;

@Data
public class AskRequest {
    String userId;
    String message;
}
