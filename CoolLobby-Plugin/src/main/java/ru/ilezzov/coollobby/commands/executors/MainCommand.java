package ru.ilezzov.coollobby.commands.executors;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.commands.CommandManager;
import ru.ilezzov.coollobby.database.SQLDatabase;
import ru.ilezzov.coollobby.database.data.player.PlayerData;
import ru.ilezzov.coollobby.database.data.player.PlayerDataRepository;
import ru.ilezzov.coollobby.database.data.spawn.SpawnData;
import ru.ilezzov.coollobby.database.data.spawn.SpawnDataRepository;
import ru.ilezzov.coollobby.events.EventManager;
import ru.ilezzov.coollobby.logging.Logger;
import ru.ilezzov.coollobby.managers.VersionManager;
import ru.ilezzov.coollobby.messages.ConsoleMessages;
import ru.ilezzov.coollobby.messages.PluginMessages;
import ru.ilezzov.coollobby.permission.Permission;
import ru.ilezzov.coollobby.placeholder.PluginPlaceholder;
import ru.ilezzov.coollobby.utils.LegacySerialize;
import ru.ilezzov.coollobby.utils.ListUtils;

import java.beans.EventHandler;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ru.ilezzov.coollobby.Main.*;
import static ru.ilezzov.coollobby.messages.ConsoleMessages.errorOccurred;
import static ru.ilezzov.coollobby.messages.ConsoleMessages.successConnectToDatabase;
import static ru.ilezzov.coollobby.messages.PluginMessages.*;
import static ru.ilezzov.coollobby.permission.PermissionsChecker.hasPermission;

