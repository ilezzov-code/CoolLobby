package ru.ilezzov.coollobby;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import ru.ilezzov.coollobby.api.CoolLobbyApi;
import ru.ilezzov.coollobby.command.*;
import ru.ilezzov.coollobby.database.DBConnection;
import ru.ilezzov.coollobby.database.H2Connection;
import ru.ilezzov.coollobby.events.*;
import ru.ilezzov.coollobby.managers.DoubleJumpManager;
import ru.ilezzov.coollobby.managers.FlyManager;
import ru.ilezzov.coollobby.models.PluginFile;
import ru.ilezzov.coollobby.logging.Logger;
import ru.ilezzov.coollobby.logging.PaperLogger;
import ru.ilezzov.coollobby.managers.VersionManager;
import ru.ilezzov.coollobby.messages.ConsoleMessages;
import ru.ilezzov.coollobby.models.PluginSettings;
import ru.ilezzov.coollobby.utils.ListUtils;
import ru.ilezzov.coollobby.managers.LobbyManager;
import ru.ilezzov.coollobby.utils.Metrics;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public final class Main extends JavaPlugin {
    // Plugin instance
    @Getter
    private static Main instance;

    // Logger
    @Getter
    private static Logger pluginLogger;

    // Settings
    @Getter
    private static PluginSettings pluginSettings;

    // Plugin info
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

    // Files
    @Getter
    private static PluginFile configFile;
    @Getter
    private static PluginFile messagesFile;
    @Getter
    private static PluginFile databaseFile;

    // Api
    @Getter
    private static CoolLobbyApi api;

    // Managers
    @Getter
    private static VersionManager versionManager;
    @Getter
    private static LobbyManager lobbyManager;
    @Getter
    private static FlyManager flyManager;
    @Getter
    private static DoubleJumpManager doubleJumpManager;

    // Database
    @Getter
    private static DBConnection dbConnect;

    @Override
    public void onEnable() {
        instance = this;

        pluginLogger = new PaperLogger(this);
        flyManager = new FlyManager();

        // Load plugin files
        loadSettings();
        loadFiles();

        // Load plugin api
        loadApi();

        // Connect to Database
        setDbConnect();

        // Set plugin info
        loadPluginInfo();

        // Check Plugin Version
        checkPluginVersion();

        // Set lobby settings
        setLobbySettings();

        // Register command and events
        registerCommands();
        registerEvents();

        doubleJumpManager = new DoubleJumpManager();

        // Connect to Bstats
        createBstatsMetrics();

        sendEnableMessage();
    }

    private void disablePlugin(final Main plugin) {
        Bukkit.getPluginManager().disablePlugin(plugin);
    }

    @Override
    public void onDisable() {

        if (dbConnect != null) {
            try {
                dbConnect.close();
            } catch (SQLException e) {
                getPluginLogger().info(ConsoleMessages.errorOccurred("Couldn't close database connect: " + e.getMessage()));
                throw new RuntimeException(e);
            }
        }

        doubleJumpManager.stop();
        sendDisableMessage();
    }

    public static void loadFiles() {
        configFile = new PluginFile(Main.getInstance(), "config.yml");
        messagesFile = new PluginFile(Main.getInstance(), "messages/".concat(configFile.getString("language").concat(".yml")));
        databaseFile = new PluginFile(Main.getInstance(), "data/database.yml");
    }

    public static void registerCommands() {
        final PluginCommand mainCommand = Main.getInstance().getCommand("cool-lobby");

        if(mainCommand != null) {
            mainCommand.setExecutor(new MainCommand());
            mainCommand.setTabCompleter(new MainCommand());
        }

        Objects.requireNonNull(Main.getInstance().getCommand("firework")).setExecutor(new FireworkCommand());
        Objects.requireNonNull(Main.getInstance().getCommand("lighting")).setExecutor(new LightingCommand());
        Objects.requireNonNull(Main.getInstance().getCommand("spit")).setExecutor(new SpitCommand());
        Objects.requireNonNull(Main.getInstance().getCommand("fly")).setExecutor(new FlyCommand());

    }

    public static void reloadPrefix() {
        prefix = getMessagesFile().getString("Plugin.plugin-prefix");
    }

    public static void reloadEvents() {
        HandlerList.unregisterAll();
        registerEvents();
    }

    public static void setLobbySettings() {
        final List<World> worldList = new ArrayList<>();
        final List<String> worldNameList = getConfigFile().getList("lobby_settings.worlds", String.class);

        for (final World world : Bukkit.getWorlds()) {
            if (worldNameList.contains(world.getName())) {
                worldList.add(world);
            }
        }

        lobbyManager = new LobbyManager(worldList);

        setGamerules();
        setWeather();
        setTime();
    }

    public static void loadApi() {
        api = new CoolLobbyApi();
    }

    private static void setGamerules() {
        lobbyManager.setGamerule(GameRule.DO_DAYLIGHT_CYCLE, !Main.getConfigFile().getBoolean("lobby_settings.disable_daylight_cycle"));
        lobbyManager.setGamerule(GameRule.DO_MOB_SPAWNING, !Main.getConfigFile().getBoolean("lobby_settings.disable_mob_spawning"));
        lobbyManager.setGamerule(GameRule.DO_FIRE_TICK, !Main.getConfigFile().getBoolean("lobby_settings.disable_fire_tick"));
        lobbyManager.setGamerule(GameRule.DO_WEATHER_CYCLE, !Main.getConfigFile().getBoolean("lobby_settings.disable_weather_cycle"));
    }

    private static void setWeather() {
        if (!Main.getConfigFile().getBoolean("lobby_settings.default_weather.enable")) {
            return;
        }

        final String weatherType = getConfigFile().getString("lobby_settings.default_weather.type");

        switch (weatherType.toLowerCase()) {
            case "rain" -> lobbyManager.setWeather(true, false);
            case "thunder" -> lobbyManager.setWeather(true, true);
            default -> lobbyManager.setWeather(false, false);
        }
    }

    private static void setTime() {
        if (Main.getConfigFile().getBoolean("lobby_settings.default_time.enable")) {
            lobbyManager.setTime(Main.getConfigFile().getLong("lobby_settings.default_time.value"));
        }
    }

    private void setDbConnect() {
        final HashMap<String, String> dbArgs = new HashMap<>();
        dbArgs.put("PATH", getDBFilePath());

        createDBConnection(databaseFile.getString("Database.type"), dbArgs);

        try {
            dbConnect.connect();
            pluginLogger.info(ConsoleMessages.successConnectToDatabase());
        } catch (SQLException e) {
            getPluginLogger().info(ConsoleMessages.errorOccurred("Couldn't connect to the database: " + e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    private void createDBConnection(final String dbType, final HashMap<String, String> dbArgs) {
        switch (dbType.toLowerCase()) {
            default -> dbConnect = new H2Connection(dbArgs.get("PATH"));
        }
    }

    private String getDBFilePath() {
        return new File(Paths.get(this.getDataFolder().getPath(), "data").toString(), "database.db").getPath();
    }

    private static void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new VersionCheckEvent(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerChangeGameModeEvent(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerChangeWorldEvent(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerJoinEvent(), Main.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerLeaveEvent(), Main.getInstance());

        if (getConfigFile().getBoolean("lobby_settings.disable_damage")) {
           Bukkit.getPluginManager().registerEvents(new PlayerDamageEvent(), Main.getInstance());
        }

        if (getConfigFile().getBoolean("lobby_settings.disable_hunger")) {
            Bukkit.getPluginManager().registerEvents(new PlayerHungerEvent(), Main.getInstance());
        }

        if (getConfigFile().getBoolean("double_jump.enable")) {
            Bukkit.getPluginManager().registerEvents(new DoubleJumpEvent(), Main.getInstance());
        }
    }

    private void sendEnableMessage() {
        pluginLogger.info(ConsoleMessages.enablePlugin(ListUtils.listToString(getPluginDevelopers()), getPluginVersion(), getPluginContactLink()));
    }

    private void sendDisableMessage() {
        pluginLogger.info(ConsoleMessages.disablePlugin(ListUtils.listToString(getPluginDevelopers()), getPluginVersion(), getPluginContactLink()));
    }

    private void loadSettings() {
        try {
            pluginSettings = new PluginSettings();
        } catch (IOException e) {
            pluginLogger.info("An error occurred when loading the plugin settings");
            throw new RuntimeException(e);
        }
    }

    private void loadPluginInfo() {
        prefix = getMessagesFile().getString("Plugin.plugin-prefix");
        pluginVersion = this.getDescription().getVersion();
        pluginDevelopers = this.getDescription().getAuthors();
        pluginContactLink = this.getDescription().getWebsite();
    }

    private void checkPluginVersion() {
        if (configFile.getBoolean("check_updates")) {
            try {
                versionManager = new VersionManager(pluginVersion, pluginSettings.getUrlToFileVersion());

                if (versionManager.check()) {
                    pluginLogger.info(ConsoleMessages.latestPluginVersion(pluginVersion));
                    outdatedVersion = false;
                } else {
                    pluginLogger.info(ConsoleMessages.outdatedPluginVersion(pluginVersion, versionManager.getCurrentPluginVersion(), pluginSettings.getUrlToDownloadLatestVersion()));
                    outdatedVersion = true;
                }
            } catch (URISyntaxException e) {
                pluginLogger.info(ConsoleMessages.errorOccurred("Invalid link to the GitHub file. link = ".concat(versionManager.getUrlToFileVersion())));
            } catch (IOException | InterruptedException e ) {
                pluginLogger.info(ConsoleMessages.errorOccurred("Couldn't send a request to get the plugin version"));
            }
        }
    }

    private void createBstatsMetrics() {
        if (pluginSettings.isBstatsEnable()) {
            new Metrics(this, pluginSettings.getBstatsPluginId());
        }
    }

    public static void reloadDoubleJumpManager() {
        doubleJumpManager.stop();
        doubleJumpManager = new DoubleJumpManager();
    }
}
