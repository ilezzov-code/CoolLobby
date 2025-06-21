package ru.ilezzov.coollobby.events.listeners;

import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.managers.LobbyManager;

public class TimeChangeEvent implements Listener {
    private final LobbyManager lobbyManager = Main.getLobbyManager();
    private BukkitTask dayTask;

    public TimeChangeEvent() {
        final ConfigurationSection defaultLobbyTime = Main.getConfigFile().getConfig().getConfigurationSection("lobby_settings.default_time");
        assert defaultLobbyTime != null;
        boolean useGamerule = Main.getConfigFile().getBoolean("lobby_settings.disable_daylight_cycle.use_gamerule");

        if (useGamerule) {
            lobbyManager.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        } else {
            dayTask = startDayTask(defaultLobbyTime.getLong("value"));
            lobbyManager.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        }
    }

    @EventHandler
    public void onTimeSkipEvent(final TimeSkipEvent event) {
        final World world = event.getWorld();

        if (!lobbyManager.isLobby(world)) {
            return;
        }

        if (event.getSkipReason() != TimeSkipEvent.SkipReason.CUSTOM) {
            event.setCancelled(true);
        }
    }

    private BukkitTask startDayTask(long time) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                lobbyManager.setTime(time);
            }
        }.runTaskTimer(Main.getInstance(), 0L, 20L);
    }

    public void stopDayTask() {
        if (dayTask == null) {
            return;
        }

        this.dayTask.cancel();
    }
}
