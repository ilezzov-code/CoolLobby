package ru.ilezzov.coollobby.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.messages.PluginMessages;
import ru.ilezzov.coollobby.models.DefaultPlaceholder;
import ru.ilezzov.coollobby.utils.PermissionsChecker;

public class VersionCheckEvent implements Listener {
    private final DefaultPlaceholder eventPlaceholders = new DefaultPlaceholder();
    private final boolean isEnable = (Main.getConfigFile().getBoolean("check_updates"));

    @EventHandler
    public void onPlayerJoinEvent(final PlayerJoinEvent event) {
        if (!isEnable) {
            return;
        }

        if (!Main.isOutdatedVersion()) {
            return;
        }

        final Player player = event.getPlayer();

        if (!PermissionsChecker.hasPermission(player)) {
            return;
        }

        eventPlaceholders.addPlaceholder("{OUTDATED_VERS}", Main.getPluginVersion());
        eventPlaceholders.addPlaceholder("{LATEST_VERS}", Main.getVersionManager().getCurrentPluginVersion());
        eventPlaceholders.addPlaceholder("{DOWNLOAD_LINK}", Main.URL_TO_DOWNLOAD_LATEST_VERSION);

        player.sendMessage(PluginMessages.pluginOutdatedVersionMessage(eventPlaceholders.getPlaceholders()));
    }
}
