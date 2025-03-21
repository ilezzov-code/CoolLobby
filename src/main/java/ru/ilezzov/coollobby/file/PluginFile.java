package ru.ilezzov.coollobby.file;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;
import ru.ilezzov.coollobby.Main;

import java.io.*;
import java.util.List;
import java.util.Map;

public class PluginFile {
    private final JavaPlugin plugin;
    private final String fileName;
    private File file;

    @Getter
    private FileConfiguration config;
    @Getter
    private String configVersion;

    public PluginFile(final JavaPlugin plugin, final String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;

        createConfig();
        configVersion = config.getString("config_version");
        checkVersion();
    }

    private void createConfig() {
        file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(fileName, false);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    private void checkVersion() {
        try (final InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName)) {

            if (inputStream == null) {
                return;
            }

            final YamlConfiguration defaultConfigFile = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream));
            final String defaultConfigFileVersion = defaultConfigFile.getString("config_version", "1.0");

            if (equalsVersion(defaultConfigFileVersion, configVersion) == 1) {
                addMissingKeys(defaultConfigFile);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addMissingKeys(final YamlConfiguration defaultConfigFile) {
        boolean isUpdate = false;

        for (final String key : defaultConfigFile.getKeys(true)) {
            if (config.contains(key)) {
               continue;
            }
            getConfig().set(key, defaultConfigFile.get(key));
            isUpdate = true;
        }

        if (isUpdate) {
            getConfig().set("config_version", defaultConfigFile.get("config_version"));
            saveConfig();
        }
    }

    private int equalsVersion(final String version1, final String version2) {
        final String[] version1Split = version1.split("\\.");
        final String[] version2Split = version2.split("\\.");

        final int maxLength = Math.max(version1Split.length, version2Split.length);

        for (int i = 0; i < maxLength; i++) {
            final int num1 = i < version1Split.length ? Integer.parseInt(version1Split[i]) : 0;
            final int num2 = i < version2Split.length ? Integer.parseInt(version2Split[i]) : 0;

            if (num1 < num2) {
                return -1; // version1 < version2
            }

            if (num1 > num2) {
                return 1; // version1 > version2
            }
        }

        return 0; // version1 == version2
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
        }
    }

    public void reloadConfig() {
        createConfig();
        config = YamlConfiguration.loadConfiguration(file);
    }

    public String getString(final String key) {
        return config.getString(key);
    }

    public int getInt(final String key) {
        return config.getInt(key);
    }

    public double getDouble(final String key) {
        return config.getDouble(key);
    }

    public long getLong(final String key) {
        return config.getLong(key);
    }

    public boolean getBoolean(final String key) {
        return config.getBoolean(key);
    }

    public List<?> getList(final String key) {
        return config.getList(key);
    }
}
