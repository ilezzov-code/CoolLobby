package ru.ilezzov.coollobby.events;

import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.events.listeners.*;
import ru.ilezzov.coollobby.file.PluginFile;
import ru.ilezzov.coollobby.logging.Logger;

import java.util.HashMap;
import java.util.Map;

import static ru.ilezzov.coollobby.messages.ConsoleMessages.*;

public class EventManager {
    private final Logger logger = Main.getPluginLogger();
    private final JavaPlugin plugin;
    private Map<Class<? extends Listener>, Boolean> listenerClasses;
    private Map<String, Object> events;

    public EventManager(final JavaPlugin plugin) {
        this.plugin = plugin;
        this.events = new HashMap<>();

        listenerClasses = loadListenerClasses();
    }

    public Object get(final String eventName) {
        return events.get(eventName);
    }

    public void loadEvents() {
        final Map<String, Object> events = new HashMap<>();

        for (Class<? extends Listener> listenerClass : listenerClasses.keySet()) {
            try {
                if (listenerClasses.get(listenerClass)) {
                    final Listener listener = listenerClass.getDeclaredConstructor().newInstance();
                    Bukkit.getPluginManager().registerEvents(listener, plugin);
                    events.put(listenerClass.getSimpleName(), listener);
                    logger.info(eventLoaded(listenerClass.getSimpleName()));
                }
            } catch (Exception e) {
                logger.info(errorOccurred(String.format("Couldn't load event %s", listenerClass.getSimpleName())));
            }
        }

        this.events = events;
    }

    public void reloadEvents() {
        HandlerList.unregisterAll(plugin);
        listenerClasses = loadListenerClasses();
        loadEvents();
    }

    private Map<Class<? extends Listener>, Boolean> loadListenerClasses() {
        final FileConfiguration config = Main.getConfigFile().getConfig();
        final ConfigurationSection lobbySettings = config.getConfigurationSection("lobby_settings");

        return Map.ofEntries(
                Map.entry(VersionCheckEvent.class, true),
                Map.entry(PlayerWorldChangeEvent.class, true),
                Map.entry(PlayerJoinEvent.class, true),
                Map.entry(PlayerLeaveEvent.class, true),
                Map.entry(DamageEvent.class, lobbySettings.getBoolean("disable_damage", false)),
                Map.entry(HungerEvent.class, lobbySettings.getBoolean("disable_hunger", false)),
                Map.entry(TimeChangeEvent.class, lobbySettings.getBoolean("disable_daylight_cycle.enable", false)),
                Map.entry(EntitySpawnEvent.class, lobbySettings.getBoolean("disable_mob_spawning", false)),
                Map.entry(FireTickEvent.class, lobbySettings.getBoolean("disable_fire_tick", false)),
                Map.entry(WeatherChangeEvent.class, lobbySettings.getBoolean("disable_weather_cycle", false)),
                Map.entry(DoubleJumpEvent.class, config.getBoolean("double_jump.enable"))
        );
    }
}
