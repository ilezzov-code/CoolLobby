package ru.ilezzov.coollobby.events;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import ru.ilezzov.coollobby.Main;

public class PlayerDamageEvent implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDamageEvent(final EntityDamageEvent event) {
        if (!Main.getConfigFile().getBoolean("lobby_settings.disable_damage")) {
            return;
        }

        if (Main.getWorldManager().getWorld().getUID() != event.getEntity().getWorld().getUID()) {
            return;
        }

        if (event.getEntityType() == EntityType.PLAYER) {
            event.setCancelled(true);
        }
    }
}
