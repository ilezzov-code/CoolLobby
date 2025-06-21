package ru.ilezzov.coollobby.managers;

import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.World;
import ru.ilezzov.coollobby.utils.ListUtils;

import java.util.*;

public class LobbyManager {
    private final Map<UUID, World> worlds = new HashMap<>();

    public LobbyManager(final List<World> worlds) {
        addWorlds(worlds);
    }

    private void addWorlds(final List<World> worlds) {
        for (final World world : worlds) {
            this.worlds.put(world.getUID(), world);
        }
    }

    public boolean isLobby(final World world) {
        return worlds.containsKey(world.getUID());
    }

    public void setTime(final long time) {
        for (final World world : this.worlds.values()) {
            world.setTime(time);
        }
    }

    public void setWeather(final boolean rain, final boolean thundering) {
        for (final World world : this.worlds.values()) {
            world.setStorm(rain);
            world.setThundering(thundering);
        }
    }

    public void setGameRule(final GameRule<Boolean> gamerule, final boolean isEnable) {
        for (final World world : this.worlds.values()) {
            world.setGameRule(gamerule, isEnable);
        }
    }

    public String getLobbyWorldsName() {
        final Collection<World> worlds = this.worlds.values();

        if (worlds.isEmpty()) {
            return "none";
        }
        return ListUtils.worldsToString(worlds);
    }
}
