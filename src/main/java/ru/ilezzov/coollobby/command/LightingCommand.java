package ru.ilezzov.coollobby.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.enums.Permission;
import ru.ilezzov.coollobby.managers.CooldownManager;
import ru.ilezzov.coollobby.messages.PluginMessages;
import ru.ilezzov.coollobby.models.PluginPlaceholder;
import ru.ilezzov.coollobby.utils.PermissionsChecker;

public class LightingCommand implements CommandExecutor {
    private final boolean isEnable = Main.getConfigFile().getBoolean("lt_command.enable");
    private final boolean isOnlyLobby = Main.getConfigFile().getBoolean("lt_command.only_lobby");

    private final CooldownManager cooldownManager = new CooldownManager(Main.getConfigFile().getInt("lt_command.cooldown"));
    private final PluginPlaceholder commandPlaceholders = new PluginPlaceholder();

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String s, final @NotNull String[] strings) {
        if(!isEnable) {
            sender.sendMessage(PluginMessages.commandDisableCommandMessage(commandPlaceholders));
            return true;
        }

        if (!(sender instanceof final Player player)) {
            sender.sendMessage(PluginMessages.pluginNoConsoleMessage(commandPlaceholders));
            return true;
        }

        if (!PermissionsChecker.hasPermission(player, Permission.LT_COMMAND)) {
            sender.sendMessage(PluginMessages.pluginNoPermsMessage(commandPlaceholders));
            return true;
        }

        if (isOnlyLobby) {
            if (!Main.getLobbyManager().isLobby(player.getWorld())) {
                commandPlaceholders.addPlaceholder("{WORLD}", Main.getLobbyManager().getLobbyWorldsName());
                sender.sendMessage(PluginMessages.commandOnlyLobbyUse(commandPlaceholders));
                return true;
            }
        }

        if (!cooldownManager.checkCooldown(player.getUniqueId())) {
            commandPlaceholders.addPlaceholder("{TIME}", cooldownManager.getRemainingTime(player.getUniqueId()));
            sender.sendMessage(PluginMessages.pluginCommandCooldownMessage(commandPlaceholders));
            return true;
        }

        Main.getApi().sendLighting(player);
        sender.sendMessage(PluginMessages.commandLightingCommandMessage(commandPlaceholders));

        if (!PermissionsChecker.hasPermission(sender, Permission.NO_COOLDOWN)) {
            cooldownManager.newCooldown(player.getUniqueId());
        }

        return true;
    }
}
