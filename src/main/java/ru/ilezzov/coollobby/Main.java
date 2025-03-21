package ru.ilezzov.coollobby;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ru.ilezzov.coollobby.bStats.Metrics;
import ru.ilezzov.coollobby.commands.*;
import ru.ilezzov.coollobby.database.DBConnection;
import ru.ilezzov.coollobby.database.H2Connection;
import ru.ilezzov.coollobby.events.*;
import ru.ilezzov.coollobby.logging.Logger;
import ru.ilezzov.coollobby.logging.PaperLogger;
import ru.ilezzov.coollobby.manager.*;
import ru.ilezzov.coollobby.messages.ConsoleMessages;
import ru.ilezzov.coollobby.file.PluginFile;
import ru.ilezzov.coollobby.models.DefaultPlaceholder;
import ru.ilezzov.coollobby.utils.LegacySerialize;
import ru.ilezzov.coollobby.utils.ListUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import static org.bukkit.Bukkit.*;
import static ru.ilezzov.coollobby.messages.PluginMessages.*;

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
    private final String URL_TO_FILE_VERSION = "https://raw.githubusercontent.com/ilezzov-code/CoolLObby/main/VERSION";
    public static final String URL_TO_DOWNLOAD_LATEST_VERSION = "https://github.com/ilezzov-code/CoolLobby/releases";

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
    private final int BS_STATS_PLUGIN_ID = 25190;

    //Managers
    @Getter
    private static VersionManager versionManager;
    @Getter
    private static WorldManager worldManager;
    @Getter
    private static FlyManager flyManager;

    //Database
    @Getter
    private static DBConnection dbConnect;


    //Files
    @Getter
    private static PluginFile configFile;
    @Getter
    private static PluginFile messagesFile;
    @Getter
    private static PluginFile databaseFile;

    @Override
    public void onEnable() {
        instance = this;
        flyManager = new FlyManager();

        loadFiles();

        final HashMap<String, String> dbArgs = new HashMap<>();
        dbArgs.put("PATH", getDBFilePath("data", "database.db"));

        createDBConnection(databaseFile.getString("database.type"), dbArgs);

        try {
            dbConnect.connect();
        } catch (SQLException e) {
            getPluginLogger().info("Couldn't connect to the database");
            throw new RuntimeException(e);
        }

        prefix = messagesFile.getString("Plugin.plugin-prefix");
        pluginVersion = this.getPluginMeta().getVersion();
        pluginDevelopers = this.getPluginMeta().getAuthors();
        pluginContactLink = this.getPluginMeta().getWebsite();

        final DefaultPlaceholder enablePlaceholders = new DefaultPlaceholder();

        if(configFile.getBoolean("check_updates")) {
            try {
                versionManager = new VersionManager(pluginVersion, URL_TO_FILE_VERSION);

                if (versionManager.check()) {
                    pluginLogger.info(pluginLatestVersionMessage(enablePlaceholders.getPlaceholders()));
                    outdatedVersion = false;
                } else {
                    enablePlaceholders.addPlaceholder("{OUTDATED_VERS}", pluginVersion);
                    enablePlaceholders.addPlaceholder("{LATEST_VERS}", versionManager.getCurrentPluginVersion());
                    enablePlaceholders.addPlaceholder("{DOWNLOAD_LINK}", URL_TO_DOWNLOAD_LATEST_VERSION);

                    pluginLogger.info(pluginOutdatedVersionMessage(enablePlaceholders.getPlaceholders()));
                    outdatedVersion = true;
                }
            } catch (URISyntaxException e) {
                enablePlaceholders.addPlaceholder("{ERROR}", "Invalid link to the GitHub file. link = ".concat(versionManager.getUrlToFileVersion()));
                pluginLogger.info(pluginHasErrorMessageEnable(enablePlaceholders.getPlaceholders()));

                disablePlugin(Main.getInstance());
            } catch (IOException | InterruptedException e ) {
                enablePlaceholders.addPlaceholder("{ERROR}", "Couldn't send a request to get the plugin version");
                pluginLogger.info(pluginHasErrorMessageEnable(enablePlaceholders.getPlaceholders()));
            }
        }

        setWorldSettings();
        registerCommands();
        registerEvents();

        enablePlaceholders.addPlaceholder("{VERSION}", pluginVersion);
        enablePlaceholders.addPlaceholder("{DEVELOPER}", ListUtils.listToString(pluginDevelopers));

        DoubleJumpManager.start();

        Metrics metrics = new Metrics(this, BS_STATS_PLUGIN_ID);

        sendEnableMessage(enablePlaceholders.getPlaceholders());
    }

    @Override
    public void onDisable() {
        sendDisableMessage();
    }

    private void sendEnableMessage(final HashMap<String, String> enablePlaceholders) {
        for (final Component component : ConsoleMessages.pluginEnableMessage(enablePlaceholders)) {
            pluginLogger.info(component);
        }
    }

    private void sendDisableMessage() {
        for (final Component component : ConsoleMessages.pluginDisableMessage()) {
            pluginLogger.info(component);
        }
    }

    private String getDBFilePath(final String filePath, final String fileName) {
        return new File(Paths.get(this.getDataPath().toString(), filePath).toString(), fileName).getPath();
    }

    //Register your commands. Add a new command in plugin.yml to register her
    public static void registerCommands() {
        final PluginCommand mainCommand = Main.getInstance().getCommand("cool-lobby");

        if(mainCommand != null) {
            mainCommand.setExecutor(new MainCommand());
            mainCommand.setTabCompleter(new MainCommand());
        }

        Main.getInstance().getCommand("firework").setExecutor(new FireworkCommand());
        Main.getInstance().getCommand("lighting").setExecutor(new LightingCommand());
        Main.getInstance().getCommand("spit").setExecutor(new SpitCommand());
        Main.getInstance().getCommand("fly").setExecutor(new FlyCommand());
    }

    //Register your events. Add a new event class to register her
    private static void registerEvents() {
        getPluginManager().registerEvents(new PlayerJoinEvent(), Main.getInstance());
        getPluginManager().registerEvents(new VersionCheckEvent(), Main.getInstance());
        getPluginManager().registerEvents(new PlayerDamageEvent(), Main.getInstance());
        getPluginManager().registerEvents(new PlayerHungerEvent(), Main.getInstance());
        getPluginManager().registerEvents(new PlayerLeaveEvent(), Main.getInstance());
        getPluginManager().registerEvents(new PlayerChangeWorldEvent(), Main.getInstance());
        getPluginManager().registerEvents(new DoubleJumpEvent(), Main.getInstance());
        getPluginManager().registerEvents(new PlayerChangeGamemodeEvent(), Main.getInstance());
    }

    private void disablePlugin(final Plugin plugin) {
        getPluginManager().disablePlugin(plugin);
    }

    private void createMetrics() {
    }

    private void loadFiles() {
        configFile = new PluginFile(this, "config.yml");
        messagesFile = new PluginFile(this, "messages/".concat(configFile.getString("language").concat(".yml")));
        databaseFile = new PluginFile(this, "data/database.yml");

    }

    public static void reloadFiles() {
        configFile = new PluginFile(Main.getInstance(), "config.yml");
        messagesFile = new PluginFile(Main.getInstance(), "messages/".concat(configFile.getString("language").concat(".yml")));
        databaseFile = new PluginFile(Main.getInstance(), "data/database.yml");
    }

    public static void createDBConnection(final String dbType, final HashMap<String, String> dbArgs) {
        switch (dbType.toLowerCase()) {
            default -> dbConnect = new H2Connection(dbArgs.get("PATH"));
        }
    }

    public static void reloadPrefix() {
        prefix = messagesFile.getString("Plugin.plugin-prefix");
    }

    public static void reloadEvents() {
        HandlerList.unregisterAll();
        registerEvents();
    }

    public static void setWorldSettings() {
        worldManager = new WorldManager(Bukkit.getWorld(configFile.getString("lobby_settings.world")));

        worldManager.setDefaultTime();
        worldManager.setDefaultWeather();
        worldManager.setGamerules();
    }
}
