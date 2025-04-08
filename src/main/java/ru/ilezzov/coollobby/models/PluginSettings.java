package ru.ilezzov.coollobby.models;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PluginSettings {
    private final Yaml yaml = new Yaml();
    private YamlConfiguration file;

    @Getter
    private final String urlToFileVersion;
    @Getter
    private final String urlToDownloadLatestVersion;
    @Getter
    private final boolean bstatsEnable;
    @Getter
    private final int bstatsPluginId;

    public PluginSettings() throws IOException {
        loadSettings();

        urlToFileVersion = getString("Urls.url-to-file-version");
        urlToDownloadLatestVersion = getString("Urls.url-to-download-latest-version");
        bstatsEnable = getBoolean("Bstats.enable");
        bstatsPluginId = getInt("Bstats.plugin-id");
    }

    private void loadSettings() throws IOException {
        try (final InputStream inputStream = getClass().getClassLoader().getResourceAsStream("plugin-settings.yml")) {
            if (inputStream == null) {
                throw new RuntimeException("Settings.yml cannot be bull");
            }
            this.file = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream));
        }
    }

    public String getString(final String key) {
        return file.getString(key);
    }

    public int getInt(final String key) {
        return file.getInt(key);
    }

    public double getDouble(final String key) {
        return file.getDouble(key);
    }

    public long getLong(final String key) {
        return file.getLong(key);
    }

    public boolean getBoolean(final String key) {
        return file.getBoolean(key);
    }
}
