package ru.ilezzov.coollobby.database;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import ru.ilezzov.coollobby.models.PluginPlayer;

import java.sql.*;
import java.util.UUID;

public class H2Connection implements DBConnection {
    private final String path;
    private final String URL_CONNECTION_TEMPLATE = "jdbc:sqlite:%s";

    private Connection connection;

    public H2Connection(final String path) {
        this.path = path;
    }

    @Override
    public void connect() throws SQLException {
        this.connection = DriverManager.getConnection(URL_CONNECTION_TEMPLATE.formatted(path));
        checkTables();
    }

    @Override
    public void checkTables() throws SQLException {
        this.connection.prepareStatement(Tables.playersTable).execute();
    }

    @Override
    public void updateUser(final Player player) throws SQLException {
        try (final PreparedStatement preparedStatement = this.connection.prepareStatement("UPDATE players SET gamemode = ?, exp_level = ?, exp_level_exp = ?, food_level = ? WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getGameMode().name());
            preparedStatement.setInt(2, player.getLevel());
            preparedStatement.setDouble(3, player.getExp());
            preparedStatement.setInt(4, player.getFoodLevel());
            preparedStatement.setObject(5, player.getUniqueId());

            preparedStatement.execute();
        }
    }

    @Override
    public void insertUser(final Player player) throws SQLException {
        try(final PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO players (uuid, gamemode, exp_level, exp_level_exp, food_level) VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setObject(1, player.getUniqueId());
            preparedStatement.setString(2, player.getGameMode().name());
            preparedStatement.setInt(3, player.getLevel());
            preparedStatement.setDouble(4, player.getExp());
            preparedStatement.setInt(5, player.getFoodLevel());

            preparedStatement.execute();
        }
    }

    @Override
    public void insertUser(final PluginPlayer myPlayer) throws SQLException {
        try(final PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO players (uuid, gamemode, exp_level, exp_level_exp, food_level) VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setObject(1, myPlayer.getUniqueId());
            preparedStatement.setString(2, myPlayer.getGameMode().name());
            preparedStatement.setInt(3, myPlayer.getLevel());
            preparedStatement.setDouble(4, myPlayer.getExp());
            preparedStatement.setInt(5, myPlayer.getFoodLevel());

            preparedStatement.execute();
        }
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    @Override
    public boolean checkUser(final UUID playerUniqueId) throws SQLException {
        try (final PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT COUNT(*) FROM players WHERE uuid = ?")) {
            preparedStatement.setObject(1, playerUniqueId);

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
            return false;
        }
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public PluginPlayer getPlayer(final Player player) throws SQLException {
        try (final PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT * FROM players WHERE uuid = ?")) {
            preparedStatement.setObject(1, player.getUniqueId());

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    return new PluginPlayer(
                            UUID.fromString(resultSet.getString("uuid")),
                            resultSet.getInt("exp_level"),
                            resultSet.getInt("food_level"),
                            resultSet.getFloat("exp_level_exp"),
                            GameMode.valueOf(resultSet.getString("gamemode"))
                    );
                }
            }
            return null;
        }
    }
}
