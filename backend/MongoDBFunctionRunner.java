import org.bson.Document;
import java.util.List;

public class MongoDBFunctionRunner {
    public static void main(String[] args) {
        // Add a user
        MongoClientConnectionExample.addUser("john_doe", "John Doe", "123-456-7890", "New York", "password123");

        // Add a friend request
        MongoClientConnectionExample.addRequest("john_doe", "jane_smith", 0);

        // Add a friendship
        MongoClientConnectionExample.addFriend("john_doe", "jane_smith");

        // Get all requests for a receiver
        List<Document> requests = MongoClientConnectionExample.getAllRequests("jane_smith");
        System.out.println("Requests for jane_smith: " + requests);

        // Get all friends for a user
        List<String> friends = MongoClientConnectionExample.getAllFriends("john_doe");
        System.out.println("Friends of john_doe: " + friends);

        // Verify a user's password
        boolean isValid = MongoClientConnectionExample.verifyPassword("john_doe", "password123");
        System.out.println("Password valid for john_doe: " + isValid);

        // Get user details
        Document userDetails = MongoClientConnectionExample.getUserDetails("john_doe");
        System.out.println("User details for john_doe: " + userDetails);
    }
}