package com.example.locationtracker.views;

import com.example.locationtracker.backendClient.backendClient;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;

@Route("friends")
@PageTitle("Friends | WayFinder")
public class FriendsView extends VerticalLayout {

    private backendClient backend = new backendClient();

    public FriendsView() {

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);
        setSpacing(true);

        String username = (String) VaadinSession.getCurrent().getAttribute("username");

        H1 title = new H1("Friends");
        add(title);

        // ============================================================
        // 1️⃣ SEARCH USERS AND SEND FRIEND REQUEST
        // ============================================================

        Paragraph searchHeader = new Paragraph("Send Friend Request:");

        TextField searchUser = new TextField("Enter username");
        Button sendRequestBtn = new Button("Send Request", e -> {
        String receiver = searchUser.getValue().trim();

        if (receiver.isEmpty()) {
            searchUser.setErrorMessage("Username cannot be empty");
            searchUser.setInvalid(true);
            return;
        }
        String response = backend.addRequest(username, receiver);

        searchUser.clear();
        searchUser.setInvalid(false);

        if (response == "success") {
            add(new Paragraph("📤 Request sent to: " + receiver + " (" + response + ")"));
        }
        });

        add(searchHeader, searchUser, sendRequestBtn);


        // ============================================================
        // 2️⃣ SHOW PENDING FRIEND REQUESTS (YOU NEED TO ACCEPT/REJECT)
        // ============================================================

        Paragraph pendingHeader = new Paragraph("Pending Friend Requests:");

        VerticalLayout pendingList = new VerticalLayout();
        pendingList.setWidth("400px");

        List<String> pending = backend.getPendingRequests(username);

        if (pending.isEmpty()) {
            pendingList.add(new Paragraph("No pending requests."));
        }
        else {
            for (String sender : pending) {

                Paragraph reqText = new Paragraph("Request from: " + sender);

                Button accept = new Button("Accept", e -> {
                    backend.addFriend(sender, username);
                    pendingList.add(new Paragraph("Accepted " + sender));
                    UI.getCurrent().getPage().reload();
                });

                Button reject = new Button("Reject", e -> {
                    backend.removeRequest(sender, username);
                    pendingList.add(new Paragraph("Rejected " + sender));
                    UI.getCurrent().getPage().reload();

                });

                HorizontalLayout row = new HorizontalLayout(reqText, accept, reject);
                pendingList.add(row);
            }
        }

        add(pendingHeader, pendingList);


        // ============================================================
        // 3️⃣ SHOW FRIEND LIST
        // ============================================================

        Paragraph friendsHeader = new Paragraph("Your Friends:");

        VerticalLayout friendsList = new VerticalLayout();
        friendsList.setWidth("400px");

        List<String> friends = backend.getFriends(username);

        if (friends.isEmpty()) {
            friendsList.add(new Paragraph("You have no friends yet."));
        } else {
            for (String friend : friends) {

                Paragraph fText = new Paragraph(friend);

                Button remove = new Button("Remove", e -> {
                    backend.removeFriend(username, friend);
                    friendsList.add(new Paragraph("Removed " + friend));
                    UI.getCurrent().getPage().reload();

                });

                HorizontalLayout row = new HorizontalLayout(fText, remove);
                friendsList.add(row);
            }
        }

        add(friendsHeader, friendsList);


        // ============================================================
        // 4️⃣ BACK TO HOME BUTTON
        // ============================================================

        Button back = new Button("Back to Home", e -> UI.getCurrent().navigate("home"));
        add(back);
    }
}
