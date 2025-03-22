package ru.ilezzov.coollobby.messages;

import net.kyori.adventure.text.Component;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.utils.LegacySerialize;
import ru.ilezzov.coollobby.utils.PlaceholderReplacer;

import java.util.HashMap;
import java.util.List;


public class ConsoleMessages {
    public static List<Component> pluginEnableMessage(final HashMap<String, String> placeholders) {
        return List.of(
                getComponent(""),
                getComponent("░░█████╗░░█████╗░░█████╗░██╗░░░░░░░██╗░░░░░░█████╗░██████╗░██████╗░██╗░░░██╗"),
                getComponent("░██╔══██╗██╔══██╗██╔══██╗██║░░░░░░░██║░░░░░██╔══██╗██╔══██╗██╔══██╗╚██╗░██╔╝"),
                getComponent("░██║░░╚═╝██║░░██║██║░░██║██║░░░░░░░██║░░░░░██║░░██║██████╦╝██████╦╝░╚████╔╝░"),
                getComponent("░██║░░██╗██║░░██║██║░░██║██║░░░░░░░██║░░░░░██║░░██║██╔══██╗██╔══██╗░░╚██╔╝░░"),
                getComponent("░╚█████╔╝╚█████╔╝╚█████╔╝███████╗░░███████╗╚█████╔╝██████╦╝██████╦╝░░░██║░░░"),
                getComponent("░░╚════╝░░╚════╝░░╚════╝░╚══════╝░░╚══════╝░╚════╝░╚═════╝░╚═════╝░░░░╚═╝░░░"),
                getComponent(""),
                getComponent("                            &7Plugin is &aenabled                           "),
                getComponent("                 &7Developer: {DEVELOPER} | Version: {VERSION}              ", placeholders)
        );
    }

    public static List<Component> pluginDisableMessage() {
        return List.of(
                getComponent(""),
                getComponent("░░█████╗░░█████╗░░█████╗░██╗░░░░░░░██╗░░░░░░█████╗░██████╗░██████╗░██╗░░░██╗"),
                getComponent("░██╔══██╗██╔══██╗██╔══██╗██║░░░░░░░██║░░░░░██╔══██╗██╔══██╗██╔══██╗╚██╗░██╔╝"),
                getComponent("░██║░░╚═╝██║░░██║██║░░██║██║░░░░░░░██║░░░░░██║░░██║██████╦╝██████╦╝░╚████╔╝░"),
                getComponent("░██║░░██╗██║░░██║██║░░██║██║░░░░░░░██║░░░░░██║░░██║██╔══██╗██╔══██╗░░╚██╔╝░░"),
                getComponent("░╚█████╔╝╚█████╔╝╚█████╔╝███████╗░░███████╗╚█████╔╝██████╦╝██████╦╝░░░██║░░░"),
                getComponent("░░╚════╝░░╚════╝░░╚════╝░╚══════╝░░╚══════╝░╚════╝░╚═════╝░╚═════╝░░░░╚═╝░░░"),
                getComponent(""),
                getComponent("                           &7Plugin is &cdisabled                           ")
        );
    }

    private static Component getComponent(String message, final HashMap<String, String> placeholders) {
        message = replacePlaceholder(message, placeholders);

        return LegacySerialize.serialize(message);
    }

    private static Component getComponent(final String message) {
        return LegacySerialize.serialize(message);
    }

    private static String replacePlaceholder(final String message, final HashMap<String, String> placeholders) {
        return PlaceholderReplacer.replacePlaceholder(message, placeholders);
    }
}
