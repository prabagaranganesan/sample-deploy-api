package com.example.sampledeploy.web;

import com.example.sampledeploy.dto.ProfileResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ProfileController {

    @GetMapping("/profile")
    public ProfileResponse profile() {
        Map<String, Object> preferences = new LinkedHashMap<>();
        preferences.put("theme", "light");
        preferences.put("notifications", true);

        return new ProfileResponse(
                "usr_demo_1",
                "demo@example.com",
                "Demo User",
                "free",
                Instant.parse("2026-01-01T00:00:00Z"),
                preferences
        );
    }
}
