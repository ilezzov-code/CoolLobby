package ru.ilezzov.coollobby.events;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.messages.PluginMessages;
import ru.ilezzov.coollobby.models.DefaultPlaceholder;

import java.sql.SQLException;

import static ru.ilezzov.coollobby.Main.*;


public class PlayerLeaveEvent implements Listener {
    private final DefaultPlaceholder eventPlaceholders = new DefaultPlaceholder();

    @EventHandler
    public void onPlayerQuitEvent(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        eventPlaceholders.addPlaceholder("{NAME}", player.getName());

        sendGlobalLeaveMessage();
        updatePlayerData(player);
        setAllowFlight(player);
    }

    private void sendGlobalLeaveMessage() {
        if (Main.getConfigFile().getBoolean("player_leave.global_leave_message.enable")) {
            Bukkit.broadcast(PluginMessages.eventPlayerLeaveGlobalMessage(eventPlaceholders.getPlaceholders()));
        }
    }

    private void updatePlayerData(final Player player) {
        final World world = player.getWorld();

        if (Main.getWorldManager().getWorld().getUID() == world.getUID()) {
            return;
        }

        try {
            if (getDbConnect().checkUser(player.getUniqueId())) {
                getDbConnect().updateUser(player);
            }
        } catch (SQLException e) {
            eventPlaceholders.addPlaceholder("{ERROR}", e.getMessage());
            Main.getPluginLogger().info(PluginMessages.pluginHasErrorMessage(eventPlaceholders.getPlaceholders()));
        }
    }

    private void setAllowFlight(final Player player) {
        player.setAllowFlight(Main.getFlyManager().isAllowFlight(player.getUniqueId()));
    }


}
