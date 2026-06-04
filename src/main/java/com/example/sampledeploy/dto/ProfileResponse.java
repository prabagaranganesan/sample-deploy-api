package com.example.sampledeploy.dto;

import java.time.Instant;
import java.util.Map;

public record ProfileResponse(
        String id,
        String email,
        String displayName,
        String plan,
        Instant createdAt,
        Map<String, Object> preferences
) {}
