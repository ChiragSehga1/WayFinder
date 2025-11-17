package com.example.locationtracker.views;

import application.BackendApplication;
import application.service.connection;
import com.example.locationtracker.backendClient.backendClient;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.component.UI;

import com.vaadin.flow.component.UI;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import com.vaadin.flow.component.html.H1;


import org.json.JSONObject;

@Route("")
@PageTitle("Login | Location Tracker")
public class LoginView extends VerticalLayout {
    private backendClient backend = new backendClient();

    public LoginView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H1 title = new H1("Login to Location Tracker");
        TextField usernameField = new TextField("Username");
        PasswordField passwordField = new PasswordField("Password");
        Button loginButton = new Button("Authenticate");

        loginButton.addClickListener(e -> {
            String name = usernameField.getValue();
            String password = passwordField.getValue();

            boolean success = authenticateUser(name, password);

            if (success) {
                // ✅ Store username for later use across views
                VaadinSession.getCurrent().setAttribute("username", name);

                Notification.show("Login successful!");
                UI.getCurrent().navigate("home");
            } else {
                Notification.show("Invalid credentials, try again.");
            }
        });


        add(title, usernameField, passwordField, loginButton);
    }

    private boolean authenticateUser(String name, String password) {
        try {
            /*URL url = new URL("http://localhost:8080/api/auth");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = String.format("{\"name\": \"%s\", \"password\": \"%s\"}", name, password);
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            } */
            // Dummy authentication logic for now
            if ("admin".equals(name) && "1234".equals(password)) {
                // Simulated backend response
                String response = "{ \"status\": \"success\", \"name\": \"admin\" }";
                JSONObject jsonResponse = new JSONObject(response);
                return "success".equals(jsonResponse.optString("status"));
            }
            else {
                // Simulate failed response
                String reply = backend.login(name, password);
                String response = "{ \"status\": \""+ reply +"\" }";
                JSONObject jsonResponse = new JSONObject(response);
                return "success".equals(jsonResponse.optString("status"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
