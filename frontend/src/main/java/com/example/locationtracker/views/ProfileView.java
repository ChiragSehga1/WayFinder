package com.example.locationtracker.views;

import com.example.locationtracker.backendClient.backendClient;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.bson.Document;

@Route("profile")
@PageTitle("Profile | Location Tracker")
public class ProfileView extends VerticalLayout {

    private final backendClient backend = new backendClient();

    public ProfileView() {

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Fetch username from session
        String username = (String) VaadinSession.getCurrent().getAttribute("username");
        if (username == null) {
            UI.getCurrent().navigate("");
            return;
        }

        // ================
        //  🔥 CALL BACKEND
        // ================
        Document user = backend.getUserDetails(username);
        System.out.println("🔥 PROFILE VIEW GOT:");
        System.out.println(user.toJson());


        String name = user.getString("name");
        String contact = user.getString("contact");


        H1 title = new H1("User Profile");

        Paragraph nameText = new Paragraph("Name: " + name);
        Paragraph usernameText = new Paragraph("Username: " + username);
        Paragraph contactText = new Paragraph("Contact: " + contact);


        // Back to Home Button
        Button back = new Button("Back to Home", e -> UI.getCurrent().navigate("home"));

        // Logout
        Button logoutButton = new Button("Logout", e -> {
            VaadinSession.getCurrent().close();
            UI.getCurrent().navigate("");
        });

        add(title, nameText, usernameText, contactText, back, logoutButton);
    }
}
