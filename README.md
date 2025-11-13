# WayFinder

## Overview
WayFinder is a Computer Networks course project that integrates MongoDB with Java to manage user data, requests, and friendships. This document provides guidance for developers writing logic to call the database functions implemented in `MongoClientConnectionExample.java`.

## Database Collections
The project uses the following MongoDB collections:

1. **Users.UserDatabase**
   - Fields: `username`, `name`, `contact`, `lastLocation`, `password`
2. **Users.Requests**
   - Fields: `sender`, `receiver`, `requestStatus` (0/1)
3. **Users.Friends**
   - Fields: `userName1`, `userName2`

## Available Functions
The following functions are implemented in `mongoDBFunctions.java`:

### 1. Add Records
- `addUser(String username, String name, String contact, String lastLocation, String password)`
  - Adds a user to `Users.UserDatabase`.
- `addRequest(String sender, String receiver, int requestStatus)`
  - Adds a friend request to `Users.Requests`.
- `addFriend(String userName1, String userName2)`
  - Adds a friendship to `Users.Friends`.

### 2. Remove Records
- `removeUser(String username)`
  - Removes a user from `Users.UserDatabase`.
- `removeRequest(String sender, String receiver)`
  - Removes a request from `Users.Requests` (only if `requestStatus = 1`).
- `removeFriend(String userName1, String userName2)`
  - Removes a friendship from `Users.Friends` (both directions).

### 3. Query Functions
- `getAllRequests(String receiver)`
  - Returns all requests for a specific receiver.
- `getAllFriends(String userName1)`
  - Returns all friends for a specific user.
- `verifyPassword(String username, String password)`
  - Verifies if the password matches the username.
- `getUserDetails(String username)`
  - Returns the `name` and `contact` for a specific username.

## Usage Example
Here is an example of how to use the functions:

```java
// Add a user
mongoDBFunctions.addUser("john_doe", "John Doe", "123-456-7890", "New York", "password123");

// Add a friend request
mongoDBFunctions.addRequest("john_doe", "jane_smith", 0);

// Add a friendship
mongoDBFunctions.addFriend("john_doe", "jane_smith");

// Get all requests for a receiver
List<Document> requests = mongoDBFunctions.getAllRequests("jane_smith");

// Get all friends for a user
List<String> friends = mongoDBFunctions.getAllFriends("john_doe");

// Verify a user's password
boolean isValid = mongoDBFunctions.verifyPassword("john_doe", "password123");

// Get user details
Document userDetails = mongoDBFunctions.getUserDetails("john_doe");
```

## Notes
- Ensure the MongoDB connection string is correctly set in the `.env` file under the key `MONGO_URI`.
- Handle exceptions gracefully when calling these functions in your application logic.
- Refer to the `testDatabaseFunctions()` method in `MongoClientConnectionExample.java` for a demonstration of all operations.

## Contact
For any issues or questions, please contact the project maintainer.