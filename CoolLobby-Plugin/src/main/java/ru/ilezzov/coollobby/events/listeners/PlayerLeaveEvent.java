package ru.ilezzov.coollobby.events.listeners;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.database.data.player.PlayerData;
import ru.ilezzov.coollobby.database.data.player.PlayerDataRepository;
import ru.ilezzov.coollobby.managers.LobbyManager;
import ru.ilezzov.coollobby.messages.PluginMessages;
import ru.ilezzov.coollobby.placeholder.PluginPlaceholder;

import java.util.UUID;

public class PlayerLeaveEvent implements Listener {
    private final PluginPlaceholder placeholder = new PluginPlaceholder();
    private final boolean enablePlayerLeaveGlobalMessage = Main.getConfigFile().getBoolean("player_leave.global_leave_message.enable");
    private final PlayerDataRepository playerDataRepository = Main.getPlayerDataRepository();
    private final LobbyManager lobbyManager = Main.getLobbyManager();

    @EventHandler
    public void onPlayerLeaveEvent(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        placeholder.addPlaceholder("{NAME}", player.getName());

        sendGlobalLeaveMessage(event);
        savePlayer(player);
    }


    private void sendGlobalLeaveMessage(final PlayerQuitEvent event) {
        if (!enablePlayerLeaveGlobalMessage) {
            return;
        }

        event.quitMessage(PluginMessages.playerLeaveGlobalMessage(placeholder));
    }

    private void savePlayer(final Player player) {
        final UUID uuid = player.getUniqueId();

        final GameMode mode = player.getGameMode();
        final int foodLevel = player.getFoodLevel();
        final int expLevel = player.getLevel();
        final float exp = player.getExp();
        final World world = player.getWorld();

        playerDataRepository.get(uuid).thenAccept(data -> {
            if (data == null) return;

            if (!lobbyManager.isLobby(world)) {
                data.setMode(mode);
                data.setFoodLevel(foodLevel);
                data.setExpLevel(expLevel);
                data.setExpLevelExp(exp);
            }

            playerDataRepository.queueSave(data, true);
        });
    }
}
