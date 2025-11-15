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

    public static void delete_Account(String username) {
        mongoDBFunctions.removeUser(username);
    }

    public static void addRequest(String sender, String receiver){
        mongoDBFunctions.addRequest(sender, receiver, 0);
    }

    public static void removeRequest(String sender, String receiver){
        mongoDBFunctions.removeRequest(sender, receiver);
    }

    public static boolean addFriend(String userName1, String userName2){
        if (mongoDBFunctions.getAllFriends(userName1).size() < 5
                && mongoDBFunctions.getAllFriends(userName2).size()<5) {
            mongoDBFunctions.addFriend(userName1, userName2);
            return true;
        }
        else {
            return false;
        }
    }

    public static void removeFriend(String userName1, String userName2){
        mongoDBFunctions.removeFriend(userName1, userName2);
    }

    public static List<String> getPendingRequests(String userName){
        return mongoDBFunctions.getPendingRequests(userName);
    }

    public  static List<String> getFriends(String userName){
        return mongoDBFunctions.getAllFriends(userName);
    }

    public static String getLocation(String userName){
        return mongoDBFunctions.getLocation(userName);
    }

    public static boolean updatelocation(String username, String location) {
        mongoDBFunctions.updateLocation(username, location);
        return true;
    }

    public static Document getUserDetails(String userName){
        return mongoDBFunctions.getUserDetails(userName);
    }

}
