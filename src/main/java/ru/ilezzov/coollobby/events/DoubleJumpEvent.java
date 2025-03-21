package ru.ilezzov.coollobby.events;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import ru.ilezzov.coollobby.utils.Permissions;

import java.net.http.WebSocket;
import java.util.HashMap;
import java.util.UUID;

public class DoubleJampEvent implements WebSocket.Listener {
    public static HashMap<UUID, Boolean> flyPlayerChecker = new HashMap<>();

    @EventHandler
    public void playerToggleFly(final PlayerToggleFlightEvent event) {
        final Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if (flyPlayerChecker.get(player.getUniqueId())) {
            return;
        }

        if (!Permissions.hasPermission(player, Permissions.DOUBLE_JUMP_PERMISSION)) {
            return;
        }

        event.setCancelled(true);
    }

    private void setVelocity(final Player player) {
        
    }

}
