package com.learning.dynamodb.model;

import com.learning.dynamodb.DynamoDBBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.regions.Region;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for User model with DynamoDB Enhanced Client.
 * Note: These tests require AWS credentials and permissions to be configured.
 */
class UserTest {
    
    private DynamoDBBase dynamoDBBase;
    
    @BeforeEach
    void setUp() {
        dynamoDBBase = new DynamoDBBase(Region.US_EAST_1);
    }
    
    @AfterEach
    void tearDown() {
        if (dynamoDBBase != null) {
            dynamoDBBase.close();
        }
    }
    
    @Test
    void testUserModelCreation() {
        User user = new User("user123", "John", "Doe", 30, "123 Main St");
        
        assertNotNull(user);
        assertEquals("user123", user.getUserId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals(30, user.getAge());
        assertEquals("123 Main St", user.getAddress());
    }
    
    @Test
    void testUserModelSetters() {
        User user = new User();
        user.setUserId("user456");
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setAge(25);
        user.setAddress("456 Oak Ave");
        
        assertEquals("user456", user.getUserId());
        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals(25, user.getAge());
        assertEquals("456 Oak Ave", user.getAddress());
    }
    
    @Test
    void testUserToString() {
        User user = new User("user789", "Bob", "Johnson", 35, "789 Pine Rd");
        String result = user.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("user789"));
        assertTrue(result.contains("Bob"));
        assertTrue(result.contains("Johnson"));
        assertTrue(result.contains("35"));
        assertTrue(result.contains("789 Pine Rd"));
    }
    
    @Test
    void testGetTableForUserModel() {
        assertNotNull(dynamoDBBase.getEnhancedClient());
        var table = dynamoDBBase.getTable(User.class, "Users");
        assertNotNull(table);
    }
}
