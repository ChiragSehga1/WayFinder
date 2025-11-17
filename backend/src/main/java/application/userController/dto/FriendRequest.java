package application.userController.dto;

public class FriendRequest {
    public String sender;
    public String receiver;

    public FriendRequest(String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }
}
