package com.flight.management.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RenderKeepAliveService {

    private static final Logger logger = LoggerFactory.getLogger(RenderKeepAliveService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    // Ping every 14 minutes (14 * 60 * 1000 ms)
    @Scheduled(fixedRate = 840000)
    public void keepAlive() {
        String url = System.getenv("RENDER_EXTERNAL_URL");
        if (url == null || url.isEmpty()) {
            url = "https://jetwayz-backend.onrender.com";
        }

        String pingUrl = url + "/api/keep-alive";

        try {
            logger.info("Sending keep-alive ping to: {}", pingUrl);
            String response = restTemplate.getForObject(pingUrl, String.class);
            logger.info("Keep-alive ping successful: {}", response);
        } catch (Exception e) {
            logger.error("Keep-alive ping failed for URL {}: {}", pingUrl, e.getMessage());
        }
    }
}