public class MainCommand implements CommandExecutor, TabCompleter {
    private final PluginPlaceholder commandPlaceholders = new PluginPlaceholder();
    private final PlayerDataRepository playerDataRepository = Main.getPlayerDataRepository();
    private final SpawnDataRepository spawnDataRepository = Main.getSpawnDataRepository();
    private final SQLDatabase database = Main.getDatabase();
    private final Logger logger = Main.getPluginLogger();


    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String s, final @NotNull String @NotNull [] args) {
        commandPlaceholders.addPlaceholder("{DEVELOPER}", ListUtils.listToString(getPluginDevelopers()));
        commandPlaceholders.addPlaceholder("{CONTACT_LINK}", getPluginContactLink());

        if (args.length == 0) {
            sender.sendMessage(commandMainCommandMessage(commandPlaceholders));
            return true;
        }

        switch (args[0]) {
            case "reload" -> {
                if (hasPermission(sender, Permission.RELOAD)) {
                    loadFiles();
                    reloadPluginInfo();

                    checkPluginVersion();

                    try {
                        database.connect();
                        database.initialize();
                        logger.info(successConnectToDatabase());
                    } catch (SQLException | IOException e) {
                        logger.info(errorOccurred(e.getMessage()));

                        commandPlaceholders.addPlaceholder("{ERROR}", e.getMessage());
                        sender.sendMessage(pluginHasErrorMessage(commandPlaceholders));
                        throw new RuntimeException(e);
                    }

                    Main.stopWeatherTask();
                    Main.getDoubleJumpManager().stopTask();

                    loadLobbyManager();
                    loadDoubleJumpManager();
                    setLobbySettings();

                    loadApi();

                    insertAllPlayers();

                    CommandManager.loadCommands();
                    Main.getEventManager().reloadEvents();

                    commandPlaceholders.addPlaceholder("{P}", getPrefix());
                    sender.sendMessage(pluginReloadMessage(commandPlaceholders));

                    return true;
                }

                sender.sendMessage(pluginNoPermsMessage(commandPlaceholders));
                return true;
            }
            case "version" -> {
                final VersionManager versionManager = getVersionManager();

                if (versionManager == null) {
                    sender.sendMessage(pluginHasErrorCheckVersionMessage(commandPlaceholders));
                    return true;
                }

                commandPlaceholders.addPlaceholder("{LATEST_VERS}", Main.getPluginVersion());

                if (isOutdatedVersion()) {
                    commandPlaceholders.addPlaceholder("{DOWNLOAD_LINK}", getPluginSettings().getUrlToDownloadLatestVersion());
                    commandPlaceholders.addPlaceholder("{OUTDATED_VERS}", getPluginVersion());

                    sender.sendMessage(pluginUseOutdatedVersionMessage(commandPlaceholders));
                    return true;
                }

                sender.sendMessage(pluginUseLatestVersionMessage(commandPlaceholders));
                return true;
            }
            case "sql" -> {
                if (hasPermission(sender)) {
                    try {
                        database.executeUpdate(getSqlUrl(args));
                        sender.sendMessage(LegacySerialize.serialize("&7Запрос &aуспешно &7выполнен"));
                    } catch (SQLException e) {
                        sender.sendMessage(errorOccurred(e.getMessage()));
                    }
                }
            }
            case "sql-result" -> {
                if (hasPermission(sender)) {
                    try (final ResultSet resultSet = database.executeQuery(getSqlUrl(args))) {
                        sender.sendMessage(getSqlResponse(resultSet));
                    } catch (SQLException e) {
                        sender.sendMessage(errorOccurred(e.getMessage()));
                    }
                }
            }
            case "cache" -> {
                if (hasPermission(sender)) {
                    final Map<UUID, PlayerData> playerDataCache = playerDataRepository.asMap();
                    final Map<String, SpawnData> spawnDataCache = spawnDataRepository.asMap();

                    final StringBuilder message = new StringBuilder("PlayerData cache\n");

                    message.append("| %-36s | %-16s | %-9s | %-8s | %-10s | %-5s | %-4s |\n"
                            .formatted("UUID", "Nick", "GameMode", "Exp", "Exp Level", "Food", "Fly"));

                    message.append("|------------------------------------|------------------|-----------|----------|------------|-------|------|\n");

                    for (final PlayerData playerData : playerDataCache.values()) {
                        message.append(playerData.toString()).append("\n");
                    }

                    message.append("SpawnData\n");
                    message.append("| %-16s | %-16s | %-5s | %-5s | %-5s | %-5s | %-5s | %s |".formatted("Name", "World", "x", "y", "z", "pitch", "yaw", "location"));
                    message.append("|------------------------------------|------------------|-----------|----------|------------|-------|------|\n");

                    for (final SpawnData spawnData : spawnDataCache.values()) {
                        message.append(spawnData.toString()).append("\n");
                    }

                    sender.sendMessage(message.toString());
                }
            }
            case "world" -> {
                if (!(sender instanceof final Player player)) {
                    sender.sendMessage(PluginMessages.pluginNoConsoleMessage(commandPlaceholders));
                    return true;
                }

                if (hasPermission(player)) {
                    if (args.length > 1) {
                        final World world = Bukkit.getWorld(args[1]);

                        if (world == null) {
                            commandPlaceholders.addPlaceholder("{ERROR}", "The world not found");
                            sender.sendMessage(pluginHasErrorMessage(commandPlaceholders));
                            return true;
                        }

                        final Location location = world.getSpawnLocation();
                        player.teleport(location);

                        return true;
                    }

                    commandPlaceholders.addPlaceholder("{ERROR}", "The world not found");
                    sender.sendMessage(pluginHasErrorMessage(commandPlaceholders));
                    return true;
                }
            }
            default -> sender.sendMessage(commandMainCommandMessage(commandPlaceholders));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String s, final @NotNull String @NotNull [] args) {
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("version");
            if (hasPermission(sender)) {
                completions.add("reload");
            }
        }

        if (args.length == 2) {
            if (hasPermission(sender)) {
                if (args[0].equalsIgnoreCase("world")) {
                    completions.addAll(Bukkit.getWorlds().stream().map(World::getName).toList());
                }
            }
        }

        return completions;
    }

    private String getSqlUrl(final @NotNull String @NotNull [] args) {
        final StringBuilder stringBuilder = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            stringBuilder.append(args[i]).append(" ");
        }

        return stringBuilder.toString();
    }

    private @NotNull String getSqlResponse(final ResultSet resultSet) throws SQLException {
        final StringBuilder sb = new StringBuilder();

        final ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            sb.append(metaData.getColumnName(i));
            if (i < columnCount) {
                sb.append(" | ");
            }
        }
        sb.append("\n");

        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                sb.append(resultSet.getString(i));
                if (i < columnCount) {
                    sb.append(" | ");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
