package com.learning.dynamodb;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for DynamoDB operations using AWS SDK 2.
 * Provides common functionality for interacting with DynamoDB.
 */
public class DynamoDBBase {
    
    protected DynamoDbClient dynamoDbClient;
    protected Region region;
    
    /**
     * Default constructor that creates a DynamoDB client with default region (US_EAST_1).
     */
    public DynamoDBBase() {
        this(Region.US_EAST_1);
    }
    
    /**
     * Constructor that creates a DynamoDB client with the specified region.
     * 
     * @param region AWS region for the DynamoDB client
     */
    public DynamoDBBase(Region region) {
        this.region = region;
        this.dynamoDbClient = createDynamoDbClient();
    }
    
    /**
     * Creates and configures the DynamoDB client.
     * 
     * @return Configured DynamoDB client
     */
    protected DynamoDbClient createDynamoDbClient() {
        return DynamoDbClient.builder()
                .region(region)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
    
    /**
     * Gets the DynamoDB client instance.
     * 
     * @return DynamoDB client
     */
    public DynamoDbClient getDynamoDbClient() {
        return dynamoDbClient;
    }
    
    /**
     * Closes the DynamoDB client and releases resources.
     */
    public void close() {
        if (dynamoDbClient != null) {
            dynamoDbClient.close();
        }
    }
    
    /**
     * Lists all tables in the DynamoDB account.
     * 
     * @return ListTablesResponse containing table names
     */
    public ListTablesResponse listTables() {
        ListTablesRequest request = ListTablesRequest.builder().build();
        return dynamoDbClient.listTables(request);
    }
    
    /**
     * Creates a table with the specified name and key schema.
     * 
     * @param tableName Name of the table to create
     * @param partitionKey Partition key attribute name
     * @param partitionKeyType Key type (S for String, N for Number, B for Binary)
     * @return CreateTableResponse
     */
    public CreateTableResponse createTable(String tableName, String partitionKey, String partitionKeyType) {
        CreateTableRequest request = CreateTableRequest.builder()
                .tableName(tableName)
                .keySchema(
                        KeySchemaElement.builder()
                                .attributeName(partitionKey)
                                .keyType(KeyType.HASH)
                                .build()
                )
                .attributeDefinitions(
                        AttributeDefinition.builder()
                                .attributeName(partitionKey)
                                .attributeType(partitionKeyType)
                                .build()
                )
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .build();
        
        return dynamoDbClient.createTable(request);
    }
    
    /**
     * Deletes a table with the specified name.
     * 
     * @param tableName Name of the table to delete
     * @return DeleteTableResponse
     */
    public DeleteTableResponse deleteTable(String tableName) {
        DeleteTableRequest request = DeleteTableRequest.builder()
                .tableName(tableName)
                .build();
        
        return dynamoDbClient.deleteTable(request);
    }
    
    /**
     * Describes a table with the specified name.
     * 
     * @param tableName Name of the table to describe
     * @return DescribeTableResponse containing table details
     */
    public DescribeTableResponse describeTable(String tableName) {
        DescribeTableRequest request = DescribeTableRequest.builder()
                .tableName(tableName)
                .build();
        
        return dynamoDbClient.describeTable(request);
    }
    
    /**
     * Puts an item into the specified table.
     * 
     * @param tableName Name of the table
     * @param item Map of attribute names to attribute values
     * @return PutItemResponse
     */
    public PutItemResponse putItem(String tableName, Map<String, AttributeValue> item) {
        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();
        
        return dynamoDbClient.putItem(request);
    }
    
    /**
     * Gets an item from the specified table.
     * 
     * @param tableName Name of the table
     * @param key Map of key attribute names to attribute values
     * @return GetItemResponse
     */
    public GetItemResponse getItem(String tableName, Map<String, AttributeValue> key) {
        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();
        
        return dynamoDbClient.getItem(request);
    }
    
    /**
     * Updates an item in the specified table.
     * 
     * @param tableName Name of the table
     * @param key Map of key attribute names to attribute values
     * @param attributeUpdates Map of attribute names to update actions
     * @return UpdateItemResponse
     */
    public UpdateItemResponse updateItem(String tableName, Map<String, AttributeValue> key, 
                                        Map<String, AttributeValueUpdate> attributeUpdates) {
        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .attributeUpdates(attributeUpdates)
                .build();
        
        return dynamoDbClient.updateItem(request);
    }
    
    /**
     * Deletes an item from the specified table.
     * 
     * @param tableName Name of the table
     * @param key Map of key attribute names to attribute values
     * @return DeleteItemResponse
     */
    public DeleteItemResponse deleteItem(String tableName, Map<String, AttributeValue> key) {
        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();
        
        return dynamoDbClient.deleteItem(request);
    }
    
    /**
     * Scans the entire table.
     * 
     * @param tableName Name of the table to scan
     * @return ScanResponse containing scan results
     */
    public ScanResponse scan(String tableName) {
        ScanRequest request = ScanRequest.builder()
                .tableName(tableName)
                .build();
        
        return dynamoDbClient.scan(request);
    }
    
    /**
     * Creates an AttributeValue from a string.
     * 
     * @param value String value
     * @return AttributeValue
     */
    protected AttributeValue createStringAttribute(String value) {
        return AttributeValue.builder().s(value).build();
    }
    
    /**
     * Creates an AttributeValue from a number.
     * 
     * @param value Number value as string
     * @return AttributeValue
     */
    protected AttributeValue createNumberAttribute(String value) {
        return AttributeValue.builder().n(value).build();
    }
}
