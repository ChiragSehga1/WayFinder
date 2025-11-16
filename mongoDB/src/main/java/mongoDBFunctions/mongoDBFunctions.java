package mongoDBFunctions;
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
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.ArrayList;
import java.util.List;

public class mongoDBFunctions {
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

     // Add a user to UserDatabase collection
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
    
    //Add a friend request to Requests collection
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
    
     //Add a friendship to Friends collection
    public static boolean addFriend(String userName1, String userName2) {
        try {
            MongoCollection<Document> collection = database.getCollection("Users.Friends");
            
            Document friendship = new Document("userName1", userName1)
                    .append("userName2", userName2);
            Document friendship2 = new Document("userName1", userName2)
                    .append("userName2", userName1);
            
            collection.insertOne(friendship);
            collection.insertOne(friendship2);
            System.out.println("Friendship added successfully: " + userName1 + " <-> " + userName2);
            return true;
        } catch (MongoException e) {
            System.err.println("Error adding friendship: " + e.getMessage());
            return false;
        }
    }

     // Remove a user from UserDatabase collection
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
    
     // Remove a request from Requests collection (only if requestStatus = 1)
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

    //Remove friendship from Friends collection (removes both directions
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
    
    // 3. Return all pending request senders for a given receiver
    public static List<String> getPendingRequests(String receiver) {
        List<String> senders = new ArrayList<>();
        try {
            MongoCollection<Document> collection = database.getCollection("Users.Requests");
            
            Document filter = new Document("receiver", receiver).append("requestStatus",0);
            FindIterable<Document> results = collection.find(filter);
            
            for (Document doc : results) {
                senders.add(doc.getString("sender"));
            }
            
            System.out.println("Found " + senders.size() + " pending requests for receiver: " + receiver);
            System.out.println("Senders " + senders);
            return senders;
        } catch (MongoException e) {
            System.err.println("Error getting requests: " + e.getMessage());
            return senders;
        }
    }
    
