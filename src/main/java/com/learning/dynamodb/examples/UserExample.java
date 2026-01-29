package com.learning.dynamodb.examples;

import com.learning.dynamodb.DynamoDBBase;
import com.learning.dynamodb.model.User;
import software.amazon.awssdk.regions.Region;

import java.util.List;

/**
 * Example demonstrating how to use DynamoDB with data models.
 * This example shows CRUD operations using the User model.
 */
public class UserExample {
    
    public static void main(String[] args) {
        // Create DynamoDB base instance with try-with-resources for automatic cleanup
        try (DynamoDBBase dynamoDB = new DynamoDBBase(Region.US_EAST_1)) {
            
            String tableName = "Users";
            
            // Note: Uncomment the following line to create the table
            // This only needs to be done once
            // dynamoDB.createTableForModel(User.class, tableName);
            
            // Create a new user
            User newUser = new User(
                "user001",
                "John",
                "Doe",
                30,
                "123 Main Street, Springfield"
            );
            
            // Save the user to DynamoDB
            System.out.println("Saving user...");
            dynamoDB.saveItem(User.class, tableName, newUser);
            System.out.println("User saved successfully!");
            
            // Get the user by key
            System.out.println("\nRetrieving user...");
            User keyUser = new User();
            keyUser.setUserId("user001");
            User retrievedUser = dynamoDB.getItemByKey(User.class, tableName, keyUser);
            
            if (retrievedUser != null) {
                System.out.println("Retrieved user: " + retrievedUser);
            } else {
                System.out.println("User not found!");
            }
            
            // Update the user
            if (retrievedUser != null) {
                System.out.println("\nUpdating user...");
                retrievedUser.setAge(31);
                retrievedUser.setAddress("456 Oak Avenue, Springfield");
                dynamoDB.updateItemModel(User.class, tableName, retrievedUser);
                System.out.println("User updated successfully!");
            }
            
            // Scan all users
            System.out.println("\nScanning all users...");
            List<User> allUsers = dynamoDB.scanAllItems(User.class, tableName);
            System.out.println("Found " + allUsers.size() + " user(s):");
            for (User user : allUsers) {
                System.out.println("  - " + user);
            }
            
            // Delete the user
            System.out.println("\nDeleting user...");
            User deletedUser = dynamoDB.deleteItemByKey(User.class, tableName, keyUser);
            if (deletedUser != null) {
                System.out.println("Deleted user: " + deletedUser);
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
