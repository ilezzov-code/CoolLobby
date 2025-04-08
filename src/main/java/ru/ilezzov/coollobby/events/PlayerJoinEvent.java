package ru.ilezzov.coollobby.events;

import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.messages.ConsoleMessages;
import ru.ilezzov.coollobby.messages.PluginMessages;
import ru.ilezzov.coollobby.models.PluginPlaceholder;

import java.sql.SQLException;
import java.time.Duration;

public class PlayerJoinEvent implements Listener {
    private final PluginPlaceholder eventPlaceholder = new PluginPlaceholder();

    private final boolean enableDefaultGamemode = Main.getConfigFile().getBoolean("lobby_settings.default_gamemode.enable");
    private final GameMode defaultGamemode = getDefaultGamemode();

    private final boolean enableDefaultLevel = Main.getConfigFile().getBoolean("lobby_settings.default_level.enable");
    private final int expLevel = getDefaultLevel();

    private final boolean enablePlayerJoinMessage = Main.getConfigFile().getBoolean("player_join.join_message.enable");
    private final boolean enablePlayerJoinGlobalMessage = Main.getConfigFile().getBoolean("player_join.global_join_message.enable");

    private final boolean enablePlayerJoinTitle = Main.getConfigFile().getBoolean("player_join.join_title.enable");
    private final int[] titleTimes = getTitleTimes();

    private final boolean enablePlayerJoinSound = Main.getConfigFile().getBoolean("player_join.join_sound.enable");
    private final Sound joinSound = getJoinSound();

    @EventHandler
    public void onPlayerLoginEvent(final PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        insertPlayer(player);
        Main.getFlyManager().addPlayer(player.getUniqueId(), player.getAllowFlight());
    }

    @EventHandler
    public void onPlayerJoinEvent(final org.bukkit.event.player.PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final World world = event.getPlayer().getWorld();;

        if (Main.getLobbyManager().isLobby(world)) {
            Main.getApi().setLevel(player, expLevel);
            Main.getApi().setGameMode(player, defaultGamemode);
            Main.getApi().setFoodLevel(player, 20);
        }

        eventPlaceholder.addPlaceholder("{NAME}", player.getName());

        sendJoinMessage(player);
        sendJoinTitle(player);
        sendJoinSound(player);

        event.setJoinMessage(null);
        sendGlobalJoinMessage();
    }

    private void insertPlayer(final Player player) {
        try {
            if (!Main.getDbConnect().checkUser(player.getUniqueId())) {
                Main.getDbConnect().insertUser(player);
            }
        } catch (SQLException e) {
            Main.getPluginLogger().info(ConsoleMessages.errorOccurred("Couldn't send a request to the database: " + e.getMessage()));
        }
    }

    private void sendGlobalJoinMessage() {
        if (!enablePlayerJoinGlobalMessage) {
            return;
        }

        Bukkit.broadcast(PluginMessages.playerJoinGlobalMessage(eventPlaceholder));
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
        player.sendMessage(PluginMessages.playerJoinMessage(eventPlaceholder));
    }

    private Sound getJoinSound() {
        return Sound.valueOf(Main.getConfigFile().getString("player_join.join_sound.sound"));
    }

    private int[] getTitleTimes() {
        int[] times = new int[3];

        times[0] = Main.getConfigFile().getInt("player_join.join_title.fade_in");
        times[1] = Main.getConfigFile().getInt("player_join.join_title.stay");
        times[2] = Main.getConfigFile().getInt("player_join.join_title.fade_out");

        return times;
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
}
