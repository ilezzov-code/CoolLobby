package ru.ilezzov.coollobby.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.enums.Permission;
import ru.ilezzov.coollobby.messages.ConsoleMessages;
import ru.ilezzov.coollobby.messages.PluginMessages;
import ru.ilezzov.coollobby.utils.LegacySerialize;
import ru.ilezzov.coollobby.utils.ListUtils;
import ru.ilezzov.coollobby.models.PluginPlaceholder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.ilezzov.coollobby.utils.PermissionsChecker.hasPermission;

public class MainCommand implements CommandExecutor, TabCompleter {
    private final PluginPlaceholder commandPlaceholders = new PluginPlaceholder();

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String s, final @NotNull String @NotNull [] args) {
        commandPlaceholders.addPlaceholder("{DEVELOPER}", ListUtils.listToString(Main.getPluginDevelopers()));
        commandPlaceholders.addPlaceholder("{CONTACT_LINK}", Main.getPluginContactLink());

        if(args.length == 0) {
            sender.sendMessage(PluginMessages.commandMainCommandMessage(commandPlaceholders));
            return true;
        }

        switch (args[0]) {
            case "reload" -> {
                if (hasPermission(sender, Permission.RELOAD)) {
                    Main.loadFiles();
                    Main.loadApi();
                    Main.reloadPrefix();
                    Main.setLobbySettings();
                    Main.registerCommands();
                    Main.reloadEvents();
                    Main.reloadDoubleJumpManager();

                    try {
                        Main.getDbConnect().connect();
                    } catch (SQLException e) {
                        Main.getPluginLogger().info(ConsoleMessages.errorOccurred("Couldn't connect to the database: " + e.getMessage()));
                        throw new RuntimeException(e);
                    }

                    commandPlaceholders.addPlaceholder("{P}", Main.getPrefix());
                    sender.sendMessage(PluginMessages.pluginReloadMessage(commandPlaceholders));

                    return true;
                }

                sender.sendMessage(PluginMessages.pluginNoPermsMessage(commandPlaceholders));
                return true;
            }
            case "sql-result" -> {
                if (hasPermission(sender)) {
                    try(final PreparedStatement preparedStatement = Main.getDbConnect().getConnection().prepareStatement(getSqlUrl(args));
                        final ResultSet resultSet = preparedStatement.executeQuery()) {
                        sender.sendMessage(getSqlResponse(resultSet));
                    } catch (SQLException e) {
                        sender.sendMessage(ConsoleMessages.errorOccurred(e.getMessage()));
                    }
                }
            }
            case "sql" -> {
                if (hasPermission(sender)) {
                    try(final PreparedStatement preparedStatement = Main.getDbConnect().getConnection().prepareStatement(getSqlUrl(args))) {
                        preparedStatement.execute();
                        sender.sendMessage(LegacySerialize.serialize("&7Запрос &aуспешно &7выполнен"));
                    } catch (SQLException e) {
                        sender.sendMessage(ConsoleMessages.errorOccurred(e.getMessage()));
                    }
                }
            }
            case "flymode" -> {
                if (hasPermission(sender)) {
                    final Player player = (Player) sender;
                    sender.sendMessage(String.valueOf((Main.getFlyManager().isAllowFlight(player.getUniqueId()))) + player.getAllowFlight());
                }
            }

            default -> sender.sendMessage(PluginMessages.commandMainCommandMessage(commandPlaceholders));
        }
        return true;
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

    private String getSqlUrl(final @NotNull String @NotNull [] args) {
        final StringBuilder stringBuilder = new StringBuilder();

        for (int i = 1; i < args.length; i++) {
            stringBuilder.append(args[i]).append(" ");
        }

        return stringBuilder.toString();
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String s, final @NotNull String @NotNull [] args) {
        final List<String> completions = new ArrayList<>();

        if(args.length == 1) {
            if(hasPermission(sender)) {
                completions.add("reload");
            }
        }

        return completions;
    }
}
