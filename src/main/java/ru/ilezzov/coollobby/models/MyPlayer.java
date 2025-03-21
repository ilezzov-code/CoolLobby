package ru.ilezzov.coollobby.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.GameMode;

import java.util.UUID;

@Data
@AllArgsConstructor
public class MyPlayer {
    private final UUID uniqueId;

    private int expLevel;
    private int foodLevel;

    private float expLevelExp;

    private GameMode gameMode;
}
