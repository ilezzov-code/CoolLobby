package ru.ilezzov.coollobby.manager;

import java.util.HashMap;
import java.util.UUID;

public class FlyManager {
    private final HashMap<UUID, Boolean> allowFlightPlayers;

    public FlyManager() {
        this.allowFlightPlayers = new HashMap<>();
    }

    public void addPlayer(final UUID uuid, boolean isAllowFlight) {
        this.allowFlightPlayers.put(uuid, isAllowFlight);
    }

    public boolean isAllowFlight(final UUID uuid) {
        if (allowFlightPlayers.containsKey(uuid)) {
            return allowFlightPlayers.get(uuid);
        }
        return false;
    }
}
