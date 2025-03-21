package ru.ilezzov.coollobby.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.ilezzov.coollobby.messages.PluginMessages;

import java.util.HashMap;

import static ru.ilezzov.coollobby.Main.*;

public class PluginEvent implements Listener {

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        final HashMap<String, String> placeholders = new HashMap<>();
        final Player player = event.getPlayer();

        placeholders.put("{P}", getPrefix());
        placeholders.put("{NAME}", player.getName());
        player.sendMessage(PluginMessages.eventPlayerJoinMessage(placeholders));
    }
}
