package ru.ilezzov.coollobby.events.listeners;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.managers.LobbyManager;

public class WeatherChangeEvent implements Listener {
    private final LobbyManager lobbyManager = Main.getLobbyManager();

    @EventHandler
    public void onWeatherChangeEvent(final org.bukkit.event.weather.WeatherChangeEvent event) {
        final World world = event.getWorld();

        if (!lobbyManager.isLobby(world)) {
            return;
        }

        if (event.getCause() != org.bukkit.event.weather.WeatherChangeEvent.Cause.PLUGIN) {
            event.setCancelled(true);
        }
    }
}
