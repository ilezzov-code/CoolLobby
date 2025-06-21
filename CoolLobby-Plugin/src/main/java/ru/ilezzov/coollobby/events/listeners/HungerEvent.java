package ru.ilezzov.coollobby.events.listeners;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.managers.LobbyManager;

public class HungerEvent implements Listener {
    private final LobbyManager lobbyManager = Main.getLobbyManager();

    @EventHandler
    public void onFoodLevelChangeEvent(final FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        final World world = player.getWorld();
        if (!lobbyManager.isLobby(world)) {
            return;
        }

        event.setCancelled(true);
    }
}
