package ru.ilezzov.coollobby.logging;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class PaperLogger implements Logger {
    private final CommandSender commandSender;

    public PaperLogger() {
        this.commandSender = Bukkit.getConsoleSender();
    }

    @Override
    public void info(final Component component) {
        commandSender.sendMessage(component);
    }

    @Override
    public void info(final String message) {
        commandSender.sendMessage(message);
    }

}
