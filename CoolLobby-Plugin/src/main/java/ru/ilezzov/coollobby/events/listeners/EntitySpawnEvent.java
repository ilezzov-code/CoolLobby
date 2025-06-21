package ru.ilezzov.coollobby.events.listeners;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.managers.LobbyManager;

public class EntitySpawnEvent implements Listener {
    private final LobbyManager lobbyManager = Main.getLobbyManager();

    @EventHandler
    public void onCreatureSpawnEvent(final CreatureSpawnEvent event) {
        final World world = event.getEntity().getWorld();

        if (!lobbyManager.isLobby(world)) {
            return;
        }

        event.setCancelled(true);
    }
}
