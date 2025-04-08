package ru.ilezzov.coollobby.managers;

import org.bukkit.GameRule;
import org.bukkit.World;
import ru.ilezzov.coollobby.utils.ListUtils;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LobbyManager {
    private final HashMap<UUID, World> worlds;

    public LobbyManager(final List<World> worlds) {
        this.worlds = new HashMap<>();
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

    public void setGamerule(final GameRule<Boolean> gamerule, final boolean isEnable) {
        for (final World world : this.worlds.values()) {
            world.setGameRule(gamerule, isEnable);
        }
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

    public String getLobbyWorldsName() {
        return ListUtils.worldsToString(this.worlds.values());
    }
}
