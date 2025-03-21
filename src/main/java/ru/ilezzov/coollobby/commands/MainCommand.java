package ru.ilezzov.pluginBlank.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.ilezzov.pluginBlank.Main;
import ru.ilezzov.pluginBlank.messages.PluginMessages;
import ru.ilezzov.pluginBlank.utils.ListUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.ilezzov.pluginBlank.Main.*;
import static ru.ilezzov.pluginBlank.utils.Permissions.*;

public class MainCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String s, final @NotNull String @NotNull [] args) {
        final HashMap<String, String> commandPlaceholders = new HashMap<>();

        commandPlaceholders.put("{P}", getPrefix());
        commandPlaceholders.put("{DEVELOPER}", ListUtils.listToString(getPluginDevelopers()));
        commandPlaceholders.put("{CONTACT_LINK}", getPluginContactLink());

        if(args.length == 0) {
            sender.sendMessage(PluginMessages.commandMainCommandMessage(commandPlaceholders));
            return true;
        }

        switch (args[0]) {
            case "reload" -> {
                if(hasPermission(sender, COMMAND_MAIN_COMMAND_RELOAD)) {
                    try {
                        Main.loadFiles();
                        Main.reloadPrefix();

                        commandPlaceholders.replace("{P}", getPrefix());

                        sender.sendMessage(PluginMessages.pluginReloadMessage(commandPlaceholders));
                    } catch (IOException e) {
                        commandPlaceholders.put("{ERROR}", e.getMessage());
                        sender.sendMessage(PluginMessages.pluginHasErrorMessageReload(commandPlaceholders));

                        throw new RuntimeException(e);
                    }
                    return true;
                }

                sender.sendMessage(PluginMessages.pluginNoPermsMessage(commandPlaceholders));
                return true;
            }
            default -> sender.sendMessage(PluginMessages.commandMainCommandMessage(commandPlaceholders));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String s, final @NotNull String @NotNull [] args) {
        final List<String> completions = new ArrayList<>();

        if(args.length == 1) {
            if(hasPermission(sender, COMMAND_MAIN_COMMAND_RELOAD)) {
                completions.add("reload");
            }
        }

        return completions;
    }
}
