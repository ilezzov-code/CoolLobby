package ru.ilezzov.coollobby.events;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.commands.FlyCommand;
import ru.ilezzov.coollobby.messages.PluginMessages;
import ru.ilezzov.coollobby.models.DefaultPlaceholder;
import ru.ilezzov.coollobby.models.MyPlayer;

import java.sql.SQLException;

public class PlayerChangeWorldEvent implements Listener {
    private final DefaultPlaceholder eventPlaceholders = new DefaultPlaceholder();

    @EventHandler
    public void onPlayerChangeWorldEvent(final PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();
        final int isFromLobby = isFromLobby(event);

        if (isFromLobby == 1) {
            teleportFromLobby(player);
        }

        if (isFromLobby == 0) {
            teleportToLobby(player);
        }
    }

    private void teleportFromLobby(final Player player) {
        try {
            if (!Main.getDbConnect().checkUser(player.getUniqueId())) {
                Main.getDbConnect().insertUser(player);
            }

            final MyPlayer myPlayer = Main.getDbConnect().getPlayer(player);

            player.setGameMode(myPlayer.getGameMode());
            player.setLevel(myPlayer.getExpLevel());
            player.setFoodLevel(myPlayer.getFoodLevel());
            player.setExp(myPlayer.getExpLevelExp());
            setAllowFlight(player);

        } catch (SQLException e) {
            eventPlaceholders.addPlaceholder("{ERROR}", e.getMessage());

            Main.getPluginLogger().info(PluginMessages.pluginHasErrorMessage(eventPlaceholders.getPlaceholders()));
        }
    }

    private void teleportToLobby(final Player player) {
        try {
            if (!Main.getDbConnect().checkUser(player.getUniqueId())) {
                Main.getDbConnect().insertUser(player);
            } else {
                Main.getDbConnect().updateUser(player);
            }

            setFoodLevel(player);
            setLobbyGamemode(player);
            setLobbyLevel(player);
            player.setExp(0);
            player.setAllowFlight(true);

        } catch (SQLException e) {
            eventPlaceholders.addPlaceholder("{ERROR}", e.getMessage());

            Main.getPluginLogger().info(PluginMessages.pluginHasErrorMessage(eventPlaceholders.getPlaceholders()));
        }
    }

    private int isFromLobby(final PlayerChangedWorldEvent event) {
        if (Main.getWorldManager().getWorld().getUID() == event.getFrom().getUID()) {
            return 1; //Teleport from lobby
        }

        if (Main.getWorldManager().getWorld().getUID() == event.getPlayer().getWorld().getUID()) {
            return 0; //Teleport to lobby
        }

        return -1; //Other teleports
    }

    private void setLobbyGamemode(final Player player) {
        if (!Main.getConfigFile().getBoolean("lobby_settings.default_gamemode.enable")) {
            return;
        }

        final World world = player.getWorld();

        if (Main.getWorldManager().getWorld().getUID() == world.getUID()) {
            final String gamemodeType = Main.getConfigFile().getString("lobby_settings.default_gamemode.type");

            switch (gamemodeType.toLowerCase()) {
                case "creative" -> player.setGameMode(GameMode.CREATIVE);
                case "spectator" -> player.setGameMode(GameMode.SPECTATOR);
                case "adventure" -> player.setGameMode(GameMode.ADVENTURE);
                default -> player.setGameMode(GameMode.SURVIVAL);
            }
        }
    }

    private void setLobbyLevel(final Player player) {
        if (!Main.getConfigFile().getBoolean("lobby_settings.default_level.enable")) {
            return;
        }

        final String worldName = player.getWorld().getName();

        if (Main.getWorldManager().isEquals(worldName)) {
            final int levelValue = Main.getConfigFile().getInt("lobby_settings.default_level.level");

            player.setLevel(levelValue);
        }
    }

    private void setFoodLevel(final Player player) {
        player.setFoodLevel(20);
    }

    private void setAllowFlight(final Player player) {
        if (!Main.getConfigFile().getBoolean("fly_command.only_lobby")) {
            player.setAllowFlight(Main.getFlyManager().isAllowFlight(player.getUniqueId()));

            return;
        }
        player.setAllowFlight(false);
        Main.getFlyManager().addPlayer(player.getUniqueId(), false);
    }
}
