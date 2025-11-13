import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.ArrayList;
import java.util.List;

public class MongoClientConnectionExample {
    private static MongoDatabase database;
    
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String connectionString = dotenv.get("MONGO_URI");

        if (connectionString == null || connectionString.isEmpty()) {
            throw new IllegalArgumentException("Environment variable MONGO_URI is not set or empty");
        }

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();

        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                database = mongoClient.getDatabase("Cluster");
                database.runCommand(new Document("ping", 1));
                System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
                
                // Test the functions
                testDatabaseFunctions();
                
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }
    }
    
    // 1. Add a record given the fields to be filled for all tables
    
    /**
     * Add a user to UserDatabase collection
     */
    public static boolean addUser(String username, String name, String contact, String lastLocation, String password) {
        try {
            MongoCollection<Document> collection = database.getCollection("Users.UserDatabase");
            
            Document user = new Document("username", username)
                    .append("name", name)
                    .append("contact", contact)
                    .append("lastLocation", lastLocation)
                    .append("password", password);
            
            collection.insertOne(user);
            System.out.println("User added successfully: " + username);
            return true;
        } catch (MongoException e) {
            System.err.println("Error adding user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Add a friend request to Requests collection
     */
    public static boolean addRequest(String sender, String receiver, int requestStatus) {
        try {
            MongoCollection<Document> collection = database.getCollection("Users.Requests");
            
            Document request = new Document("sender", sender)
                    .append("receiver", receiver)
                    .append("requestStatus", requestStatus);
            
            collection.insertOne(request);
            System.out.println("Request added successfully: " + sender + " -> " + receiver);
            return true;
        } catch (MongoException e) {
            System.err.println("Error adding request: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Add a friendship to Friends collection
     */
    public static boolean addFriend(String userName1, String userName2) {
        try {
            MongoCollection<Document> collection = database.getCollection("Users.Friends");
            
            Document friendship = new Document("userName1", userName1)
                    .append("userName2", userName2);
            
            collection.insertOne(friendship);
            System.out.println("Friendship added successfully: " + userName1 + " <-> " + userName2);
            return true;
        } catch (MongoException e) {
            System.err.println("Error adding friendship: " + e.getMessage());
            return false;
        }
    }
    
    // 2. Remove a record given a username for all tables
    
    /**
     * Remove a user from UserDatabase collection
     */
    public static boolean removeUser(String username) {
        try {
            MongoCollection<Document> collection = database.getCollection("Users.UserDatabase");
            
            Document filter = new Document("username", username);
            DeleteResult result = collection.deleteOne(filter);
            
            if (result.getDeletedCount() > 0) {
                System.out.println("User removed successfully: " + username);
                return true;
            } else {
                System.out.println("No user found with username: " + username);
                return false;
            }
        } catch (MongoException e) {
            System.err.println("Error removing user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Remove a request from Requests collection (only if requestStatus = 1)
     */
    public static boolean removeRequest(String sender, String receiver) {
        try {
            MongoCollection<Document> collection = database.getCollection("Users.Requests");
            
            Document filter = new Document("sender", sender)
                    .append("receiver", receiver)
                    .append("requestStatus", 1);
            
            DeleteResult result = collection.deleteOne(filter);
            
            if (result.getDeletedCount() > 0) {
                System.out.println("Request removed successfully: " + sender + " -> " + receiver);
                return true;
            } else {
                System.out.println("No accepted request found between: " + sender + " and " + receiver);
                return false;
            }
        } catch (MongoException e) {
            System.err.println("Error removing request: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Remove friendship from Friends collection (removes both directions)
     */
    public static boolean removeFriend(String userName1, String userName2) {
        try {
            MongoCollection<Document> collection = database.getCollection("Users.Friends");
            
            // Remove userName1 -> userName2
            Document filter1 = new Document("userName1", userName1)
                    .append("userName2", userName2);
            
            // Remove userName2 -> userName1
            Document filter2 = new Document("userName1", userName2)
                    .append("userName2", userName1);
            
            DeleteResult result1 = collection.deleteOne(filter1);
            DeleteResult result2 = collection.deleteOne(filter2);
            
            long totalDeleted = result1.getDeletedCount() + result2.getDeletedCount();
            
            if (totalDeleted > 0) {
                System.out.println("Friendship removed successfully: " + userName1 + " <-> " + userName2 + " (Deleted: " + totalDeleted + " records)");
                return true;
            } else {
                System.out.println("No friendship found between: " + userName1 + " and " + userName2);
                return false;
            }
        } catch (MongoException e) {
            System.err.println("Error removing friendship: " + e.getMessage());
            return false;
        }
    }
    
    // 3. Return all requests given a Receiver
    
    /**
     * Get all requests for a specific receiver
     */
    public static List<Document> getAllRequests(String receiver) {
        List<Document> requests = new ArrayList<>();
        try {
            MongoCollection<Document> collection = database.getCollection("Users.Requests");
            
            Document filter = new Document("receiver", receiver);
            FindIterable<Document> results = collection.find(filter);
            
            for (Document doc : results) {
                requests.add(doc);
            }
            
            System.out.println("Found " + requests.size() + " requests for receiver: " + receiver);
            return requests;
        } catch (MongoException e) {
            System.err.println("Error getting requests: " + e.getMessage());
            return requests;
        }
    }
    
    // 4. Return all friends given UserName1
    
    /**
     * Get all friends for a specific user
     */
    public static List<String> getAllFriends(String userName1) {
        List<String> friends = new ArrayList<>();
        try {
            MongoCollection<Document> collection = database.getCollection("Users.Friends");
            
            Document filter = new Document("userName1", userName1);
            FindIterable<Document> results = collection.find(filter);
            
            for (Document doc : results) {
                friends.add(doc.getString("userName2"));
            }
            
            System.out.println("Found " + friends.size() + " friends for user: " + userName1);
            return friends;
        } catch (MongoException e) {
            System.err.println("Error getting friends: " + e.getMessage());
            return friends;
        }
    }
    
    // 5. Check if password matches a username's pass and only return true or false
    
    /**
     * Verify password for a username
     */
    public static boolean verifyPassword(String username, String password) {
        try {
            MongoCollection<Document> collection = database.getCollection("Users.UserDatabase");
            
            Document filter = new Document("username", username)
                    .append("password", password);
            
            Document user = collection.find(filter).first();
            
            boolean isValid = (user != null);
            System.out.println("Password verification for " + username + ": " + isValid);
            return isValid;
        } catch (MongoException e) {
            System.err.println("Error verifying password: " + e.getMessage());
            return false;
        }
    }
    
    // 6. Return name, contact given username
    
    /**
     * Get user details (name and contact) by username
     */
    public static Document getUserDetails(String username) {
        try {
            MongoCollection<Document> collection = database.getCollection("Users.UserDatabase");
            
            Document filter = new Document("username", username);
            Document projection = new Document("name", 1)
                    .append("contact", 1)
                    .append("_id", 0);
            
            Document user = collection.find(filter).projection(projection).first();
            
            if (user != null) {
                System.out.println("User details for " + username + ": " + user.toJson());
                return user;
            } else {
                System.out.println("No user found with username: " + username);
                return null;
            }
        } catch (MongoException e) {
            System.err.println("Error getting user details: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Test function to demonstrate all database operations
     */
    public static void testDatabaseFunctions() {
        System.out.println("\n=== Testing Database Functions ===");
        
        // Test adding users
        addUser("john_doe", "John Doe", "123-456-7890", "New York", "password123");
        addUser("jane_smith", "Jane Smith", "098-765-4321", "Los Angeles", "securepass");
        
        // Test adding requests
        addRequest("john_doe", "jane_smith", 0);
        addRequest("jane_smith", "john_doe", 1);
        
        // Test adding friends
        addFriend("john_doe", "jane_smith");
        
        // Test getting requests
        getAllRequests("jane_smith");
        
        // Test getting friends
        getAllFriends("john_doe");
        
        // Test password verification
        verifyPassword("john_doe", "password123");
        verifyPassword("john_doe", "wrongpass");
        
        // Test getting user details
        getUserDetails("john_doe");
        
        System.out.println("\n=== Database Functions Test Complete ===");
    }
}
