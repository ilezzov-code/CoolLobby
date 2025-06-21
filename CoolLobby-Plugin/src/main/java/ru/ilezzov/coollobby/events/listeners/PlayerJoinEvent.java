package ru.ilezzov.coollobby.events.listeners;

import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.api.CoolLobbyApi;
import ru.ilezzov.coollobby.database.data.player.PlayerData;
import ru.ilezzov.coollobby.database.data.player.PlayerDataRepository;
import ru.ilezzov.coollobby.managers.LobbyManager;
import ru.ilezzov.coollobby.messages.PluginMessages;
import ru.ilezzov.coollobby.placeholder.PluginPlaceholder;

import java.time.Duration;
import java.util.UUID;

public class PlayerJoinEvent implements Listener {
    private final PluginPlaceholder placeholder = new PluginPlaceholder();
    private final LobbyManager lobbyManager = Main.getLobbyManager();
    private final PlayerDataRepository playerDataRepository = Main.getPlayerDataRepository();

    private final FileConfiguration config = Main.getConfigFile().getConfig();

    private final ConfigurationSection playerJoinSettings = config.getConfigurationSection("player_join");
    private final boolean enablePlayerJoinMessage = playerJoinSettings.getBoolean("join_message.enable");
    private final boolean enablePlayerJoinGlobalMessage = playerJoinSettings.getBoolean("global_join_message.enable");

    private final boolean enablePlayerJoinTitle = playerJoinSettings.getBoolean("join_title.enable");
    private final int[] titleTimes = getTitleTimes();

    private final boolean enablePlayerJoinSound = playerJoinSettings.getBoolean("join_sound.enable");
    private final Sound joinSound = getJoinSound();

    private final boolean enableTeleportToSpawn = config.getBoolean("teleport_to_spawn");

    private final CoolLobbyApi api = Main.getApi();

    @EventHandler
    public void onPlayerJoinEvent(final org.bukkit.event.player.PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final World world = player.getWorld();

        playerDataRepository.get(player.getUniqueId()).thenAccept(data -> {
            if (data == null) {
                final PlayerData newData = getPlayerData(player);
                playerDataRepository.insert(player.getUniqueId(), newData);
            }
        });

        if (lobbyManager.isLobby(world)) {
            api.setPlayerLevel(player);
            api.setGamemode(player);
            api.setFoodLevel(player, 20);
        }

        placeholder.addPlaceholder("{NAME}", player.getName());

        sendJoinMessage(player);
        sendJoinTitle(player);
        sendJoinSound(player);

        sendGlobalJoinMessage(event);

        if (enableTeleportToSpawn) {
            api.teleportToSpawn(player);
        }
    }

    private void sendGlobalJoinMessage(final org.bukkit.event.player.PlayerJoinEvent event) {
        if (!enablePlayerJoinGlobalMessage) {
            return;
        }

        event.joinMessage(PluginMessages.playerJoinGlobalMessage(placeholder));
    }

    private void sendJoinSound(final Player player) {
        if (!enablePlayerJoinSound) {
            return;
        }
        player.playSound(player, joinSound, 1, 1);
    }

    private void sendJoinTitle(final Player player) {
        if (!enablePlayerJoinTitle) {
            return;
        }
        player.showTitle(Title.title(
                PluginMessages.playerJoinTitleTitle(),
                PluginMessages.playerJoinTitleSubtitle(),
                Title.Times.times(
                        Duration.ofSeconds(titleTimes[0]),
                        Duration.ofSeconds(titleTimes[1]),
                        Duration.ofSeconds(titleTimes[2])
                )
        ));
    }

    private void sendJoinMessage(final Player player) {
        if (!enablePlayerJoinMessage) {
            return;
        }
        player.sendMessage(PluginMessages.playerJoinMessage(placeholder));
    }


    private Sound getJoinSound() {
        return Sound.valueOf(playerJoinSettings.getString("join_sound.sound"));
    }

    private int[] getTitleTimes() {
        int[] times = new int[3];

        times[0] = playerJoinSettings.getInt("join_title.fade_in");
        times[1] = playerJoinSettings.getInt("join_title.stay");
        times[2] = playerJoinSettings.getInt("join_title.fade_out");

        return times;
    }

    private static @NotNull PlayerData getPlayerData(final Player player) {
        final UUID playerUniqueId = player.getUniqueId();
        final String playerName = player.getName();
        final GameMode gameMode = player.getGameMode();
        final int expLevel = player.getLevel();
        final float expLevelExp = player.getExp();
        final int foodLevel = player.getFoodLevel();
        final boolean isFly = player.getAllowFlight();

        return new PlayerData(
                playerUniqueId,
                playerName,
                gameMode,
                expLevel,
                expLevelExp,
                foodLevel,
                isFly
        );
    }
}


