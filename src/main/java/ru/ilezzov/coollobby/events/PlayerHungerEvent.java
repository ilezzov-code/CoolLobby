package ru.ilezzov.coollobby.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import ru.ilezzov.coollobby.Main;

public class PlayerHungerEvent implements Listener {
    @EventHandler
    public void onPlayerHungerEvent(final FoodLevelChangeEvent event) {
        if (!Main.getLobbyManager().isLobby(event.getEntity().getWorld())) {
            return;
        }

        event.setCancelled(true);
    }
}
