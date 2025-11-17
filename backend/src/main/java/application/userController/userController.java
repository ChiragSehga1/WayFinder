package application.userController;

import application.mongoDB.mongoDBFunctions.mongoDBFunctions;
import application.userController.dto.*;
import application.service.connection; // your functions
import com.mongodb.client.MongoClient;
import org.bson.Document;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class userController {

    private final connection connection;

    public userController(connection connection) {
        this.connection = connection;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest req) {
        return connection.login(req.username, req.password) ? "success" : "fail";
    }

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest req) {
        boolean ok = connection.sign_up(
                req.username,
                req.name,
                req.contact,
                req.lastLocation,
                req.password
        );
        return ok ? "success" : "fail";
    }

    @PostMapping("/deleteAccount")
    public String deleteAccount(@RequestBody UsernameRequest req) {
        connection.delete_account(req.username);
        return "success";
    }

    @PostMapping("/addRequest")
    public String addRequest(@RequestBody FriendRequest req) {
        connection.addRequest(req.sender, req.receiver);
        return "success";
    }

    @PostMapping("/removeRequest")
    public String removeRequest(@RequestBody FriendRequest req) {
        connection.removeRequest(req.sender, req.receiver);
        return "success";
    }

    @PostMapping("/addFriend")
    public String addFriend(@RequestBody FriendRequest req) {
        boolean ok = connection.addFriend(req.sender, req.receiver);
        return ok ? "success" : "fail";
    }

    @PostMapping("/removeFriend")
    public String removeFriend(@RequestBody FriendPair req) {
        connection.removeFriend(req.username1, req.username2);
        return "success";
    }

    @PostMapping("/getPendingRequests")
    public List<String> getPendingRequests(@RequestBody UsernameRequest req) {
        return connection.getPendingRequests(req.username);
    }

    @PostMapping("/getFriends")
    public List<String> getFriends(@RequestBody UsernameRequest req) {
        return connection.getFriends(req.username);
    }

    @PostMapping("/getLocation")
    public String getLocation(@RequestBody LocationRequest req) {
        return connection.getLocation(req.username);
    }


    @PostMapping("/updateLocation")
    public String updateLocation(@RequestBody LocationRequest req) {
        connection.updatelocation(req.username, req.location);
        return "success";
    }

    @PostMapping("/getUserDetails")
    public Document getUserDetails(@RequestBody UsernameRequest req) {
        return connection.getUserDetails(req.username);
    }
}