    // 4. Return all friends given UserName1
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
            System.out.println("Friends " + friends);
            return friends;
        } catch (MongoException e) {
            System.err.println("Error getting friends: " + e.getMessage());
            return friends;
        }
    }
    
    // 5. Check if password matches a username's pass and only return true or false
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

    // 5.1 Check if password matches a username's pass and only return true or false
    public static boolean verifyUsername(String username) {
        try {
            MongoCollection<Document> collection = database.getCollection("Users.UserDatabase");

            Document filter = new Document("username", username);

            Document user = collection.find(filter).first();

            boolean isValid = (user != null);
            System.out.println("Username" + username + " xistence : " + isValid);
            return isValid;
        } catch (MongoException e) {
            System.err.println("Error verifying username: " + e.getMessage());
            return false;
        }
    }


    // 6. Return name, contact given username
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
    
    // 7. Accept a friend request (set requestStatus = 1)
    public static boolean acceptRequest(String sender, String receiver) {
        try {
            MongoCollection<Document> collection = database.getCollection("Users.Requests");
            
            Document filter = new Document("sender", sender)
                    .append("receiver", receiver)
                    .append("requestStatus", 0);
            
            Document update = new Document("$set", new Document("requestStatus", 1));
            
            long modifiedCount = collection.updateOne(filter, update).getModifiedCount();
            
            if (modifiedCount > 0) {
                System.out.println("Request accepted successfully: " + sender + " -> " + receiver);
                return true;
            } else {
                System.out.println("No pending request found between: " + sender + " and " + receiver);
                return false;
            }
        } catch (MongoException e) {
            System.err.println("Error accepting request: " + e.getMessage());
            return false;
        }
    }
    
    // 8. Get user's current location
    public static String getLocation(String username) {
        try {
            MongoCollection<Document> collection = database.getCollection("Users.UserDatabase");
            
            Document filter = new Document("username", username);
            Document projection = new Document("lastLocation", 1)
                    .append("_id", 0);
            
            Document user = collection.find(filter).projection(projection).first();
            
            if (user != null) {
                String location = user.getString("lastLocation");
                System.out.println("Location for " + username + ": " + location);
                return location;
            } else {
                System.out.println("No user found with username: " + username);
                return null;
            }
        } catch (MongoException e) {
            System.err.println("Error getting user location: " + e.getMessage());
            return null;
        }
    }
    
    // 9. Update user's location
    public static boolean updateLocation(String username, String newLocation) {
        try {
            MongoCollection<Document> collection = database.getCollection("Users.UserDatabase");
            
            Document filter = new Document("username", username);
            Document update = new Document("$set", new Document("lastLocation", newLocation));
            
            long modifiedCount = collection.updateOne(filter, update).getModifiedCount();
            
            if (modifiedCount > 0) {
                System.out.println("Location updated successfully for " + username + ": " + newLocation);
                return true;
            } else {
                System.out.println("Location remains the same OR No user found with username: " + username);
                return false;
            }
        } catch (MongoException e) {
            System.err.println("Error updating user location: " + e.getMessage());
            return false;
        }
    }
    
    // 10. Display all records from all three collections
    public static void displayAllDatabases() {
        try {
            System.out.println("\n=== DISPLAYING ALL DATABASE RECORDS ===");
            
            // Display Users.UserDatabase
            System.out.println("\n--- Users.UserDatabase ---");
            MongoCollection<Document> userCollection = database.getCollection("Users.UserDatabase");
            FindIterable<Document> users = userCollection.find();
            int userCount = 0;
            for (Document user : users) {
                userCount++;
                System.out.println("User " + userCount + ": " + user.toJson());
            }
            System.out.println("Total Users: " + userCount);
            
            // Display Users.Requests
            System.out.println("\n--- Users.Requests ---");
            MongoCollection<Document> requestCollection = database.getCollection("Users.Requests");
            FindIterable<Document> requests = requestCollection.find();
            int requestCount = 0;
            for (Document request : requests) {
                requestCount++;
                System.out.println("Request " + requestCount + ": " + request.toJson());
            }
            System.out.println("Total Requests: " + requestCount);
            
            // Display Users.Friends
            System.out.println("\n--- Users.Friends ---");
            MongoCollection<Document> friendCollection = database.getCollection("Users.Friends");
            FindIterable<Document> friends = friendCollection.find();
            int friendshipCount = 0;
            for (Document friendship : friends) {
                friendshipCount++;
                System.out.println("Friendship " + friendshipCount + ": " + friendship.toJson());
            }
            System.out.println("Total Friendships: " + friendshipCount);
            
            System.out.println("\n=== DATABASE DISPLAY COMPLETE ===");
            
        } catch (MongoException e) {
            System.err.println("Error displaying databases: " + e.getMessage());
        }
    }
    
     //Test function to demonstrate all database operations
    public static void testDatabaseFunctions() {
        System.out.println("\n=== Testing Database Functions ===");
        
        // Test adding users
//        addUser("a.sh*t", "Akshat Patiyal", "098-765-4321", "73.444,27.3333", "1234");
//        addUser("snehgal", "Chirag Sehgal", "198-765-4321", "83.444,27.3333", "1234");
        removeUser("jasjyotg");
        addUser("jasjyotg", "Jasjyot Gulati", "298-765-4321", "93.444,27.3333", "1234");
//        addUser("cobalt", "Dhruv Jaiswal", "398-765-4321", "103.444,27.3333", "1234");
//        addUser("admin", "Admin", "498-765-4321", "173.444,27.3333", "1234");


        // Test adding requests
        addRequest("snehgal", "a.sh*t", 0);
        addRequest("jasjyotg", "a.sh*t", 0);

        // Test accepting a request
        acceptRequest("jasjyotg", "a.sh*t");

        // Test getting requests
        getPendingRequests("a.sh*t");

        // Test adding and removing friends
        removeFriend("jasjyotg", "a.sh*t");
        removeFriend("jasjyotg", "cobalt");
        removeFriend("jasjyotg", "snehgal");
        removeFriend("cobalt", "snehgal");

        getAllFriends("jasjyotg");

        addFriend("jasjyotg", "a.sh*t");
        addFriend("jasjyotg", "cobalt");
        addFriend("jasjyotg", "snehgal");
        addFriend("cobalt", "snehgal");

        // Test getting friends
        getAllFriends("jasjyotg");

        // Test username verification
        verifyUsername("nonono");

        // Test password verification
        verifyPassword("admin", "1234");

        // Test getting user details
        getUserDetails("a.sh*t");

        // Display all database records
        displayAllDatabases();

        // Test updating user location
        getUserDetails("jasjyotg");

        // Test getting user location
        getLocation("jasjyotg");
        updateLocation("jasjyotg", "28.6139,77.2090");
        getLocation("jasjyotg");
        
        System.out.println("\n=== Database Functions Test Complete ===");
    }
}
