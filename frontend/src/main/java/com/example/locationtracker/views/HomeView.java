package com.example.locationtracker.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("home")
@PageTitle("Home | Location Tracker")
public class HomeView extends VerticalLayout {

    public HomeView() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // ======== TOP BAR ========
        H1 heading = new H1("Home Page");
        heading.getStyle().set("margin", "0").set("font-size", "24px");

        Button profileButton = new Button("Profile", e -> UI.getCurrent().navigate("profile"));
        HorizontalLayout topBar = new HorizontalLayout(heading, profileButton);
        topBar.setWidthFull();
        topBar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        topBar.setAlignItems(Alignment.CENTER);
        topBar.getStyle()
                .set("padding", "10px 20px")
                .set("background-color", "#ffffff")
                .set("box-shadow", "0 2px 5px rgba(0,0,0,0.1)");

        // ======== MAP CONTAINER ========
        Div mapContainer = new Div();
        mapContainer.setId("map");
        mapContainer.setWidth("80%");
        mapContainer.setHeight("70vh");
        mapContainer.getStyle()
                .set("border", "2px solid #ccc")
                .set("border-radius", "12px")
                .set("overflow", "hidden")
                .set("box-shadow", "0 4px 15px rgba(0,0,0,0.2)")
                .set("margin", "40px auto");

        // ======== BOTTOM BUTTON ========
        Button sendLocation = new Button("Send Location", e -> Notification.show("Location sent! (simulated)"));
        sendLocation.getStyle()
                .set("background-color", "#007bff")
                .set("color", "white")
                .set("border-radius", "8px")
                .set("padding", "12px 24px")
                .set("font-size", "16px");

        // ======== LAYOUT ========
        add(topBar, mapContainer, sendLocation);
        setHorizontalComponentAlignment(Alignment.CENTER, mapContainer, sendLocation);

        // ======== INITIALIZE LEAFLET MAP WITH MULTIPLE MARKERS ========
        UI.getCurrent().getPage().addStyleSheet("https://unpkg.com/leaflet@1.9.4/dist/leaflet.css");
        UI.getCurrent().getPage().addJavaScript("https://unpkg.com/leaflet@1.9.4/dist/leaflet.js");

        UI.getCurrent().getPage().executeJs("""
            const map = L.map('map').setView([28.54599, 77.27281], 13);

            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                maxZoom: 19,
                attribution: '© OpenStreetMap'
            }).addTo(map);

            // Add multiple markers
            const markers = [
                {lat: 28.5494, lon: 77.2010, label: "Hauz Khas"},
                {lat: 28.5355, lon: 77.2430, label: "Greater Kailash"},
                {lat: 28.54599, lon: 77.27281, label: "Custom Location"}
            ];

            markers.forEach(m => {
                L.marker([m.lat, m.lon]).addTo(map)
                    .bindPopup("<b>" + m.label + "</b><br>(" + m.lat + ", " + m.lon + ")");
            });
        """);
    }
}
