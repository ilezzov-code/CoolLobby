package ru.ilezzov.pluginBlank.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.ilezzov.pluginBlank.Main;
import ru.ilezzov.pluginBlank.messages.PluginMessages;
import ru.ilezzov.pluginBlank.utils.Permissions;

import java.util.HashMap;

public class VersionCheckEvent implements Listener {

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        if (Main.getPluginConfig().getBoolean("check_updates")) {
            final Player player = event.getPlayer();

            if(Main.isOutdatedVersion()) {
                if (Permissions.hasPermission(player, Permissions.MAIN_PERMISSIONS)) {
                    final HashMap<String, String> eventPlaceholders = new HashMap<>();
                    eventPlaceholders.put("{P}", Main.getPrefix());
                    eventPlaceholders.put("{OUTDATED_VERS}", Main.getPluginVersion());
                    eventPlaceholders.put("{LATEST_VERS}", Main.getVersionManager().getCurrentPluginVersion());
                    eventPlaceholders.put("{DOWNLOAD_LINK}", Main.getUrlToDownloadLatestVersion());

                    player.sendMessage(PluginMessages.pluginOutdatedVersionMessage(eventPlaceholders));
                }
            }
        }
    }
}
