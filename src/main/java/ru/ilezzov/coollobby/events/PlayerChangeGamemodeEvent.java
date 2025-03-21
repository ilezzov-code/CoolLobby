package ru.ilezzov.coollobby.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import ru.ilezzov.coollobby.Main;

public class PlayerChangeGamemodeEvent implements Listener {

    @EventHandler
    public void onPlayerChangeGamemodeEvent(final PlayerGameModeChangeEvent event) {
            setAllowFlight(event.getPlayer());
    }

    private void setAllowFlight(final Player player) {
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> player.setAllowFlight(Main.getFlyManager().isAllowFlight(player.getUniqueId())), 5);
    }
}
