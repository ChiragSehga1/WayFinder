package application.userController.dto;

public class SignupRequest {
    public String username;
    public String name;
    public String contact;
    public String lastLocation;
    public String password;

    public SignupRequest(String username, String name, String contact, String lastLocation, String password) {
        this.username = username;
        this.name = name;
        this.contact = contact;
        this.lastLocation = lastLocation;
        this.password = password;
    }
}
