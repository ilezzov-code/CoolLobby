package ru.ilezzov.coollobby.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import ru.ilezzov.coollobby.Main;

public class PlayerChangeWorld implements Listener {
    @EventHandler
    public void playerChangeWorldEvent(final PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();
        final int isFromLobby = isFromLobby(event);

        if (isFromLobby == 1) {

        }

        if (isFromLobby == 0) {

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

    private void setDefaultGamemode(final Player player) {
        final GameMode gameMode = Bukkit.getDefaultGameMode();
        player.setGameMode(gameMode);
    }

    private void setLobbyGamemode(final Player player) {
        if (Main.getConfigFile().getBoolean("lobby_settings.default_gamemode.enable")) {
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
    }

}
