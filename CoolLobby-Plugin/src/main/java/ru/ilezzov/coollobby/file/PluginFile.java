package ru.ilezzov.coollobby.file;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.logging.Logger;
import ru.ilezzov.coollobby.messages.ConsoleMessages;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class PluginFile {
    private final JavaPlugin plugin;
    private final String fileName;
    private final Logger LOGGER = Main.getPluginLogger();

    @Getter
    private FileConfiguration config;
    private File file;
    @Getter
    private String configVersion;

    public PluginFile(final JavaPlugin plugin, final String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;

        if (!createConfig()) {
            configVersion = config.getString("config_version");
            checkVersion();
        }
    }

    private boolean createConfig() {
        file = new File(plugin.getDataFolder(), fileName);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(fileName, false);

            LOGGER.info(ConsoleMessages.createNewFile(fileName));
            config = YamlConfiguration.loadConfiguration(file);
            return true;
        }

        config = YamlConfiguration.loadConfiguration(file);
        return false;
    }

    private void checkVersion() {
        try (final InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName)) {

            if (inputStream == null) {
                return;
            }

            final YamlConfiguration defaultConfigFile = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            final String defaultConfigFileVersion = defaultConfigFile.getString("config_version");

            if (defaultConfigFileVersion == null) {
                LOGGER.info("");
                return;
            }

            if (equalsVersion(defaultConfigFileVersion, configVersion) == 1) {
                addMissingKeys(defaultConfigFile);

                LOGGER.info(ConsoleMessages.addNewKeysToFile(fileName));
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
        } catch (IOException ignored) {
        }
    }

    public void reload() {
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

    public <T> List<T> getList(final String key, final Class<T> tClass) {
        List<?> rawList = config.getList(key);
        if (rawList == null) {
            return List.of(); // Возвращаем пустой список, если ключ не найден
        }

        return rawList.stream()
                .filter(tClass::isInstance) // Фильтруем элементы, чтобы они соответствовали классу
                .map(tClass::cast) // Преобразуем к указанному классу
                .collect(Collectors.toList());
    }
}
