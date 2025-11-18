package application.service;
import application.mongoDB.mongoDBFunctions.mongoDBFunctions;
import jakarta.annotation.PostConstruct;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class connection {
    @Value("${MONGO_URI}")
    String uri;

    private final mongoDBFunctions mongoDBFunctions;

    public connection(mongoDBFunctions mongoDBFunctions) {
        this.mongoDBFunctions = mongoDBFunctions;
    }

    @PostConstruct
    public void check() {
        System.out.println("Connection service initialized!");
    }

    public boolean login(String username, String password) {
         if (mongoDBFunctions.verifyUsername(username)) {
             return mongoDBFunctions.verifyPassword(username, password);
        }
        else {
            return false;
        }
    }

    public boolean sign_up(String username, String name, String contact, String lastLocation, String password) {
        if (!mongoDBFunctions.verifyUsername(username)) {
            mongoDBFunctions.addUser(username, name, contact, lastLocation, password);
            return true;
        }
        else {
            return false;
        }
    }

    public void delete_account(String username) {
        mongoDBFunctions.removeUser(username);
    }

    public boolean addRequest(String sender, String receiver){
        List<String> friends = mongoDBFunctions.getAllFriends(sender);
        if(!receiver.equals(sender) && !friends.contains(receiver) && mongoDBFunctions.verifyUsername(receiver)) {
            if (!getPendingRequests(receiver).contains(sender)){
                mongoDBFunctions.addRequest(sender, receiver, 0);
                return true;
            }
        }
        return false;
    }

    public void removeRequest(String sender, String receiver){
        mongoDBFunctions.acceptRequest(sender, receiver);
    }

    public boolean addFriend(String username1, String username2){
        if (mongoDBFunctions.getAllFriends(username1).size() < 10
                && mongoDBFunctions.getAllFriends(username2).size()< 10) {
            mongoDBFunctions.addFriend(username1, username2);
            mongoDBFunctions.acceptRequest(username1, username2);
            return true;
        }
        else {
            return false;
        }
    }

    public void removeFriend(String username1, String username2){
        mongoDBFunctions.removeFriend(username1, username2);
    }

    public List<String> getPendingRequests(String username){
        return mongoDBFunctions.getPendingRequests(username);
    }

    public  List<String> getFriends(String username){
        return mongoDBFunctions.getAllFriends(username);
    }

    public String getLocation(String username){
        return mongoDBFunctions.getLocation(username);
    }

    public void updatelocation(String username, String location) {
        mongoDBFunctions.updateLocation(username, location);
    }

    public Document getUserDetails(String username){
        return mongoDBFunctions.getUserDetails(username);
    }

}
