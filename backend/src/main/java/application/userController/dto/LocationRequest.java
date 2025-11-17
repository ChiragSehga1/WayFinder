package application.userController.dto;

public class LocationRequest {
    public String username;
    public String location;

    public LocationRequest(String username, String location) {
        this.username = username;
        this.location = location;
    }
}
