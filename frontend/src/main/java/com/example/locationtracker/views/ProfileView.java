package com.example.locationtracker.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("profile")
@PageTitle("Profile | Location Tracker")
public class ProfileView extends VerticalLayout {

    public ProfileView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // ✅ Retrieve username from session
        String username = (String) VaadinSession.getCurrent().getAttribute("username");
        if (username == null) {
            username = "Guest";
        }

        H1 title = new H1("User Profile");
        Paragraph nameText = new Paragraph("Name: " + username);
        Paragraph usernameText = new Paragraph("Username: " + username);
        Paragraph contactText = new Paragraph("Contact: +91-9876543210");

        // ✅ Logout button clears session and redirects
        Button logoutButton = new Button("Logout", e -> {
            VaadinSession.getCurrent().close();
            UI.getCurrent().navigate("");
        });

        add(title, nameText, usernameText, contactText, logoutButton);
    }
}
/*

package com.example.locationtracker.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Route("profile")
@PageTitle("Profile | Location Tracker")
public class ProfileView extends VerticalLayout {

    private Paragraph nameText;
    private Paragraph usernameText;
    private Paragraph contactText;

    public ProfileView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H1 title = new H1("User Profile");
        nameText = new Paragraph("Loading...");
        usernameText = new Paragraph("");
        contactText = new Paragraph("");

        Button logoutButton = new Button("Logout", e -> {
            VaadinSession.getCurrent().close();
            UI.getCurrent().navigate("");
        });

        add(title, nameText, usernameText, contactText, logoutButton);

        // Fetch user details
        String username = (String) VaadinSession.getCurrent().getAttribute("username");
        if (username != null) {
            fetchUserData(username);
        } else {
            Notification.show("No user logged in!");
        }
    }

    private void fetchUserData(String username) {
        try {
            URL url = new URL("http://localhost:8080/api/user/" + username);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            JSONObject json = new JSONObject(response.toString());

            String name = json.optString("name", "Unknown");
            String contact = json.optString("contact", "Not available");

            nameText.setText("Name: " + name);
            usernameText.setText("Username: " + username);
            contactText.setText("Contact: " + contact);

        } catch (Exception e) {
            e.printStackTrace();
            Notification.show("Failed to load user data");
        }
    }
}
*/