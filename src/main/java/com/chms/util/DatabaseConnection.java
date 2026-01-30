package com.chms.util;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database Connection Manager using Apache Commons DBCP Connection Pooling
 * This class provides thread-safe database connections for the CHMS application
 */
public class DatabaseConnection {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    private static BasicDataSource dataSource;
    
    // Static block to initialize the connection pool
    static {
        try {
            initializeDataSource();
            logger.info("Database connection pool initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize database connection pool", e);
            throw new ExceptionInInitializerError("Failed to initialize database connection pool: " + e.getMessage());
        }
    }
    
    /**
     * Initialize the database connection pool
     */
    private static void initializeDataSource() throws IOException {
        Properties props = loadDatabaseProperties();
        
        dataSource = new BasicDataSource();
        
        // Basic connection properties
        dataSource.setDriverClassName(props.getProperty("db.driver"));
        dataSource.setUrl(props.getProperty("db.url"));
        dataSource.setUsername(props.getProperty("db.username"));
        dataSource.setPassword(props.getProperty("db.password"));
        
        // Connection pool configuration
        dataSource.setInitialSize(Integer.parseInt(props.getProperty("db.initialSize", "5")));
        dataSource.setMaxTotal(Integer.parseInt(props.getProperty("db.maxTotal", "10")));
        dataSource.setMaxIdle(Integer.parseInt(props.getProperty("db.maxIdle", "5")));
        dataSource.setMinIdle(Integer.parseInt(props.getProperty("db.minIdle", "2")));
        dataSource.setMaxWaitMillis(Long.parseLong(props.getProperty("db.maxWaitMillis", "10000")));
        
        // Connection validation
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestOnBorrow(true);
        dataSource.setTestWhileIdle(true);
        dataSource.setTimeBetweenEvictionRunsMillis(30000);
        
        // Connection timeout settings
        dataSource.setRemoveAbandonedOnBorrow(true);
        dataSource.setRemoveAbandonedTimeout(60);
        dataSource.setLogAbandoned(true);
    }
    
    /**
     * Load database properties from configuration file
     */
    private static Properties loadDatabaseProperties() throws IOException {
        Properties props = new Properties();
        
        try (InputStream input = DatabaseConnection.class.getClassLoader()
                .getResourceAsStream("database.properties")) {
            
            if (input == null) {
                throw new IOException("Unable to find database.properties file");
            }
            
            props.load(input);
            logger.info("Database properties loaded successfully");
            
        } catch (IOException e) {
            logger.error("Error loading database properties", e);
            throw e;
        }
        
        return props;
    }
    
    /**
     * Get a connection from the pool
     * @return Connection object
     * @throws SQLException if unable to get connection
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource is not initialized");
        }
        
        Connection conn = dataSource.getConnection();
        logger.debug("Connection obtained from pool. Active connections: {}", dataSource.getNumActive());
        return conn;
    }
    
    /**
     * Close connection (returns it to the pool)
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                logger.debug("Connection returned to pool. Active connections: {}", dataSource.getNumActive());
            } catch (SQLException e) {
                logger.error("Error closing connection", e);
            }
        }
    }
    
    /**
     * Close the entire connection pool (should be called on application shutdown)
     */
    public static void closePool() {
        if (dataSource != null) {
            try {
                dataSource.close();
                logger.info("Database connection pool closed successfully");
            } catch (SQLException e) {
                logger.error("Error closing connection pool", e);
            }
        }
    }
    
    /**
     * Get connection pool statistics
     * @return String containing pool statistics
     */
    public static String getPoolStats() {
        if (dataSource == null) {
            return "DataSource not initialized";
        }
        
        return String.format(
            "Connection Pool Stats - Active: %d, Idle: %d, Max: %d",
            dataSource.getNumActive(),
            dataSource.getNumIdle(),
            dataSource.getMaxTotal()
        );
    }
    
    /**
     * Test database connection
     * @return true if connection successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            boolean isValid = conn.isValid(5);
            if (isValid) {
                logger.info("Database connection test successful");
            } else {
                logger.warn("Database connection test failed");
            }
            return isValid;
        } catch (SQLException e) {
            logger.error("Database connection test failed", e);
            return false;
        }
    }
}
