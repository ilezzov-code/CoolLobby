package ru.ilezzov.coollobby.stats;

import org.bukkit.plugin.java.JavaPlugin;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.settings.PluginSettings;
import ru.ilezzov.coollobby.stats.bStats.Metrics;

public class PluginStats {
    private final JavaPlugin plugin;
    private final Metrics metrics;
    private final PluginSettings settings = Main.getPluginSettings();

    public PluginStats(final JavaPlugin plugin) {
        this.plugin = plugin;

        if (settings.isBstatsEnable()) {
            metrics = new Metrics(this.plugin, settings.getBstatsPluginId());

            // Language chart
            metrics.addCustomChart(new Metrics.SimplePie("used_languages", Main::getMessageLanguage));
        } else {
            metrics = null;
        }
    }
}
