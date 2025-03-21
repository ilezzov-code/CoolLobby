package ru.ilezzov.pluginBlank.logging;

import net.kyori.adventure.text.Component;
import org.bukkit.command.ConsoleCommandSender;
import ru.ilezzov.pluginBlank.Main;

import static org.bukkit.Bukkit.*;

public class PaperLogger implements Logger {
    private final ConsoleCommandSender consoleCommandSender;

    public PaperLogger() {
        this.consoleCommandSender = getConsoleSender();
    }

    @Override
    public void info(final Component component) {
        consoleCommandSender.sendMessage(component);
    }

    @Override
    public void info(final String message) {
        consoleCommandSender.sendMessage(Main.getLegacySerialize().serialize(message));
    }
}
