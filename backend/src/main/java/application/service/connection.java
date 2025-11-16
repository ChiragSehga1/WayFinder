package application.service;
import jakarta.annotation.PostConstruct;
import mongoDBFunctions.mongoDBFunctions;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class connection {
    @Value("${MONGO_URI}")
    String uri;

    @PostConstruct
    public void check() {
        System.out.println("Loaded URI = " + uri);
    }

    public static boolean login(String username, String password) {
         if (mongoDBFunctions.verifyUsername(username)) {
             return mongoDBFunctions.verifyPassword(username, password);
        }
        else {
            return false;
        }
    }

    public static boolean sign_up(String username, String name, String contact, String lastLocation, String password) {
        if (!mongoDBFunctions.verifyUsername(username)) {
            mongoDBFunctions.addUser(username, name, contact, lastLocation, password);
            return true;
        }
        else {
            return false;
        }
    }

    public static void delete_account(String username) {
        mongoDBFunctions.removeUser(username);
    }

    public static void addRequest(String sender, String receiver){
        mongoDBFunctions.addRequest(sender, receiver, 0);
    }

    public static void removeRequest(String sender, String receiver){
        mongoDBFunctions.removeRequest(sender, receiver);
    }

    public static boolean addFriend(String username1, String username2){
        if (mongoDBFunctions.getAllFriends(username1).size() < 5
                && mongoDBFunctions.getAllFriends(username2).size()<5) {
            mongoDBFunctions.addFriend(username1, username2);
            return true;
        }
        else {
            return false;
        }
    }

    public static void removeFriend(String username1, String username2){
        mongoDBFunctions.removeFriend(username1, username2);
    }

    public static List<String> getPendingRequests(String username){
        return mongoDBFunctions.getPendingRequests(username);
    }

    public  static List<String> getFriends(String username){
        return mongoDBFunctions.getAllFriends(username);
    }

    public static String getLocation(String username){
        return mongoDBFunctions.getLocation(username);
    }

    public static void updatelocation(String username, String location) {
        mongoDBFunctions.updateLocation(username, location);
    }

    public static Document getUserDetails(String username){
        return mongoDBFunctions.getUserDetails(username);
    }

}
