package net.quantumli.csagent.ai;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

public interface Agent {
    String chat(@MemoryId String memoryId, @UserMessage String message);
}
