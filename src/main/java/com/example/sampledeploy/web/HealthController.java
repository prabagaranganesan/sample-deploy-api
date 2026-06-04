package com.example.sampledeploy.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @Value("${app.git-sha:local-dev}")
    private String gitSha;

    @Value("${app.version:0.1.0}")
    private String version;

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", "ok");
        body.put("service", "sample-deploy-api");
        body.put("version", version);
        body.put("gitSha", gitSha);
        return body;
    }
}
