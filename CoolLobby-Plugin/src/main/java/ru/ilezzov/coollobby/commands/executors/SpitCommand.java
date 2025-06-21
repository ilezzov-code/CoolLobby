package ru.ilezzov.coollobby.commands.executors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.api.CoolLobbyApi;
import ru.ilezzov.coollobby.managers.CooldownManager;
import ru.ilezzov.coollobby.messages.PluginMessages;
import ru.ilezzov.coollobby.permission.Permission;
import ru.ilezzov.coollobby.permission.PermissionsChecker;
import ru.ilezzov.coollobby.placeholder.PluginPlaceholder;

public class SpitCommand implements CommandExecutor {
    private final boolean isEnable = Main.getConfigFile().getBoolean("spit_command.enable");
    private final boolean isOnlyLobby = Main.getConfigFile().getBoolean("spit_command.only_lobby");

    private final CooldownManager cooldownManager = new CooldownManager(Main.getConfigFile().getInt("spit_command.cooldown"));
    private final PluginPlaceholder commandPlaceholders = new PluginPlaceholder();
    private final CoolLobbyApi api = Main.getApi();

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String s, final @NotNull String[] strings) {
        if (!isEnable) {
            sender.sendMessage(PluginMessages.commandDisableCommandMessage(commandPlaceholders));
            return true;
        }

        if (!(sender instanceof final Player player)) {
            sender.sendMessage(PluginMessages.pluginNoConsoleMessage(commandPlaceholders));
            return true;
        }

        if (!PermissionsChecker.hasPermission(player, Permission.SPIT_COMMAND)) {
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

        if (cooldownManager.checkCooldown(player.getUniqueId())) {
            commandPlaceholders.addPlaceholder("{TIME}", cooldownManager.getRemainingTime(player.getUniqueId()));
            sender.sendMessage(PluginMessages.pluginCommandCooldownMessage(commandPlaceholders));
            return true;
        }

        api.spawnSpit(player, true);
        sender.sendMessage(PluginMessages.commandSpitCommandMessage(commandPlaceholders));

        if (!PermissionsChecker.hasPermission(sender, Permission.NO_COOLDOWN)) {
            cooldownManager.newCooldown(player.getUniqueId());
        }

        return true;
    }
}
