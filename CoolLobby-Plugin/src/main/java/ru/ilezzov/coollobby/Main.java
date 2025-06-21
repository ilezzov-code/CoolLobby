package ru.ilezzov.coollobby;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.ilezzov.coollobby.api.CoolLobbyApi;
import ru.ilezzov.coollobby.database.DatabaseType;
import ru.ilezzov.coollobby.database.SQLDatabase;
import ru.ilezzov.coollobby.database.adapter.MySQLDatabase;
import ru.ilezzov.coollobby.database.adapter.PostgreSQLDatabase;
import ru.ilezzov.coollobby.database.adapter.SQLiteDatabase;
import ru.ilezzov.coollobby.database.data.player.PlayerData;
import ru.ilezzov.coollobby.database.data.player.PlayerDataRepository;
import ru.ilezzov.coollobby.database.data.spawn.SpawnDataRepository;
import ru.ilezzov.coollobby.events.EventManager;
import ru.ilezzov.coollobby.events.listeners.TimeChangeEvent;
import ru.ilezzov.coollobby.file.PluginFile;
import ru.ilezzov.coollobby.logging.Logger;
import ru.ilezzov.coollobby.logging.PaperLogger;
import ru.ilezzov.coollobby.managers.DoubleJumpManager;
import ru.ilezzov.coollobby.managers.LobbyManager;
import ru.ilezzov.coollobby.managers.VersionManager;
import ru.ilezzov.coollobby.messages.ConsoleMessages;
import ru.ilezzov.coollobby.settings.PluginSettings;
import ru.ilezzov.coollobby.stats.PluginStats;
import ru.ilezzov.coollobby.utils.ListUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static ru.ilezzov.coollobby.commands.CommandManager.loadCommands;
import static ru.ilezzov.coollobby.messages.ConsoleMessages.*;

public final class Main extends JavaPlugin {
    // API
    @Getter
    private static CoolLobbyApi api;
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
    @Getter
    private static String messageLanguage;
    @Getter
    private static boolean enableLogging;

    // Files
    @Getter
    private static PluginFile configFile;
    @Getter
    private static PluginFile messagesFile;
    @Getter
    private static PluginFile databaseFile;

    // Managers
    @Getter
    private static VersionManager versionManager;
    @Getter
    private static LobbyManager lobbyManager;
    @Getter
    private static DoubleJumpManager doubleJumpManager;

    // Database
    @Getter
    private static SQLDatabase database;

    // Data repo
    @Getter
    private static PlayerDataRepository playerDataRepository;
    @Getter
    private static SpawnDataRepository spawnDataRepository;

    // Events
    @Getter
    private static EventManager eventManager;


    @Override
    public void onEnable() {
        // Plugin startup logic
        pluginLogger = new PaperLogger(this);
        instance = this;

        // Load files
        loadSettings();
        loadFiles();

        // Load plugin info
        loadPluginInfo();

        // Check plugin version
        checkPluginVersion();

        // Connect to the database
        database = createDatabase();

        try {
            database.connect();
            database.initialize();
            pluginLogger.info(successConnectToDatabase());
        } catch (SQLException | IOException e) {
            pluginLogger.info(errorOccurred(e.getMessage()));
            throw new RuntimeException(e);
        }

        // Load managers
        loadManagers();

        // Set default lobby settings
        setLobbySettings();

        // Load Data Repo
        loadDataRepo();

        // Load API
        loadApi();

        // Insert online players in database
        insertAllPlayers();

        // Load commands and events
        loadCommands();
        loadEvents();

        // Load Metrics
        loadMetrics();

        // Send enable message
        sendEnableMessage();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (playerDataRepository != null) {
            playerDataRepository.stopAutoSave();
            playerDataRepository.flushQueue();
            playerDataRepository.saveCache();
        }

        if (spawnDataRepository != null) {
            spawnDataRepository.stopAutoSave();
            spawnDataRepository.flushQueue();
            spawnDataRepository.saveCache();
        }

        if (doubleJumpManager != null) {
            doubleJumpManager.stopTask();
        }

        stopWeatherTask();

        pluginLogger.info(ConsoleMessages.pluginDisable(ListUtils.listToString(pluginDevelopers), pluginVersion, pluginContactLink));
    }

    public static void reloadPluginInfo() {
        prefix = getMessagesFile().getString("Plugin.plugin-prefix");
        enableLogging = getConfigFile().getBoolean("logging");
    }

    public static void checkPluginVersion() {
        if (configFile.getBoolean("check_updates")) {
            try {
                versionManager = new VersionManager(pluginVersion, pluginSettings.getUrlToFileVersion());

                if (versionManager.check()) {
                    pluginLogger.info(latestPluginVersion(pluginVersion));
                    outdatedVersion = false;
                } else {
                    pluginLogger.info(legacyPluginVersion(pluginVersion, versionManager.getCurrentPluginVersion(), pluginSettings.getUrlToDownloadLatestVersion()));
                    outdatedVersion = true;
                }
            } catch (URISyntaxException e) {
                pluginLogger.info(errorOccurred("Invalid link to the GitHub file. link = ".concat(versionManager.getUrlToFileVersion())));
            } catch (IOException | InterruptedException e ) {
                pluginLogger.info(errorOccurred("Couldn't send a request to get the plugin version"));
            }
        }
    }

    public static void loadApi() {
        api = new LobbyAPI(Main.getInstance());
        pluginLogger.info(apiLoaded());
    }

    private void loadSettings() {
        try {
            pluginSettings = new PluginSettings();
        } catch (IOException e) {
            pluginLogger.info(errorOccurred(e.getMessage()));
        }
    }

