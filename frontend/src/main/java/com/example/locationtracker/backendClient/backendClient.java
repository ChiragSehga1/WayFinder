package com.example.locationtracker.backendClient;

import application.userController.dto.LoginRequest;
import org.bson.Document;
import org.springframework.web.client.RestTemplate;


import application.userController.dto.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class backendClient {

    private final RestTemplate rest = new RestTemplate();
    private final String baseUrl = "http://localhost:8067/api";

    // ========== AUTH ==========
    public String login(String username, String password) {
        LoginRequest req = new LoginRequest(username, password);
        return rest.postForObject(baseUrl + "/login", req, String.class);
    }

    public String signup(String username, String name, String contact, String lastLocation, String password) {
        SignupRequest req = new SignupRequest(username, name, contact, lastLocation, password);
        return rest.postForObject(baseUrl + "/signup", req, String.class);
    }

    public String deleteAccount(String username) {
        UsernameRequest req = new UsernameRequest(username);
        return rest.postForObject(baseUrl + "/deleteAccount", req, String.class);
    }

    // ========== FRIEND REQUESTS ==========

    public String addRequest(String sender, String receiver) {
        FriendRequest req = new FriendRequest(sender, receiver);
        return rest.postForObject(baseUrl + "/addRequest", req, String.class);
    }

    public String removeRequest(String sender, String receiver) {
        FriendRequest req = new FriendRequest(sender, receiver);
        return rest.postForObject(baseUrl + "/removeRequest", req, String.class);
    }

    public String addFriend(String sender, String receiver) {
        FriendRequest req = new FriendRequest(sender, receiver);
        return rest.postForObject(baseUrl + "/addFriend", req, String.class);
    }

    public String removeFriend(String username1, String username2) {
        FriendPair req = new FriendPair(username1, username2);
        return rest.postForObject(baseUrl + "/removeFriend", req, String.class);
    }

    public List<String> getPendingRequests(String username) {
        UsernameRequest req = new UsernameRequest(username);

        String[] result = rest.postForObject(baseUrl + "/getPendingRequests", req, String[].class);
        return result == null ? Collections.emptyList() : Arrays.asList(result);
    }

    public List<String> getFriends(String username) {
        UsernameRequest req = new UsernameRequest(username);

        String[] result = rest.postForObject(baseUrl + "/getFriends", req, String[].class);
        return result == null ? Collections.emptyList() : Arrays.asList(result);
    }

    // ========== LOCATION ==========
    public String getLocation(String username) {
        LocationRequest req = new LocationRequest(username, null);
        return rest.postForObject(baseUrl + "/getLocation", req, String.class);
    }

    public String updateLocation(String username, String location) {
        LocationRequest req = new LocationRequest(username, location);
        return rest.postForObject(baseUrl + "/updateLocation", req, String.class);
    }

    // ========== USER DETAILS ==========
    public Document getUserDetails(String username) {
        UsernameRequest req = new UsernameRequest(username);
        return rest.postForObject(baseUrl + "/getUserDetails", req, Document.class);
    }
}

