package ru.ilezzov.coollobby.messages;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.file.PluginFile;
import ru.ilezzov.coollobby.placeholder.PluginPlaceholder;
import ru.ilezzov.coollobby.utils.LegacySerialize;
import ru.ilezzov.coollobby.utils.PlaceholderReplacer;

import java.util.UUID;

public class PluginMessages {
    private static PluginFile getMessages() {
        return Main.getMessagesFile();
    }

    public static Component pluginUseLatestVersionMessage(final PluginPlaceholder placeholder) {
        return getComponent("Plugin.plugin-use-latest-version", placeholder);
    }

    public static Component pluginUseOutdatedVersionMessage(final PluginPlaceholder placeholder) {
        return getComponent("Plugin.plugin-use-outdated-version", placeholder);
    }

    public static Component pluginHasErrorMessage(final PluginPlaceholder placeholder) {
        return getComponent("Plugin.plugin-has-error", placeholder);
    }

    public static Component pluginHasErrorCheckVersionMessage(final PluginPlaceholder placeholder) {
        return getComponent("Plugin.plugin-has-error-check-version", placeholder);
    }

    public static Component pluginReloadMessage(final PluginPlaceholder placeholder) {
        return getComponent("Plugin.plugin-reload", placeholder);
    }

    public static Component pluginNoPermsMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-permission-error", placeholder);
    }

    public static Component pluginNoConsoleMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-no-console-error", placeholder);
    }

    public static Component pluginCommandCooldownMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-cooldown", placeholder);
    }

    public static Component pluginPlayerNotFoundMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-player-not-found", placeholder);
    }

    public static Component commandMainCommandMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-main-message", placeholder);
    }

    public static Component commandDisableCommandMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-disable", placeholder);
    }

    public static Component commandOnlyLobbyUse(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-only-lobby-use", placeholder);
    }

    public static Component commandFireworkCommandMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-firework-message", placeholder);
    }

    public static Component commandLightingCommandMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-lighting-message", placeholder);
    }

    public static Component commandSpitCommandMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-spit-message", placeholder);
    }

    public static Component commandFlyEnableCommandMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-fly-enable-message", placeholder);
    }

    public static Component commandFlyDisableCommandMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-fly-disable-message", placeholder);
    }

    public static Component commandSpawnTeleportMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-spawn-teleport-message", placeholder);
    }

    public static Component commandSpawnTeleportOtherMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-spawn-teleport-other-message", placeholder);
    }

    public static Component commandSpawnTeleportByOtherMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-spawn-teleport-by-other-message", placeholder);
    }

    public static Component commandSpawnNotSetMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-spawn-not-set-message", placeholder);
    }

    public static Component commandSpawnSetMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-spawn-set-message", placeholder);
    }

    public static Component commandSpawnSetOverwriteMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-spawn-set-overwrite-message", placeholder);
    }

    public static Component commandSpawnNotFoundConfirmMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-spawn-not-found-confirm-message", placeholder);
    }

    public static Component commandSpawnRemoveMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-spawn-remove-message", placeholder);
    }

    public static Component commandSpawnNotFoundMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-spawn-not-found-message", placeholder);
    }

    public static Component commandSpawnHelpMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.command-spawn-help-message", placeholder);
    }

    public static Component playerJoinMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.player-join-message", placeholder);
    }

    public static Component playerJoinGlobalMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.player-join-global-message", placeholder);
    }
    
    public static Component playerLeaveGlobalMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.player-leave-global-message", placeholder);
    }

    public static Component playerDoubleJumpMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.player-double-jump-message", placeholder);
    }

    public static Component playerDoubleJumpActionBarCooldownMessage(final PluginPlaceholder placeholder) {
        return getComponent("Messages.player-double-jump-action-bar-cooldown", placeholder);
    }

    public static Component playerJoinTitleTitle() {
        return getComponent("Title.player-join-title");
    }

    public static Component playerJoinTitleSubtitle() {
        return getComponent("Title.player-join-subtitle");
    }

    private static Component getComponent(final String key) {
        final String message = getMessages().getConfig().getString(key);

        return LegacySerialize.serialize(message);
    }

    private static Component getComponent(final String key, final PluginPlaceholder placeholder) {
        String message = getMessages().getString(key);

        if (message == null || message.isBlank()) {
            return null;
        }

        message = replacePlaceholder(message, placeholder);

        return LegacySerialize.serialize(message);
    }

    private static String replacePlaceholder(final String message, final PluginPlaceholder placeholder) {
        return PlaceholderReplacer.replacePlaceholder(message, placeholder);
    }
}