    public static void loadFiles() {
        configFile = new PluginFile(Main.getInstance(), "config.yml");
        messageLanguage = configFile.getString("language");
        messagesFile = new PluginFile(Main.getInstance(), "messages/".concat(messageLanguage).concat(".yml"));
        databaseFile = new PluginFile(Main.getInstance(), "data/database.yml");
    }

    private void loadEvents() {
        eventManager = new EventManager(this);
        eventManager.loadEvents();
    }

    private void loadPluginInfo() {
        prefix = getMessagesFile().getString("Plugin.plugin-prefix");
        pluginVersion = this.getDescription().getVersion();
        pluginDevelopers = this.getDescription().getAuthors();
        pluginContactLink = this.getDescription().getWebsite();
        enableLogging = getConfigFile().getBoolean("logging");
    }

    private void loadManagers() {
        // Load Lobby manager
        loadLobbyManager();
        loadDoubleJumpManager();
    }

    public static void loadLobbyManager() {
        final List<World> lobbyWorlds = configFile.getList("lobby_settings.worlds", String.class).stream()
                .map(world -> {
                    final World loadedWorld = loadWorld(world);

                    if (loadedWorld != null) {
                        pluginLogger.info(ConsoleMessages.worldLoaded(world));
                    }

                    return loadedWorld;
                })
                .filter(Objects::nonNull)
                .toList();
        lobbyManager = new LobbyManager(lobbyWorlds);
    }
    public static void loadDoubleJumpManager() {
        doubleJumpManager = new DoubleJumpManager();
    }

    private static World loadWorld(final String world) {
        final World bukkitWorld = Bukkit.getWorld(world);

        if (bukkitWorld == null) {
            if (!doesWorldExists(world)) {
                return null;
            }
            return new WorldCreator(world).createWorld();
        }

        return bukkitWorld;
    }

    private static boolean doesWorldExists(String worldName) {
        final File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
        return worldFolder.exists() && worldFolder.isDirectory();
    }

    public static void setLobbySettings() {
        final ConfigurationSection defaultLobbyTime = configFile.getConfig().getConfigurationSection("lobby_settings.default_time");
        assert defaultLobbyTime != null;

        if (defaultLobbyTime.getBoolean("enable")) {
            final long time = defaultLobbyTime.getLong("value");
            lobbyManager.setTime(time);
        }

        final ConfigurationSection defaultLobbyWeather = configFile.getConfig().getConfigurationSection("lobby_settings.default_weather");
        assert defaultLobbyWeather != null;

        if (defaultLobbyWeather.getBoolean("enable")) {
            final String type = defaultLobbyWeather.getString("type");

            switch (Objects.requireNonNull(type).toLowerCase()) {
                case "rain" -> lobbyManager.setWeather(true, false);
                case "thunder" -> lobbyManager.setWeather(true, true);
                default -> lobbyManager.setWeather(false, false);
            }
        }
    }

    private void loadDataRepo() {
        playerDataRepository = new PlayerDataRepository(database, this);
        spawnDataRepository = new SpawnDataRepository(database, this);
    }

    public static void insertAllPlayers() {
        final List<PlayerData> data = Bukkit.getOnlinePlayers().stream()
                .map(player ->  {
                    final PlayerData playerData = getPlayerDataRepository(player);

                    if (lobbyManager.isLobby(player.getWorld())) {
                        api.setGamemode(player);
                        api.setPlayerLevel(player);
                        api.setFoodLevel(player,20);
                    }

                    player.setAllowFlight(playerData.isFly());
                    return playerData;
                })
                .toList();

        playerDataRepository.insertAll(data);
    }

    public static void stopWeatherTask() {
        if (eventManager == null) {
            return;
        }

        final Object data = eventManager.get("TimeChangeEvent");

        if (data == null) {
            return;
        }

        if (data instanceof TimeChangeEvent timeChangeEvent) {
            timeChangeEvent.stopDayTask();
        }
    }

    private static @NotNull PlayerData getPlayerDataRepository(final Player player) {
        final UUID playerUniqueId = player.getUniqueId();
        final String playerName = player.getName();
        final GameMode gameMode = player.getGameMode();
        final int expLevel = player.getLevel();
        final float expLevelExp = player.getExp();
        final int foodLevel = player.getFoodLevel();
        final boolean isFly = player.getAllowFlight();

        return new PlayerData(
                playerUniqueId,
                playerName,
                gameMode,
                expLevel,
                expLevelExp,
                foodLevel,
                isFly
        );
    }

    private void loadMetrics() {
        new PluginStats(this);
    }

    private void sendEnableMessage() {
        pluginLogger.info(pluginEnable(ListUtils.listToString(getPluginDevelopers()), getPluginVersion(), getPluginContactLink()));
    }

    private SQLDatabase createDatabase() {
        final ConfigurationSection section = databaseFile.getConfig().getConfigurationSection("Database");
        assert section != null;
        final String type = section.getString("type", "SQLITE");

        return switch (type.toUpperCase()) {
            case "MYSQL" -> new MySQLDatabase(
                    section.getString("host"),
                    section.getInt("port"),
                    section.getString("database"),
                    section.getString("username"),
                    section.getString("password"),
                    DatabaseType.MYSQL
            );
            case "POSTGRESQL" -> new PostgreSQLDatabase(
                    section.getString("host"),
                    section.getInt("port"),
                    section.getString("database"),
                    section.getString("username"),
                    section.getString("password"),
                    DatabaseType.POSTGRESQL
            );
            default -> new SQLiteDatabase(new File(getDataFolder().getPath(), "data/database.db").getPath(), DatabaseType.SQLITE);
        };
    }
}
