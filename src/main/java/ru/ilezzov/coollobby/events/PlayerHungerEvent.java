package ru.ilezzov.coollobby.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import ru.ilezzov.coollobby.Main;

public class PlayerHungerEvent implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onFoodLevelChangeEvent(final FoodLevelChangeEvent event) {
        if (!Main.getConfigFile().getBoolean("lobby_settings.disable_hunger")) {
            return;
        }

        if (Main.getWorldManager().getWorld().getUID() == event.getEntity().getWorld().getUID()) {
            event.setCancelled(true);
        }
    }
}
