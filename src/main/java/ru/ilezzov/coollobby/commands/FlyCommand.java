package ru.ilezzov.coollobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.enums.Permission;
import ru.ilezzov.coollobby.manager.CooldownManager;
import ru.ilezzov.coollobby.messages.PluginMessages;
import ru.ilezzov.coollobby.models.DefaultPlaceholder;
import ru.ilezzov.coollobby.utils.PermissionsChecker;

public class FlyCommand implements CommandExecutor {

    private final long cooldownTime = Main.getConfigFile().getInt("fly_command.cooldown");
    private final boolean isEnable = Main.getConfigFile().getBoolean("fly_command.enable");
    private final boolean isOnlyLobby = Main.getConfigFile().getBoolean("fly_command.only_lobby");
    private final CooldownManager cooldownManager = new CooldownManager(cooldownTime);
    private final DefaultPlaceholder commandPlaceholders = new DefaultPlaceholder();

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String s, final @NotNull String @NotNull [] strings) {
        if(!isEnable) {
            sender.sendMessage(PluginMessages.commandDisableCommandMessage(commandPlaceholders.getPlaceholders()));
            return true;
        }

        if (!(sender instanceof final Player player)) {
            sender.sendMessage(PluginMessages.pluginNoConsoleMessage(commandPlaceholders.getPlaceholders()));
            return true;
        }

        if (!PermissionsChecker.hasPermission(player, Permission.FLY)) {
            sender.sendMessage(PluginMessages.pluginNoPermsMessage(commandPlaceholders.getPlaceholders()));
            return true;
        }

        if (isOnlyLobby) {
            if (Main.getWorldManager().getWorld().getUID() != player.getWorld().getUID()) {
                commandPlaceholders.addPlaceholder("{WORLD}", Main.getWorldManager().getWorld().getName());
                sender.sendMessage(PluginMessages.commandOnlyLobbyCommandMessage(commandPlaceholders.getPlaceholders()));
                return true;
            }
        }

        final long cooldownTimeFinish = cooldownManager.getCooldownTime(player.getUniqueId()) - System.currentTimeMillis();

        if (cooldownTimeFinish > 0) {
            commandPlaceholders.addPlaceholder("{TIME}", String.valueOf(cooldownTimeFinish / 1000));
            sender.sendMessage(PluginMessages.pluginCooldownMessage(commandPlaceholders.getPlaceholders()));
            return true;
        }

        if (setFlightMode(player)) {
            player.sendMessage(PluginMessages.commandFlyEnableCommandMessage(commandPlaceholders.getPlaceholders()));
            Main.getFlyManager().addPlayer(player.getUniqueId(), true);
        } else {
            player.sendMessage(PluginMessages.commandFlyDisableCommandMessage(commandPlaceholders.getPlaceholders()));
            Main.getFlyManager().addPlayer(player.getUniqueId(), false);
        }

        if(!PermissionsChecker.hasPermission(player, Permission.NO_COOLDOWN)) {
            cooldownManager.newCooldown(player.getUniqueId());
        }

        return true;
    }

    private boolean setFlightMode(final Player player) {
        if (Main.getFlyManager().isAllowFlight(player.getUniqueId())) {
            player.setFlying(false);
            player.setAllowFlight(false);

            return false;
        } else {
            player.setAllowFlight(true);
            return true;
        }
    }
}
