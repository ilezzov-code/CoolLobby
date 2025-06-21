package ru.ilezzov.coollobby.permission;

import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class PermissionsChecker {
    public static boolean hasPermission(final CommandSender sender) {
        return sender.hasPermission(Permission.MAIN.getPermission());
    }

    public static boolean hasPermission(final CommandSender sender, final Permission... permissions) {
        if (hasPermission(sender)) {
            return true;
        }
        return Arrays.stream(permissions).anyMatch(permission -> sender.hasPermission(permission.getPermission()));
    }
}
