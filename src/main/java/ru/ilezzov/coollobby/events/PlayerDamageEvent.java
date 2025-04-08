package ru.ilezzov.coollobby.events;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import ru.ilezzov.coollobby.Main;

public class PlayerDamageEvent implements Listener {
    @EventHandler
    public void onPlayerDamageEvent(final EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }

        if (!Main.getLobbyManager().isLobby(event.getEntity().getWorld())) {
            return;
        }

        event.setCancelled(true);
    }
}
