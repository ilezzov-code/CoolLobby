package ru.ilezzov.coollobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.enums.Permission;
import ru.ilezzov.coollobby.messages.PluginMessages;
import ru.ilezzov.coollobby.models.DefaultPlaceholder;
import ru.ilezzov.coollobby.utils.ListUtils;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.ilezzov.coollobby.Main.*;
import static ru.ilezzov.coollobby.utils.PermissionsChecker.*;

public class MainCommand implements CommandExecutor, TabCompleter {
    private final DefaultPlaceholder commandPlaceholders = new DefaultPlaceholder();

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String s, final @NotNull String @NotNull [] args) {
        commandPlaceholders.addPlaceholder("{DEVELOPER}", ListUtils.listToString(getPluginDevelopers()));
        commandPlaceholders.addPlaceholder("{CONTACT_LINK}", getPluginContactLink());

        if(args.length == 0) {
            sender.sendMessage(PluginMessages.commandMainCommandMessage(commandPlaceholders.getPlaceholders()));
            return true;
        }

        switch (args[0]) {
            case "reload" -> {
                if (hasPermission(sender)) {
                    try {
                        Main.reloadFiles();
                        Main.reloadPrefix();
                        Main.setWorldSettings();
                        Main.registerCommands();
                        Main.reloadEvents();

                        commandPlaceholders.addPlaceholder("{P}", getPrefix());

                        sender.sendMessage(PluginMessages.pluginReloadMessage(commandPlaceholders.getPlaceholders()));
                    } catch (IOException e) {
                        commandPlaceholders.addPlaceholder("{ERROR}", e.getMessage());
                        sender.sendMessage(PluginMessages.pluginHasErrorMessageReload(commandPlaceholders.getPlaceholders()));

                        throw new RuntimeException(e);
                    }
                    return true;
                }

                sender.sendMessage(PluginMessages.pluginNoPermsMessage(commandPlaceholders.getPlaceholders()));
                return true;
            }
            case "sql" -> {
                if (hasPermission(sender)) {
                    try(final PreparedStatement preparedStatement = Main.getDbConnect().getConnection().prepareStatement(getSqlUrl(args));
                        final ResultSet resultSet = preparedStatement.executeQuery()) {
                        sender.sendMessage(getSqlResponse(resultSet));
                    } catch (SQLException e) {
                        commandPlaceholders.addPlaceholder("{ERROR}", e.getMessage());
                        sender.sendMessage(PluginMessages.pluginHasErrorMessageReload(commandPlaceholders.getPlaceholders()));
                    }
                }
            }
            case "flymode" -> {
                if (hasPermission(sender)) {
                    final Player player = (Player) sender;
                    sender.sendMessage(String.valueOf((Main.getFlyManager().isAllowFlight(player.getUniqueId()))) + player.getAllowFlight());
                }
            }
            case "blocktype" -> {
                if (hasPermission(sender)) {
                    final Player player = (Player) sender;
                    sender.sendMessage(String.valueOf(player.getLocation().subtract(0, 1, 0).getBlock().getType()));
                }
            }
            default -> sender.sendMessage(PluginMessages.commandMainCommandMessage(commandPlaceholders.getPlaceholders()));
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
