package ru.ilezzov.coollobby.utils;

import org.bukkit.command.CommandSender;

public class Permissions {
    //Plugin permission
    public static final String MAIN_PERMISSIONS = "cool-lobby.*";
    public static final String NO_COOLDOWN_COMMAND = "cool-lobby.no-cooldown";
    public static final String COMMAND_MAIN_COMMAND_RELOAD = "cool-lobby.reload";
    public static final String COMMAND_FW_COMMAND = "cool-lobby.fw";
    public static final String COMMAND_LT_COMMAND = "cool-lobby.lt";
    public static final String COMMAND_SPIT_COMMAND = "cool-lobby.spit";
    public static final String COMMAND_FLY_COMMAND = "cool-lobby.fly";
    public static final String DOUBLE_JUMP_PERMISSION = "cool-lobby.double_jump";


    public static boolean hasPermission(final CommandSender sender, final String permission) {
        return sender.hasPermission(MAIN_PERMISSIONS) || sender.hasPermission(permission);
    }

    public static boolean hasPermission(final CommandSender sender) {
        return sender.hasPermission(MAIN_PERMISSIONS);
    }
}
