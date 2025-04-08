package ru.ilezzov.coollobby.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.messages.ConsoleMessages;
import ru.ilezzov.coollobby.messages.PluginMessages;
import ru.ilezzov.coollobby.models.PluginPlaceholder;

import java.sql.SQLException;

public class PlayerLeaveEvent implements Listener {
    private final PluginPlaceholder eventPlaceholder = new PluginPlaceholder();

    private final boolean enablePlayerLeaveGlobalMessgae = Main.getConfigFile().getBoolean("player_leave.global_leave_message.enable");

    @EventHandler
    public void onPlayerLeaveEvent(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        eventPlaceholder.addPlaceholder("{NAME}", player.getName());

        event.setQuitMessage(null);
        sendGlobalLeaveMessage();
        updatePlayerData(player);
    }

    private void sendGlobalLeaveMessage() {
        if (!enablePlayerLeaveGlobalMessgae) {
            return;
        }

        Bukkit.broadcast(PluginMessages.playerLeaveGlobalMessage(eventPlaceholder));
    }

    private void updatePlayerData(final Player player) {
        if (Main.getLobbyManager().isLobby(player.getWorld())) {
            return;
        }

        try {
            if (!Main.getDbConnect().checkUser(player.getUniqueId())) {
                Main.getDbConnect().insertUser(player);
            } else {
                Main.getDbConnect().updateUser(player);
            }
        } catch (SQLException e) {
            Main.getPluginLogger().info(ConsoleMessages.errorOccurred("Couldn't send a request to the database: " + e.getMessage()));
        }
    }
}
