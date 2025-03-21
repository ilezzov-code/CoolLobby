package ru.ilezzov.pluginBlank;

import lombok.Getter;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ru.ilezzov.pluginBlank.commands.MainCommand;
import ru.ilezzov.pluginBlank.events.PluginEvent;
import ru.ilezzov.pluginBlank.events.VersionCheckEvent;
import ru.ilezzov.pluginBlank.logging.Logger;
import ru.ilezzov.pluginBlank.logging.PaperLogger;
import ru.ilezzov.pluginBlank.manager.FileManager;
import ru.ilezzov.pluginBlank.manager.VersionManager;
import ru.ilezzov.pluginBlank.messages.PluginMessages;
import ru.ilezzov.pluginBlank.models.PluginFile;
import ru.ilezzov.pluginBlank.utils.LegacySerialize;
import ru.ilezzov.pluginBlank.utils.ListUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

import static org.bukkit.Bukkit.*;
import static ru.ilezzov.pluginBlank.messages.PluginMessages.*;

public final class Main extends JavaPlugin {
    //Serializer message color
    @Getter
    private static final LegacySerialize legacySerialize = new LegacySerialize();

    //Plugin logger
    @Getter
    private static final Logger pluginLogger = new PaperLogger();

    @Getter
    private static Main instance;

    //Plugin urls
    private final String urlToFileVersion = "https://raw.githubusercontent.com/ilezzov-code/Plugin-Blank-for-Paper/main/VERSION";
    @Getter
    private static final String urlToDownloadLatestVersion = "https://modrinth.com/plugin/plugin_blank";

    //Plugin info
    @Getter
    private static String prefix;
    @Getter
    private static String pluginVersion;
    @Getter
    private static String pluginContactLink;
    @Getter
    private static List<String> pluginDevelopers;
    @Getter
    private static boolean outdatedVersion;

    //Managers
    private static FileManager fileManager;
    @Getter
    private static VersionManager versionManager;

    //Files
    @Getter
    private static PluginFile pluginConfig;
    @Getter
    private static PluginFile messages;
    @Getter
    private static PluginFile database;

    @Override
    public void onEnable() {
        instance = this;

        fileManager = new FileManager();
        final HashMap<String, String> enablePlaceholders = new HashMap<>();

        try {
            loadFiles();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        prefix = messages.getString("prefix");
        pluginVersion = this.getPluginMeta().getVersion();
        pluginDevelopers = this.getPluginMeta().getAuthors();
        pluginContactLink = this.getPluginMeta().getWebsite();

        enablePlaceholders.put("{P}", prefix);

        if(pluginConfig.getBoolean("check_updates")) {
            try {
                versionManager = new VersionManager(pluginVersion, urlToFileVersion);

                if (versionManager.check()) {
                    pluginLogger.info(pluginLatestVersionMessage(enablePlaceholders));
                    outdatedVersion = false;
                } else {
                    enablePlaceholders.put("{OUTDATED_VERS}", pluginVersion);
                    enablePlaceholders.put("{LATEST_VERS}", versionManager.getCurrentPluginVersion());
                    enablePlaceholders.put("{DOWNLOAD_LINK}", urlToDownloadLatestVersion);
                    pluginLogger.info(pluginOutdatedVersionMessage(enablePlaceholders));
                    outdatedVersion = true;
                }
            } catch (URISyntaxException e) {
                enablePlaceholders.put("{ERROR}", "Invalid link to the GitHub file. link = ".concat(versionManager.getUrlToFileVersion()));
                pluginLogger.info(pluginHasErrorMessageEnable(enablePlaceholders));

                disablePlugin(this);
            } catch (IOException | InterruptedException e ) {
                enablePlaceholders.put("{ERROR}", "Couldn't send a request to get the plugin version");
                pluginLogger.info(pluginHasErrorMessageEnable(enablePlaceholders));
            }
        }

        registerCommands();
        registerEvents();

        enablePlaceholders.put("{VERSION}", pluginVersion);
        enablePlaceholders.put("{DEVELOPER}", ListUtils.listToString(pluginDevelopers));

        pluginLogger.info(pluginEnableMessage(enablePlaceholders));
    }

    @Override
    public void onDisable() {
        if(pluginConfig != null) {
            HashMap<String, String> enablePlaceholders = new HashMap<>();
            enablePlaceholders.put("{P}", prefix);

            pluginLogger.info(PluginMessages.pluginDisableMessage(enablePlaceholders));
        } else {
            pluginLogger.info("&cПлагин PluginBlank успешно выключен!");
        }
    }

    //Register your commands. Add a new command in plugin.yml to register her
    private void registerCommands() {
        final PluginCommand mainCommand = getCommand("plugin-blank");

        if(mainCommand != null) {
            mainCommand.setExecutor(new MainCommand());
            mainCommand.setTabCompleter(new MainCommand());
        }
    }

    //Register your events. Add a new event class to register her
    private void registerEvents() {
        getPluginManager().registerEvents(new PluginEvent(), this);
        getPluginManager().registerEvents(new VersionCheckEvent(), this);
    }

    private void disablePlugin(Plugin plugin) {
        getPluginManager().disablePlugin(plugin);
    }

    public static void loadFiles() throws IOException {
        pluginConfig = fileManager.newFile("config.yml", "");
        messages = fileManager.newFile(pluginConfig.getString("language").concat(".yml"), "/messages");
        database = fileManager.newFile("db-settings.yml", "/database");
    }

    public static void reloadPrefix() {
        prefix = messages.getString("prefix");
    }

}
