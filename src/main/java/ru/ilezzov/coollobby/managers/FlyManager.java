package ru.ilezzov.coollobby.managers;

import java.util.HashMap;
import java.util.UUID;

public class FlyManager {
    private final HashMap<UUID, Boolean> enableFlightPlayers;

    public FlyManager() {
        this.enableFlightPlayers = new HashMap<>();
    }

    public void addPlayer(final UUID uuid, final boolean isEnableFly) {
        this.enableFlightPlayers.put(uuid, isEnableFly);
    }

    public boolean isAllowFlight(final UUID uuid) {
        if (enableFlightPlayers.containsKey(uuid)) {
            return enableFlightPlayers.get(uuid);
        }
        return false;
    }
}
