package ru.ilezzov.coollobby.database;

public class Tables {
    public static final String playersTable = """
            CREATE TABLE IF NOT EXISTS players (
                uuid CHAR(36) PRIMARY KEY,
                gamemode TEXT CHECK(gamemode IN ('CREATIVE', 'SURVIVAL', 'ADVENTURE', 'SPECTATOR')),
                exp_level INT,
                exp_level_exp FLOAT,
                food_level INT
            )
            """;

}
