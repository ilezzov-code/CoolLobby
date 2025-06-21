package ru.ilezzov.coollobby.events.listeners;

import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.scheduler.BukkitScheduler;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.api.CoolLobbyApi;
import ru.ilezzov.coollobby.database.data.player.PlayerData;
import ru.ilezzov.coollobby.database.data.player.PlayerDataRepository;
import ru.ilezzov.coollobby.managers.LobbyManager;

import java.util.UUID;

@Slf4j
public class PlayerWorldChangeEvent implements Listener {
    private final LobbyManager lobbyManager = Main.getLobbyManager();
    private final CoolLobbyApi api = Main.getApi();
    private final PlayerDataRepository playerDataRepository = Main.getPlayerDataRepository();
    private final boolean isEnableFlyCommand = Main.getConfigFile().getBoolean("fly_command.enable");
    private final boolean isOnlyLobbyFlyCommand = Main.getConfigFile().getBoolean("fly_command.only_lobby");

    @EventHandler
    public void onPlayerChangedWorldEvent(final PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();
        final int fromLobby = isFromLobby(event);

        if (fromLobby == 2) {
            teleportFromLobbyToLobby(player);
        }
        if (fromLobby == 1) {
            teleportFromLobbyToWorld(player);
        }
        if (fromLobby == 0) {
            teleportFromWorldToLobby(player);
        }
    }

    private int isFromLobby(final PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();
        final World currentWorld = player.getWorld();
        final World fromWorld = event.getFrom();

        if (lobbyManager.isLobby(fromWorld)) {
            if (lobbyManager.isLobby(currentWorld)) {
                return 2; // From Lobby to Lobby
            }
            return 1; // From Lobby to World
        }

        if (lobbyManager.isLobby(currentWorld)) {
            if (lobbyManager.isLobby(fromWorld)) {
                return 2; // From Lobby to Lobby
            }
            return 0; /// From World to Lobby
        }

        return -1; //Other teleports
    }

    private void teleportFromLobbyToLobby(final Player player) {
        api.setPlayerLevel(player);
        api.setGamemode(player);
        api.setFoodLevel(player, 20);
    }

    private void teleportFromWorldToLobby(final Player player) {
        final GameMode gameMode = player.getGameMode();
        final int expLevel = player.getLevel();
        final float expLevelExp = player.getExp();
        final int foodLevel = player.getFoodLevel();

        playerDataRepository.get(player.getUniqueId()).thenAccept(data -> {
            if (data == null) {
                return;
            }

            data.setMode(gameMode);
            data.setExpLevel(expLevel);
            data.setExpLevelExp(expLevelExp);
            data.setFoodLevel(foodLevel);

            Bukkit.getScheduler().runTask(Main.getInstance(), () -> teleportFromLobbyToLobby(player));
        });
    }

    private void teleportFromLobbyToWorld(final Player player) {
        final UUID uuid = player.getUniqueId();

        playerDataRepository.get(uuid).thenAccept(playerData -> {
            if (playerData == null) return;

            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                player.setGameMode(playerData.getMode());
                player.setLevel(playerData.getExpLevel());
                player.setExp(playerData.getExpLevelExp());
                player.setFoodLevel(playerData.getFoodLevel());
                player.setAllowFlight(player.getGameMode() == GameMode.CREATIVE);
            });
        });
    }
}
