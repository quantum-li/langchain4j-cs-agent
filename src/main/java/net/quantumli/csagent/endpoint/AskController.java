package net.quantumli.csagent.endpoint;

import net.quantumli.csagent.ai.Agent;
import net.quantumli.csagent.endpoint.entity.AskRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AskController {

    Agent agent;

    public AskController(Agent agent){
        this.agent=agent;
    }

    @PostMapping("/ask")
    public String ask(@RequestBody AskRequest askRequest){
        return agent.chat(askRequest.getUserId(),askRequest.getMessage());
    }
}
