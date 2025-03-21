package ru.ilezzov.coollobby.events;

import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.messages.PluginMessages;
import ru.ilezzov.coollobby.models.DefaultPlaceholder;

import java.sql.SQLException;
import java.time.Duration;

import static ru.ilezzov.coollobby.Main.*;

public class PlayerJoinEvent implements Listener {
    private final DefaultPlaceholder eventPlaceholder = new DefaultPlaceholder();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLoginEvent(final PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        insertPlayer(player);
        Main.getFlyManager().addPlayer(player.getUniqueId(), player.getAllowFlight());
    }

    @EventHandler
    public void onPlayerJoinEvent(final org.bukkit.event.player.PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        
        eventPlaceholder.addPlaceholder("{NAME}", player.getName());

        setLobbyLevel(player);
        setLobbyGamemode(player);
        setFoodLevel(player);
        sendJoinMessage(player);
        sendJoinTitle(player);

        sendGlobalJoinMessage();
        sendJoinSound();
    }

    private void insertPlayer(final Player player) {
        try {
            if (!getDbConnect().checkUser(player.getUniqueId())) {
                getDbConnect().insertUser(player);
            }
        } catch (SQLException e) {
            eventPlaceholder.addPlaceholder("{ERROR}", e.getMessage());

            Main.getPluginLogger().info(PluginMessages.pluginHasErrorMessage(eventPlaceholder.getPlaceholders()));
        }
    }

    private void sendJoinMessage(final Player player) {
        if (Main.getConfigFile().getBoolean("player_join.join_message.enable")) {
            player.sendMessage(PluginMessages.eventPlayerJoinMessage(eventPlaceholder.getPlaceholders()));
        }
    }

    private void sendGlobalJoinMessage() {
        if (Main.getConfigFile().getBoolean("player_join.global_join_message.enable")) {
            Bukkit.broadcast(PluginMessages.eventPlayerJoinGlobalMessage(eventPlaceholder.getPlaceholders()));
        }
    }

    private void sendJoinTitle(final Player player) {
        if (Main.getConfigFile().getBoolean("player_join.join_title.enable")) {
            player.showTitle(Title.title(
                    PluginMessages.eventPlayerJoinTitleTitle(),
                    PluginMessages.eventPlayerJoinTitleSubtitle(),
                    Title.Times.times(
                            Duration.ofSeconds(Main.getConfigFile().getLong("player_join.join_title.fade_in")),
                            Duration.ofSeconds(Main.getConfigFile().getLong("player_join.join_title.stay")),
                            Duration.ofSeconds(Main.getConfigFile().getLong("player_join.join_title.fade_out"))
                    )));
        }
    }

    private void sendJoinSound() {
        if (Main.getConfigFile().getBoolean("player_join.join_sound.enable")) {
            final Sound sound = Sound.valueOf(Main.getConfigFile().getString("player_join.join_sound.sound"));

            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                playSoundGlobally(sound);
            });
        }
    }

    private void playSoundGlobally(final Sound sound) {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound, 1, 1);
        }
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
            player.setExp(0);
        }
    }

    private void setFoodLevel(final Player player) {
        player.setFoodLevel(20);
    }

}
