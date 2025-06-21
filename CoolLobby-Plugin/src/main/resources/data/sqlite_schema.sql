CREATE TABLE IF NOT EXISTS players (
    uuid CHAR(36) PRIMARY KEY,
    display_name VARCHAR(255) NOT NULL,
    game_mode TEXT CHECK(game_mode IN ('CREATIVE', 'SURVIVAL', 'ADVENTURE', 'SPECTATOR')),
    exp_level INTEGER NOT NULL DEFAULT 0,
    exp_level_exp REAL NOT NULL DEFAULT 0,
    food_level INTEGER NOT NULL DEFAULT 0,
    fly_mode BOOLEAN NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS server_spawn (
    name CHAR(255) PRIMARY KEY,
    world_name VARCHAR(255) NOT NULL,
    x REAL NOT NULL,
    y REAL NOT NULL,
    z REAL NOT NULL,
    pitch REAL NOT NULL,
    yaw REAL NOT NULL
);
