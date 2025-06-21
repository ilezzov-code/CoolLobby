package ru.ilezzov.coollobby.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An interface for interacting with an SQL database.
 * Provides basic operations for connecting, querying, updating, and initializing the schema.
 */
public interface SQLDatabase {

    /**
    Gets the database type
     */
    DatabaseType getType();

    /**
     * Establishes a connection to the database.
     *
     * @throws SQLException if the connection could not be established
     */
    void connect() throws SQLException;

    /**
     * Checks whether the database connection is active.
     *
     * @return {@code true} if connected, {@code false} otherwise
     */
    boolean isConnected();

    /**
     * Closes the connection to the database.
     *
     * @throws SQLException if an error occurs while closing the connection
     */
    void disconnect() throws SQLException;

    /**
     * Returns the current {@link Connection} to the database.
     *
     * @return the current {@link Connection}, or {@code null} if not connected
     */
    Connection getConnection();

    /**
     * Executes a raw SQL query (e.g., SELECT) without parameters.
     *
     * @param query the SQL query string
     * @return a {@link ResultSet} containing the query results
     * @throws SQLException if an error occurs during query execution
     */
    ResultSet executeQuery(String query) throws SQLException;

    /**
     * Executes a raw SQL update (e.g., INSERT, UPDATE, DELETE) without parameters.
     *
     * @param query the SQL update statement
     * @return the number of affected rows
     * @throws SQLException if an error occurs during update execution
     */
    int executeUpdate(String query) throws SQLException;

    /**
     * Executes a parameterized SQL query using a {@link java.sql.PreparedStatement}.
     *
     * @param query      the SQL query with placeholders (e.g., {@code SELECT * FROM users WHERE id = ?})
     * @param parameters the parameters to bind to the placeholders
     * @return a {@link ResultSet} containing the query results
     * @throws SQLException if an error occurs during query execution
     */
    ResultSet executePreparedQuery(String query, Object... parameters) throws SQLException;

    /**
     * Executes a parameterized SQL update using a {@link java.sql.PreparedStatement}.
     *
     * @param query      the SQL update statement with placeholders (e.g., {@code INSERT INTO users(name) VALUES(?)})
     * @param parameters the parameters to bind to the placeholders
     * @return the number of affected rows
     * @throws SQLException if an error occurs during update execution
     */
    int executePreparedUpdate(String query, Object... parameters) throws SQLException;

    /**
     * Executes a batch of raw SQL update statements (e.g., multiple INSERT's) without parameters.
     *
     * @param queries a list of SQL statements
     * @return an array containing the number of affected rows for each statement
     * @throws SQLException if an error occurs during execution
     */
    int[] executeBatchUpdate(Iterable<String> queries) throws SQLException;

    /**
     * Executes a batch of parameterized SQL update statements (e.g., INSERT, UPDATE) using {@link java.sql.PreparedStatement}.
     *
     * @param query       the SQL statement with placeholders (e.g., {@code INSERT INTO users(name) VALUES(?)})
     * @param batchParams a list of parameter arrays, one per batch entry
     * @return an array containing the number of affected rows for each batch entry
     * @throws SQLException if an error occurs during execution
     */
    int[] executePreparedBatchUpdate(String query, Iterable<Object[]> batchParams) throws SQLException;

    /**
     * Initializes the database schema by creating necessary tables if they do not exist.
     *
     * @throws SQLException if an error occurs while creating tables
     */
    void initialize() throws SQLException, IOException;
}
