package com.learning.dynamodb;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Base class for DynamoDB operations using AWS SDK 2.
 * Provides common functionality for interacting with DynamoDB using both
 * low-level client (for map-based operations) and Enhanced Client (for data model operations).
 * This class implements AutoCloseable to support try-with-resources pattern.
 */
public class DynamoDBBase implements AutoCloseable {
    
    protected DynamoDbClient dynamoDbClient;
    protected DynamoDbEnhancedClient enhancedClient;
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
        this.enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
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
     * Gets the DynamoDB Enhanced Client instance.
     * 
     * @return DynamoDB Enhanced Client
     */
    public DynamoDbEnhancedClient getEnhancedClient() {
        return enhancedClient;
    }
    
    /**
     * Closes the DynamoDB client and releases resources.
     */
    public void close() {
        if (enhancedClient != null) {
            // Enhanced client doesn't have a close method, but we close the underlying client
        }
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
     * Note: This method only supports tables with a partition key (hash key).
     * For tables with composite keys (partition + sort key), additional methods would be needed.
     * 
     * @param tableName Name of the table to create
     * @param partitionKey Partition key attribute name
     * @param partitionKeyType Key type (S for String, N for Number, B for Binary)
     * @return CreateTableResponse
     * @throws IllegalArgumentException if tableName or partitionKey is null or empty
     */
    public CreateTableResponse createTable(String tableName, String partitionKey, String partitionKeyType) {
        validateNotEmpty(tableName, "tableName");
        validateNotEmpty(partitionKey, "partitionKey");
        validateNotEmpty(partitionKeyType, "partitionKeyType");
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
     * @throws IllegalArgumentException if tableName is null or empty, or if item is null
     */
    public PutItemResponse putItem(String tableName, Map<String, AttributeValue> item) {
        validateNotEmpty(tableName, "tableName");
        validateNotNull(item, "item");
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
     * @throws IllegalArgumentException if tableName is null or empty, or if key is null
     */
    public GetItemResponse getItem(String tableName, Map<String, AttributeValue> key) {
        validateNotEmpty(tableName, "tableName");
        validateNotNull(key, "key");
        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();
        
        return dynamoDbClient.getItem(request);
    }
    
    /**
     * Updates an item in the specified table.
     * Note: This method uses the attributeUpdates API, which is maintained for backward compatibility.
     * For new code, consider using UpdateExpression with ExpressionAttributeValues instead.
     * 
     * @param tableName Name of the table
     * @param key Map of key attribute names to attribute values
     * @param attributeUpdates Map of attribute names to update actions
     * @return UpdateItemResponse
     * @throws IllegalArgumentException if tableName is null or empty, or if key/attributeUpdates are null
     */
    public UpdateItemResponse updateItem(String tableName, Map<String, AttributeValue> key, 
                                        Map<String, AttributeValueUpdate> attributeUpdates) {
        validateNotEmpty(tableName, "tableName");
        validateNotNull(key, "key");
        validateNotNull(attributeUpdates, "attributeUpdates");
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
     * @throws IllegalArgumentException if tableName is null or empty, or if key is null
     */
    public DeleteItemResponse deleteItem(String tableName, Map<String, AttributeValue> key) {
        validateNotEmpty(tableName, "tableName");
        validateNotNull(key, "key");
        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();
        
        return dynamoDbClient.deleteItem(request);
    }
    
    /**
     * Scans the entire table.
     * Note: DynamoDB paginates scan results. This method returns only the first page.
     * For large tables, you may need to check LastEvaluatedKey and make subsequent requests.
     * 
     * @param tableName Name of the table to scan
     * @return ScanResponse containing scan results
     * @throws IllegalArgumentException if tableName is null or empty
     */
    public ScanResponse scan(String tableName) {
        validateNotEmpty(tableName, "tableName");
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
    public AttributeValue createStringAttribute(String value) {
        return AttributeValue.builder().s(value).build();
    }
    
    /**
     * Creates an AttributeValue from a number.
     * 
     * @param value Number value as string
     * @return AttributeValue
     */
    public AttributeValue createNumberAttribute(String value) {
        return AttributeValue.builder().n(value).build();
    }
    
    // ================ Enhanced Client Methods for Data Models ================
    
    /**
     * Gets a DynamoDB table instance for the specified data model class.
     * 
     * @param <T> The type of the data model
     * @param modelClass The class of the data model
     * @param tableName The name of the table
     * @return DynamoDbTable instance
     */
    public <T> DynamoDbTable<T> getTable(Class<T> modelClass, String tableName) {
        validateNotNull(modelClass, "modelClass");
        validateNotEmpty(tableName, "tableName");
        return enhancedClient.table(tableName, TableSchema.fromBean(modelClass));
    }
    
    /**
     * Creates a table for the specified data model class.
     * The table schema is derived from the model class annotations.
     * 
     * @param <T> The type of the data model
     * @param modelClass The class of the data model
     * @param tableName The name of the table to create
     */
    public <T> void createTableForModel(Class<T> modelClass, String tableName) {
        validateNotNull(modelClass, "modelClass");
        validateNotEmpty(tableName, "tableName");
        DynamoDbTable<T> table = getTable(modelClass, tableName);
        table.createTable();
    }
    
    /**
     * Saves (puts) a data model item to the table.
     * 
     * @param <T> The type of the data model
     * @param modelClass The class of the data model
     * @param tableName The name of the table
     * @param item The item to save
     */
    public <T> void saveItem(Class<T> modelClass, String tableName, T item) {
        validateNotNull(modelClass, "modelClass");
        validateNotEmpty(tableName, "tableName");
        validateNotNull(item, "item");
        DynamoDbTable<T> table = getTable(modelClass, tableName);
        table.putItem(item);
    }
    
    /**
     * Gets a data model item by its key.
     * 
     * @param <T> The type of the data model
     * @param modelClass The class of the data model
     * @param tableName The name of the table
     * @param keyItem An instance with only the key attributes set
     * @return The retrieved item, or null if not found
     */
    public <T> T getItemByKey(Class<T> modelClass, String tableName, T keyItem) {
        validateNotNull(modelClass, "modelClass");
        validateNotEmpty(tableName, "tableName");
        validateNotNull(keyItem, "keyItem");
        DynamoDbTable<T> table = getTable(modelClass, tableName);
        return table.getItem(keyItem);
    }
    
    /**
     * Updates a data model item in the table.
     * 
     * @param <T> The type of the data model
     * @param modelClass The class of the data model
     * @param tableName The name of the table
     * @param item The item to update
     * @return The updated item
     */
    public <T> T updateItemModel(Class<T> modelClass, String tableName, T item) {
        validateNotNull(modelClass, "modelClass");
        validateNotEmpty(tableName, "tableName");
        validateNotNull(item, "item");
        DynamoDbTable<T> table = getTable(modelClass, tableName);
        return table.updateItem(item);
    }
    
    /**
     * Deletes a data model item by its key.
     * 
     * @param <T> The type of the data model
     * @param modelClass The class of the data model
     * @param tableName The name of the table
     * @param keyItem An instance with only the key attributes set
     * @return The deleted item
     */
    public <T> T deleteItemByKey(Class<T> modelClass, String tableName, T keyItem) {
        validateNotNull(modelClass, "modelClass");
        validateNotEmpty(tableName, "tableName");
        validateNotNull(keyItem, "keyItem");
        DynamoDbTable<T> table = getTable(modelClass, tableName);
        return table.deleteItem(keyItem);
    }
    
    /**
     * Scans the entire table and returns all items as data model objects.
     * Note: For large tables, consider using pagination.
     * 
     * @param <T> The type of the data model
     * @param modelClass The class of the data model
     * @param tableName The name of the table
     * @return List of all items in the table
     */
    public <T> List<T> scanAllItems(Class<T> modelClass, String tableName) {
        validateNotNull(modelClass, "modelClass");
        validateNotEmpty(tableName, "tableName");
        DynamoDbTable<T> table = getTable(modelClass, tableName);
        return table.scan().items().stream().collect(Collectors.toList());
    }
    
    /**
     * Validates that a string parameter is not null or empty.
     * 
     * @param value Value to validate
     * @param paramName Parameter name for error message
     * @throws IllegalArgumentException if value is null or empty
     */
    private void validateNotEmpty(String value, String paramName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(paramName + " cannot be null or empty");
        }
    }
    
    /**
     * Validates that an object parameter is not null.
     * 
     * @param value Value to validate
     * @param paramName Parameter name for error message
     * @throws IllegalArgumentException if value is null
     */
    private void validateNotNull(Object value, String paramName) {
        if (value == null) {
            throw new IllegalArgumentException(paramName + " cannot be null");
        }
    }
}
