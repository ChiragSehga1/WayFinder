package com.example.locationtracker.backendClient;

import application.userController.dto.LoginRequest;
import org.springframework.web.client.RestTemplate;

public class backendClient {

    private final RestTemplate rest = new RestTemplate();
    private final String baseUrl = "http://localhost:8067/api";

    public String login(String username, String password) {
        LoginRequest req = new LoginRequest(username, password);
        return rest.postForObject(baseUrl + "/login", req, String.class);
    }
}
