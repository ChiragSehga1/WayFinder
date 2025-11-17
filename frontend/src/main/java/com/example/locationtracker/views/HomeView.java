package com.example.locationtracker.views;

import com.example.locationtracker.backendClient.backendClient;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;

@Route("home")
@PageTitle("Home | WayFinder")
public class HomeView extends VerticalLayout {

    private backendClient backend = new backendClient();

    private String myGpsLat = null;
    private String myGpsLon = null;
    private boolean isAlertOn = false;

    public HomeView() {
        String savedAlert = (String) VaadinSession.getCurrent().getAttribute("alertState");
        if (savedAlert != null) {
            isAlertOn = savedAlert.equals("1");
        }

        setSizeFull();
        setAlignItems(Alignment.CENTER);

        H1 heading = new H1("Home Page");
        add(heading);


        // ===== BUTTON BAR =====
        Button addFriend = new Button("Add Friend",
                e -> UI.getCurrent().navigate("friends"));
        Button profile = new Button("Profile",
                e -> UI.getCurrent().navigate("profile"));

        Button alertToggle = new Button();

        // Apply correct initial state from session
        if (isAlertOn) {
            alertToggle.setText("Alert: ON");
            alertToggle.getStyle().set("background-color", "#ff4d4d");
            alertToggle.getStyle().set("color", "white");
        } else {
            alertToggle.setText("Alert: OFF");
            alertToggle.getStyle().set("background-color", "#d9d9d9");
            alertToggle.getStyle().set("color", "black");
        }

        alertToggle.addClickListener(e -> {
            isAlertOn = !isAlertOn;

            // SAVE ALERT STATE IN SESSION
            VaadinSession.getCurrent().setAttribute("alertState", isAlertOn ? "1" : "0");

            if (isAlertOn) {
                alertToggle.setText("Alert: ON");
                alertToggle.getStyle().set("background-color", "#ff4d4d");
                alertToggle.getStyle().set("color", "white");
            } else {
                alertToggle.setText("Alert: OFF");
                alertToggle.getStyle().set("background-color", "#d9d9d9");
                alertToggle.getStyle().set("color", "black");
            }
        });


        HorizontalLayout buttonBar = new HorizontalLayout(addFriend, profile, alertToggle);
        add(buttonBar);

        // ===== MAP CONTAINER =====
        Div mapContainer = new Div();
        mapContainer.setId("map");

        UI ui = UI.getCurrent();

        mapContainer.setWidth("80%");
        mapContainer.setHeight("70vh");
        add(mapContainer);

        // Load Leaflet
        ui.getPage().addStyleSheet("https://unpkg.com/leaflet@1.9.4/dist/leaflet.css");
        ui.getPage().addJavaScript("https://unpkg.com/leaflet@1.9.4/dist/leaflet.js");

        // Init Map
        ui.getPage().executeJs("""
            window.myMap = L.map('map').setView([28.54599, 77.27281], 13);
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                maxZoom: 19,
                attribution: '© OpenStreetMap'
            }).addTo(window.myMap);
            window.friendMarkers = [];
        """);

        // Enable GPS tracking
        injectGPSListener(ui);

        // Poll backend every 3 sec
        ui.setPollInterval(3000);
        ui.addPollListener(e -> runPollingLoop(ui));
    }

    // ===== Inject GPS listener =====
    private void injectGPSListener(UI ui) {
        ui.getPage().executeJs("""
            if (navigator.geolocation) {
                navigator.geolocation.watchPosition(pos => {
                    const lat = pos.coords.latitude;
                    const lon = pos.coords.longitude;
                    $0.$server.updateGPS(lat + "," + lon);
                });
            }
        """, getElement());
    }

    @ClientCallable
    public void updateGPS(String gps) {
        String[] p = gps.split(",");
        myGpsLat = p[0];
        myGpsLon = p[1];
    }

    // ===== Poll loop =====
    private void runPollingLoop(UI ui) {

        String username = (String) VaadinSession.getCurrent()
                .getAttribute("username");

        if (username == null || myGpsLat == null || myGpsLon == null)
            return;
        String sessionAlert = (String) VaadinSession.getCurrent().getAttribute("alertState");
        String alertBit = (sessionAlert != null && sessionAlert.equals("1")) ? "1" : "0";

        String fullLocation = myGpsLat + "," + myGpsLon + "," + alertBit;

        backend.updateLocation(username, fullLocation);

        List<String> friends = backend.getFriends(username);
        List<String> locations =
                friends.stream().map(backend::getLocation).toList();

        ui.getPage().executeJs(buildMarkerUpdateJS(friends, locations));
    }

    // ===== Marker Builder =====
    private String buildMarkerUpdateJS(List<String> friends, List<String> locations) {

        StringBuilder sb = new StringBuilder();

        // Self marker
        sb.append("""
            window.friendMarkers.forEach(m => window.myMap.removeLayer(m));
            window.friendMarkers = [];

            if (window.myMarker) window.myMap.removeLayer(window.myMarker);

            window.myMarker = L.circleMarker([""" + myGpsLat + "," + myGpsLon + """
                ], {
                radius: 12,
                color: '#0066ff',
                fillColor: '#3399ff',
                fillOpacity: 0.9,
                weight: 3
            }).addTo(window.myMap)
            .bindPopup("<b>You</b>");
        """);

        // Friend normal icon + alert icon
        sb.append("""
            var friendIcon = L.icon({
                iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-red.png',
                shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
                iconSize: [25, 41],
                iconAnchor: [12, 41],
                popupAnchor: [1, -34]
            });

            // 🔥 NEW DISTINCT ALERT MARKER 🔥
            var alertIcon = L.icon({
                iconUrl: 'https://cdn-icons-png.flaticon.com/512/463/463612.png', 
                iconSize: [40, 40],
                iconAnchor: [20, 40],
                popupAnchor: [0, -38]
            });
        """);

        // Friends loop
        for (int i = 0; i < friends.size(); i++) {

            String friend = friends.get(i);
            String loc = locations.get(i);

            if (loc == null || !loc.contains(",")) continue;

            String[] p = loc.split(",");
            String lat = p[0];
            String lon = p[1];
            String alert = p.length == 3 ? p[2] : "0";

            sb.append("""
    var iconToUse = (""" + alert + """ 
    == 1) ? alertIcon : friendIcon;

    var marker = L.marker([""" + lat + "," + lon + """
        ], { icon: iconToUse })
        .addTo(window.myMap)
        .bindPopup('<b>""" + friend +
                    (alert.equals("1") ? " ⚠ ALERT" : "") +
                    """
            </b>');
    
        window.friendMarkers.push(marker);
    """);

        }

        return sb.toString();
    }
}
