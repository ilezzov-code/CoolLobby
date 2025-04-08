package ru.ilezzov.coollobby.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.GameMode;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PluginPlayer {
    private final UUID uniqueId;
    private int level;
    private int foodLevel;
    private float exp;
    private GameMode gameMode;
}
