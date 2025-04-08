package ru.ilezzov.coollobby.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.enums.Permission;
import ru.ilezzov.coollobby.messages.ConsoleMessages;
import ru.ilezzov.coollobby.models.PluginPlayer;
import ru.ilezzov.coollobby.utils.PermissionsChecker;


import java.sql.SQLException;

public class PlayerChangeWorldEvent implements Listener {
    private final boolean enableDefaultGamemode = Main.getConfigFile().getBoolean("lobby_settings.default_gamemode.enable");
    private final GameMode defaultGamemode = getDefaultGamemode();

    private final boolean enableDefaultLevel = Main.getConfigFile().getBoolean("lobby_settings.default_level.enable");
    private final int expLevel = getDefaultLevel();

    private final boolean flyIsOnlyLobby = Main.getConfigFile().getBoolean("fly_command.only_lobby");

    @EventHandler
    public void onPlayerChangeWorldEvent(PlayerChangedWorldEvent event) {
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

        if (Main.getLobbyManager().isLobby(event.getFrom())) {
            if (Main.getLobbyManager().isLobby(player.getWorld())) {
                return 2; // From Lobby to Lobby
            }
            return 1; // From Lobby to World
        }

        if (Main.getLobbyManager().isLobby(player.getWorld())) {
            if (Main.getLobbyManager().isLobby(event.getFrom())) {
                return 2; // From Lobby to Lobby
            }
            return 0; /// From World to Lobby
        }

        return -1; //Other teleports
    }

    private void teleportFromLobbyToLobby(final Player player) {
        setLobbyPlayerSettings(player);
    }

    private void teleportFromLobbyToWorld(final Player player) {
        try {
            if (!Main.getDbConnect().checkUser(player.getUniqueId())) {
                Main.getDbConnect().insertUser(player);
                return;
            }

            final PluginPlayer myPlayer = Main.getDbConnect().getPlayer(player);

            player.setGameMode(myPlayer.getGameMode());
            player.setLevel(myPlayer.getLevel());
            player.setFoodLevel(myPlayer.getFoodLevel());
            player.setExp(myPlayer.getExp());

            setFlyMode(player);

        } catch (SQLException e) {
            Main.getPluginLogger().info(ConsoleMessages.errorOccurred("Не удалось выполнить запрос к базе данных ".concat(e.getMessage())));
        }
    }

    private void teleportFromWorldToLobby(final Player player) {
        try {
            if (!Main.getDbConnect().checkUser(player.getUniqueId())) {
                Main.getDbConnect().insertUser(player);
            } else {
                Main.getDbConnect().updateUser(player);
            }

            setLobbyPlayerSettings(player);

            if (PermissionsChecker.hasPermission(player, Permission.FLY_COMMAND)) {
                player.setAllowFlight(Main.getFlyManager().isAllowFlight(player.getUniqueId()));
            }

        } catch (SQLException e) {
            Main.getPluginLogger().info(ConsoleMessages.errorOccurred("Couldn't send a request to the database: ".concat(e.getMessage())));
        }
    }

    private GameMode getDefaultGamemode() {
        if (!enableDefaultGamemode) {
            return null;
        }

        final String gamemodeType = Main.getConfigFile().getString("lobby_settings.default_gamemode.type");

        return switch (gamemodeType.toLowerCase()) {
            case "survival" -> GameMode.SURVIVAL;
            case "creative" -> GameMode.CREATIVE;
            case "adventure" -> GameMode.ADVENTURE;
            case "spectator" -> GameMode.SPECTATOR;
            default -> Bukkit.getDefaultGameMode();
        };
    }

    private int getDefaultLevel() {
        if (enableDefaultLevel) {
            return Main.getConfigFile().getInt("lobby_settings.default_level.level");
        }
        return -1;
    }

    private void setLobbyPlayerSettings(final Player player) {
        Main.getApi().setFoodLevel(player, 20);
        Main.getApi().setGameMode(player, defaultGamemode);
        Main.getApi().setLevel(player, expLevel);
        player.setExp(0);
    }

    private void setFlyMode(final Player player) {
        if (!flyIsOnlyLobby) {
            if (PermissionsChecker.hasPermission(player, Permission.FLY_COMMAND)) {
                player.setAllowFlight(Main.getFlyManager().isAllowFlight(player.getUniqueId()));
            }
            return;
        }
        if (player.getGameMode() != GameMode.CREATIVE) {
            Main.getFlyManager().addPlayer(player.getUniqueId(), false);
            player.setAllowFlight(false);
        }
    }
}
