package com.learning.dynamodb;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DynamoDBBase.
 * Note: These tests require AWS credentials and permissions to be configured.
 */
class DynamoDBBaseTest {
    
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
    void testDynamoDBClientCreation() {
        assertNotNull(dynamoDBBase.getDynamoDbClient(), "DynamoDB client should not be null");
        assertTrue(dynamoDBBase.getDynamoDbClient() instanceof DynamoDbClient, 
                "Should be an instance of DynamoDbClient");
    }
    
    @Test
    void testDefaultConstructor() {
        DynamoDBBase base = new DynamoDBBase();
        assertNotNull(base.getDynamoDbClient(), "DynamoDB client should not be null with default constructor");
        base.close();
    }
    
    @Test
    void testCreateStringAttribute() {
        var attribute = dynamoDBBase.createStringAttribute("testValue");
        assertNotNull(attribute, "String attribute should not be null");
        assertEquals("testValue", attribute.s(), "String attribute should have correct value");
    }
    
    @Test
    void testCreateNumberAttribute() {
        var attribute = dynamoDBBase.createNumberAttribute("123");
        assertNotNull(attribute, "Number attribute should not be null");
        assertEquals("123", attribute.n(), "Number attribute should have correct value");
    }
}
