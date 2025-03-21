package ru.ilezzov.coollobby.database;

import org.bukkit.entity.Player;
import ru.ilezzov.coollobby.models.MyPlayer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public interface DBConnection {
    void connect() throws SQLException;
    void checkTables() throws SQLException;
    void updateUser(final Player player) throws SQLException;
    void insertUser(final Player player) throws SQLException;
    void insertUser(final MyPlayer myPlayer) throws SQLException;

    boolean checkUser(final UUID playerUniqueId) throws SQLException;

    Connection getConnection() throws SQLException;

    MyPlayer getPlayer(final Player player) throws SQLException;
}
