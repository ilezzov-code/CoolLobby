package ru.ilezzov.coollobby.manager;

import lombok.Getter;
import org.bukkit.*;
import ru.ilezzov.coollobby.Main;

@Getter
public class WorldManager {
    private final World world;

    public WorldManager(final World world) {
        this.world = world;
    }

    public void setGamerules() {
        this.world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, !Main.getConfigFile().getBoolean("lobby_settings.disable_daylight_cycle"));
        this.world.setGameRule(GameRule.DO_MOB_SPAWNING, !Main.getConfigFile().getBoolean("lobby_settings.disable_mob_spawning"));
        this.world.setGameRule(GameRule.DO_FIRE_TICK, !Main.getConfigFile().getBoolean("lobby_settings.disable_fire_tick"));
        this.world.setGameRule(GameRule.DO_WEATHER_CYCLE, !Main.getConfigFile().getBoolean("lobby_settings.disable_weather_cycle"));
    }

    public void setDefaultTime() {
        if (Main.getConfigFile().getBoolean("lobby_settings.default_time.enable")) {
            this.world.setTime(Main.getConfigFile().getLong("lobby_settings.default_time.value"));
        }
    }

    public void setDefaultWeather() {
        if (Main.getConfigFile().getBoolean("lobby_settings.default_weather.enable")) {
            final String weatherType = Main.getConfigFile().getString("lobby_settings.default_weather.type");

            switch (weatherType.toLowerCase()) {
                case "rain" -> this.world.setStorm(true);
                case "thunder" -> this.world.setThundering(true);
                default -> {
                    this.world.setStorm(false);
                    this.world.setThundering(false);
                }
            }
        }
    }

    public boolean isEquals(final String worldName) {
        return this.world.getName().equalsIgnoreCase(worldName);
    }

}
