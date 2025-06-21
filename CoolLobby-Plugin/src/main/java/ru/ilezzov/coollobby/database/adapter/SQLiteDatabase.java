package ru.ilezzov.coollobby.database.adapter;

import ru.ilezzov.coollobby.database.DatabaseType;
import ru.ilezzov.coollobby.database.SQLDatabase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class SQLiteDatabase implements SQLDatabase {
    private final String url;
    private final DatabaseType type;
    private Connection connection;

    public SQLiteDatabase(final String dbFilePath, final DatabaseType type) {
        this.url = "jdbc:sqlite:" + dbFilePath;
        this.type = type;
    }

    @Override
    public DatabaseType getType() {
        return this.type;
    }

    @Override
    public void connect() throws SQLException {
        if (!isConnected()) {
            this.connection = DriverManager.getConnection(url);
        }
    }

    @Override
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public void disconnect() throws SQLException {
        if (isConnected()) {
            connection.close();
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public ResultSet executeQuery(final String query) throws SQLException {
        if (!isConnected()) {
            throw new SQLException("Not connected to the database.");
        }

        final Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    @Override
    public int executeUpdate(final String query) throws SQLException {
        if (!isConnected()) {
            throw new SQLException("Not connected to the database.");
        }

        final Statement statement = connection.createStatement();
        return statement.executeUpdate(query);
    }

    @Override
    public ResultSet executePreparedQuery(final String query, final Object... parameters) throws SQLException {
        if (!isConnected()) {
            throw new SQLException("Not connected to the database.");
        }

        final PreparedStatement ps = connection.prepareStatement(query);
        bindParameters(ps, parameters);
        return ps.executeQuery();
    }

    @Override
    public int executePreparedUpdate(final String query, final Object... parameters) throws SQLException {
        if (!isConnected()) {
            throw new SQLException("Not connected to the database.");
        }

        final PreparedStatement ps = connection.prepareStatement(query);
        bindParameters(ps, parameters);
        return ps.executeUpdate();
    }

    @Override
    public int[] executeBatchUpdate(final Iterable<String> queries) throws SQLException {
        if (!isConnected()) throw new SQLException("Not connected to the database.");
        try (Statement statement = connection.createStatement()) {
            for (String query : queries) {
                statement.addBatch(query);
            }
            return statement.executeBatch();
        }
    }

    @Override
    public int[] executePreparedBatchUpdate(final String query, final Iterable<Object[]> batchParams) throws SQLException {
        if (!isConnected()) throw new SQLException("Not connected to the database.");
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (final Object[] params : batchParams) {
                bindParameters(ps, params);
                ps.addBatch();
            }
            return ps.executeBatch();
        }
    }

    @Override
    public void initialize() throws SQLException, IOException {
        runSchemaFile();
    }

    private void runSchemaFile() throws SQLException, IOException {
        try (final InputStream in = getClass().getClassLoader().getResourceAsStream("data/sqlite_schema.sql")) {
            if (in == null) {
                throw new FileNotFoundException("Schema file not found: " + "data/sqlite_schema.sql");
            }

            final String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);

            try (final Statement stmt = connection.createStatement()) {
                for (final String query : sql.split(";")) {
                    if (!query.trim().isEmpty()) {
                        stmt.execute(query.trim());
                    }
                }
            }
        }
    }

    private void bindParameters(final PreparedStatement statement, final Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            statement.setObject(i + 1, parameters[i]);
        }
    }
}
