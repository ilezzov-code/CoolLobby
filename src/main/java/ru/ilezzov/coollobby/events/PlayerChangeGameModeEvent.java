package ru.ilezzov.coollobby.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

public class PlayerChangeGameModeEvent implements Listener {
    @EventHandler
    public void onPlayerChangeGameModeEvent(final PlayerGameModeChangeEvent event) {
        final Player player = event.getPlayer();

        // setAllowFly
    }
}
